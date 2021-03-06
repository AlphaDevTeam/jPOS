package com.alphadevs.web.pos.web.rest;

import com.alphadevs.web.pos.JPosApp;

import com.alphadevs.web.pos.domain.ContactInfo;
import com.alphadevs.web.pos.repository.ContactInfoRepository;
import com.alphadevs.web.pos.service.ContactInfoService;
import com.alphadevs.web.pos.repository.search.ContactInfoSearchRepository;
import com.alphadevs.web.pos.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContactInfoResource REST controller.
 *
 * @see ContactInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JPosApp.class)
public class ContactInfoResourceIntTest {

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    @Autowired
    private ContactInfoService contactInfoService;

    @Autowired
    private ContactInfoSearchRepository contactInfoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContactInfoMockMvc;

    private ContactInfo contactInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactInfoResource contactInfoResource = new ContactInfoResource(contactInfoService);
        this.restContactInfoMockMvc = MockMvcBuilders.standaloneSetup(contactInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactInfo createEntity(EntityManager em) {
        ContactInfo contactInfo = new ContactInfo()
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .otherPhone(DEFAULT_OTHER_PHONE)
            .email(DEFAULT_EMAIL);
        return contactInfo;
    }

    @Before
    public void initTest() {
        contactInfoSearchRepository.deleteAll();
        contactInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactInfo() throws Exception {
        int databaseSizeBeforeCreate = contactInfoRepository.findAll().size();

        // Create the ContactInfo
        restContactInfoMockMvc.perform(post("/api/contact-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactInfo)))
            .andExpect(status().isCreated());

        // Validate the ContactInfo in the database
        List<ContactInfo> contactInfoList = contactInfoRepository.findAll();
        assertThat(contactInfoList).hasSize(databaseSizeBeforeCreate + 1);
        ContactInfo testContactInfo = contactInfoList.get(contactInfoList.size() - 1);
        assertThat(testContactInfo.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testContactInfo.getOtherPhone()).isEqualTo(DEFAULT_OTHER_PHONE);
        assertThat(testContactInfo.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the ContactInfo in Elasticsearch
        ContactInfo contactInfoEs = contactInfoSearchRepository.findOne(testContactInfo.getId());
        assertThat(contactInfoEs).isEqualToComparingFieldByField(testContactInfo);
    }

    @Test
    @Transactional
    public void createContactInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactInfoRepository.findAll().size();

        // Create the ContactInfo with an existing ID
        contactInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactInfoMockMvc.perform(post("/api/contact-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactInfo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ContactInfo> contactInfoList = contactInfoRepository.findAll();
        assertThat(contactInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMobileNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactInfoRepository.findAll().size();
        // set the field null
        contactInfo.setMobileNumber(null);

        // Create the ContactInfo, which fails.

        restContactInfoMockMvc.perform(post("/api/contact-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactInfo)))
            .andExpect(status().isBadRequest());

        List<ContactInfo> contactInfoList = contactInfoRepository.findAll();
        assertThat(contactInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactInfos() throws Exception {
        // Initialize the database
        contactInfoRepository.saveAndFlush(contactInfo);

        // Get all the contactInfoList
        restContactInfoMockMvc.perform(get("/api/contact-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].otherPhone").value(hasItem(DEFAULT_OTHER_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getContactInfo() throws Exception {
        // Initialize the database
        contactInfoRepository.saveAndFlush(contactInfo);

        // Get the contactInfo
        restContactInfoMockMvc.perform(get("/api/contact-infos/{id}", contactInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contactInfo.getId().intValue()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER.toString()))
            .andExpect(jsonPath("$.otherPhone").value(DEFAULT_OTHER_PHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactInfo() throws Exception {
        // Get the contactInfo
        restContactInfoMockMvc.perform(get("/api/contact-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactInfo() throws Exception {
        // Initialize the database
        contactInfoService.save(contactInfo);

        int databaseSizeBeforeUpdate = contactInfoRepository.findAll().size();

        // Update the contactInfo
        ContactInfo updatedContactInfo = contactInfoRepository.findOne(contactInfo.getId());
        updatedContactInfo
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .otherPhone(UPDATED_OTHER_PHONE)
            .email(UPDATED_EMAIL);

        restContactInfoMockMvc.perform(put("/api/contact-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactInfo)))
            .andExpect(status().isOk());

        // Validate the ContactInfo in the database
        List<ContactInfo> contactInfoList = contactInfoRepository.findAll();
        assertThat(contactInfoList).hasSize(databaseSizeBeforeUpdate);
        ContactInfo testContactInfo = contactInfoList.get(contactInfoList.size() - 1);
        assertThat(testContactInfo.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testContactInfo.getOtherPhone()).isEqualTo(UPDATED_OTHER_PHONE);
        assertThat(testContactInfo.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the ContactInfo in Elasticsearch
        ContactInfo contactInfoEs = contactInfoSearchRepository.findOne(testContactInfo.getId());
        assertThat(contactInfoEs).isEqualToComparingFieldByField(testContactInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingContactInfo() throws Exception {
        int databaseSizeBeforeUpdate = contactInfoRepository.findAll().size();

        // Create the ContactInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContactInfoMockMvc.perform(put("/api/contact-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactInfo)))
            .andExpect(status().isCreated());

        // Validate the ContactInfo in the database
        List<ContactInfo> contactInfoList = contactInfoRepository.findAll();
        assertThat(contactInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContactInfo() throws Exception {
        // Initialize the database
        contactInfoService.save(contactInfo);

        int databaseSizeBeforeDelete = contactInfoRepository.findAll().size();

        // Get the contactInfo
        restContactInfoMockMvc.perform(delete("/api/contact-infos/{id}", contactInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean contactInfoExistsInEs = contactInfoSearchRepository.exists(contactInfo.getId());
        assertThat(contactInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<ContactInfo> contactInfoList = contactInfoRepository.findAll();
        assertThat(contactInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContactInfo() throws Exception {
        // Initialize the database
        contactInfoService.save(contactInfo);

        // Search the contactInfo
        restContactInfoMockMvc.perform(get("/api/_search/contact-infos?query=id:" + contactInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].otherPhone").value(hasItem(DEFAULT_OTHER_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactInfo.class);
        ContactInfo contactInfo1 = new ContactInfo();
        contactInfo1.setId(1L);
        ContactInfo contactInfo2 = new ContactInfo();
        contactInfo2.setId(contactInfo1.getId());
        assertThat(contactInfo1).isEqualTo(contactInfo2);
        contactInfo2.setId(2L);
        assertThat(contactInfo1).isNotEqualTo(contactInfo2);
        contactInfo1.setId(null);
        assertThat(contactInfo1).isNotEqualTo(contactInfo2);
    }
}
