package com.alphadevs.web.pos.web.rest;

import com.alphadevs.web.pos.JPosApp;

import com.alphadevs.web.pos.domain.CustomerCategory;
import com.alphadevs.web.pos.repository.CustomerCategoryRepository;
import com.alphadevs.web.pos.service.CustomerCategoryService;
import com.alphadevs.web.pos.repository.search.CustomerCategorySearchRepository;
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
 * Test class for the CustomerCategoryResource REST controller.
 *
 * @see CustomerCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JPosApp.class)
public class CustomerCategoryResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CustomerCategoryRepository customerCategoryRepository;

    @Autowired
    private CustomerCategoryService customerCategoryService;

    @Autowired
    private CustomerCategorySearchRepository customerCategorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomerCategoryMockMvc;

    private CustomerCategory customerCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerCategoryResource customerCategoryResource = new CustomerCategoryResource(customerCategoryService);
        this.restCustomerCategoryMockMvc = MockMvcBuilders.standaloneSetup(customerCategoryResource)
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
    public static CustomerCategory createEntity(EntityManager em) {
        CustomerCategory customerCategory = new CustomerCategory()
            .description(DEFAULT_DESCRIPTION);
        return customerCategory;
    }

    @Before
    public void initTest() {
        customerCategorySearchRepository.deleteAll();
        customerCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerCategory() throws Exception {
        int databaseSizeBeforeCreate = customerCategoryRepository.findAll().size();

        // Create the CustomerCategory
        restCustomerCategoryMockMvc.perform(post("/api/customer-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerCategory)))
            .andExpect(status().isCreated());

        // Validate the CustomerCategory in the database
        List<CustomerCategory> customerCategoryList = customerCategoryRepository.findAll();
        assertThat(customerCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerCategory testCustomerCategory = customerCategoryList.get(customerCategoryList.size() - 1);
        assertThat(testCustomerCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the CustomerCategory in Elasticsearch
        CustomerCategory customerCategoryEs = customerCategorySearchRepository.findOne(testCustomerCategory.getId());
        assertThat(customerCategoryEs).isEqualToComparingFieldByField(testCustomerCategory);
    }

    @Test
    @Transactional
    public void createCustomerCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerCategoryRepository.findAll().size();

        // Create the CustomerCategory with an existing ID
        customerCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerCategoryMockMvc.perform(post("/api/customer-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerCategory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CustomerCategory> customerCategoryList = customerCategoryRepository.findAll();
        assertThat(customerCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerCategoryRepository.findAll().size();
        // set the field null
        customerCategory.setDescription(null);

        // Create the CustomerCategory, which fails.

        restCustomerCategoryMockMvc.perform(post("/api/customer-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerCategory)))
            .andExpect(status().isBadRequest());

        List<CustomerCategory> customerCategoryList = customerCategoryRepository.findAll();
        assertThat(customerCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerCategories() throws Exception {
        // Initialize the database
        customerCategoryRepository.saveAndFlush(customerCategory);

        // Get all the customerCategoryList
        restCustomerCategoryMockMvc.perform(get("/api/customer-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCustomerCategory() throws Exception {
        // Initialize the database
        customerCategoryRepository.saveAndFlush(customerCategory);

        // Get the customerCategory
        restCustomerCategoryMockMvc.perform(get("/api/customer-categories/{id}", customerCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerCategory.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerCategory() throws Exception {
        // Get the customerCategory
        restCustomerCategoryMockMvc.perform(get("/api/customer-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerCategory() throws Exception {
        // Initialize the database
        customerCategoryService.save(customerCategory);

        int databaseSizeBeforeUpdate = customerCategoryRepository.findAll().size();

        // Update the customerCategory
        CustomerCategory updatedCustomerCategory = customerCategoryRepository.findOne(customerCategory.getId());
        updatedCustomerCategory
            .description(UPDATED_DESCRIPTION);

        restCustomerCategoryMockMvc.perform(put("/api/customer-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomerCategory)))
            .andExpect(status().isOk());

        // Validate the CustomerCategory in the database
        List<CustomerCategory> customerCategoryList = customerCategoryRepository.findAll();
        assertThat(customerCategoryList).hasSize(databaseSizeBeforeUpdate);
        CustomerCategory testCustomerCategory = customerCategoryList.get(customerCategoryList.size() - 1);
        assertThat(testCustomerCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the CustomerCategory in Elasticsearch
        CustomerCategory customerCategoryEs = customerCategorySearchRepository.findOne(testCustomerCategory.getId());
        assertThat(customerCategoryEs).isEqualToComparingFieldByField(testCustomerCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerCategory() throws Exception {
        int databaseSizeBeforeUpdate = customerCategoryRepository.findAll().size();

        // Create the CustomerCategory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomerCategoryMockMvc.perform(put("/api/customer-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerCategory)))
            .andExpect(status().isCreated());

        // Validate the CustomerCategory in the database
        List<CustomerCategory> customerCategoryList = customerCategoryRepository.findAll();
        assertThat(customerCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomerCategory() throws Exception {
        // Initialize the database
        customerCategoryService.save(customerCategory);

        int databaseSizeBeforeDelete = customerCategoryRepository.findAll().size();

        // Get the customerCategory
        restCustomerCategoryMockMvc.perform(delete("/api/customer-categories/{id}", customerCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean customerCategoryExistsInEs = customerCategorySearchRepository.exists(customerCategory.getId());
        assertThat(customerCategoryExistsInEs).isFalse();

        // Validate the database is empty
        List<CustomerCategory> customerCategoryList = customerCategoryRepository.findAll();
        assertThat(customerCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomerCategory() throws Exception {
        // Initialize the database
        customerCategoryService.save(customerCategory);

        // Search the customerCategory
        restCustomerCategoryMockMvc.perform(get("/api/_search/customer-categories?query=id:" + customerCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerCategory.class);
        CustomerCategory customerCategory1 = new CustomerCategory();
        customerCategory1.setId(1L);
        CustomerCategory customerCategory2 = new CustomerCategory();
        customerCategory2.setId(customerCategory1.getId());
        assertThat(customerCategory1).isEqualTo(customerCategory2);
        customerCategory2.setId(2L);
        assertThat(customerCategory1).isNotEqualTo(customerCategory2);
        customerCategory1.setId(null);
        assertThat(customerCategory1).isNotEqualTo(customerCategory2);
    }
}
