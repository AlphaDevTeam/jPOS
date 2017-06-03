package com.alphadevs.web.pos.web.rest;

import com.alphadevs.web.pos.JPosApp;

import com.alphadevs.web.pos.domain.Design;
import com.alphadevs.web.pos.domain.Product;
import com.alphadevs.web.pos.repository.DesignRepository;
import com.alphadevs.web.pos.service.DesignService;
import com.alphadevs.web.pos.repository.search.DesignSearchRepository;
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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DesignResource REST controller.
 *
 * @see DesignResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JPosApp.class)
public class DesignResourceIntTest {

    private static final String DEFAULT_DESIGN_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGN_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DESIGN_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PROFIT_PERC = new BigDecimal(1);
    private static final BigDecimal UPDATED_PROFIT_PERC = new BigDecimal(2);

    private static final String DEFAULT_DESIGN_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_DESIGN_PREFIX = "BBBBBBBBBB";

    @Autowired
    private DesignRepository designRepository;

    @Autowired
    private DesignService designService;

    @Autowired
    private DesignSearchRepository designSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDesignMockMvc;

    private Design design;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DesignResource designResource = new DesignResource(designService);
        this.restDesignMockMvc = MockMvcBuilders.standaloneSetup(designResource)
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
    public static Design createEntity(EntityManager em) {
        Design design = new Design()
            .designDescription(DEFAULT_DESIGN_DESCRIPTION)
            .designCode(DEFAULT_DESIGN_CODE)
            .profitPerc(DEFAULT_PROFIT_PERC)
            .designPrefix(DEFAULT_DESIGN_PREFIX);
        // Add required entity
        Product relatedProduct = ProductResourceIntTest.createEntity(em);
        em.persist(relatedProduct);
        em.flush();
        design.setRelatedProduct(relatedProduct);
        return design;
    }

    @Before
    public void initTest() {
        designSearchRepository.deleteAll();
        design = createEntity(em);
    }

    @Test
    @Transactional
    public void createDesign() throws Exception {
        int databaseSizeBeforeCreate = designRepository.findAll().size();

        // Create the Design
        restDesignMockMvc.perform(post("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(design)))
            .andExpect(status().isCreated());

        // Validate the Design in the database
        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeCreate + 1);
        Design testDesign = designList.get(designList.size() - 1);
        assertThat(testDesign.getDesignDescription()).isEqualTo(DEFAULT_DESIGN_DESCRIPTION);
        assertThat(testDesign.getDesignCode()).isEqualTo(DEFAULT_DESIGN_CODE);
        assertThat(testDesign.getProfitPerc()).isEqualTo(DEFAULT_PROFIT_PERC);
        assertThat(testDesign.getDesignPrefix()).isEqualTo(DEFAULT_DESIGN_PREFIX);

        // Validate the Design in Elasticsearch
        Design designEs = designSearchRepository.findOne(testDesign.getId());
        assertThat(designEs).isEqualToComparingFieldByField(testDesign);
    }

    @Test
    @Transactional
    public void createDesignWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = designRepository.findAll().size();

        // Create the Design with an existing ID
        design.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDesignMockMvc.perform(post("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(design)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDesignDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = designRepository.findAll().size();
        // set the field null
        design.setDesignDescription(null);

        // Create the Design, which fails.

        restDesignMockMvc.perform(post("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(design)))
            .andExpect(status().isBadRequest());

        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDesignCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = designRepository.findAll().size();
        // set the field null
        design.setDesignCode(null);

        // Create the Design, which fails.

        restDesignMockMvc.perform(post("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(design)))
            .andExpect(status().isBadRequest());

        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDesignPrefixIsRequired() throws Exception {
        int databaseSizeBeforeTest = designRepository.findAll().size();
        // set the field null
        design.setDesignPrefix(null);

        // Create the Design, which fails.

        restDesignMockMvc.perform(post("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(design)))
            .andExpect(status().isBadRequest());

        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDesigns() throws Exception {
        // Initialize the database
        designRepository.saveAndFlush(design);

        // Get all the designList
        restDesignMockMvc.perform(get("/api/designs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(design.getId().intValue())))
            .andExpect(jsonPath("$.[*].designDescription").value(hasItem(DEFAULT_DESIGN_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].designCode").value(hasItem(DEFAULT_DESIGN_CODE.toString())))
            .andExpect(jsonPath("$.[*].profitPerc").value(hasItem(DEFAULT_PROFIT_PERC.intValue())))
            .andExpect(jsonPath("$.[*].designPrefix").value(hasItem(DEFAULT_DESIGN_PREFIX.toString())));
    }

    @Test
    @Transactional
    public void getDesign() throws Exception {
        // Initialize the database
        designRepository.saveAndFlush(design);

        // Get the design
        restDesignMockMvc.perform(get("/api/designs/{id}", design.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(design.getId().intValue()))
            .andExpect(jsonPath("$.designDescription").value(DEFAULT_DESIGN_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.designCode").value(DEFAULT_DESIGN_CODE.toString()))
            .andExpect(jsonPath("$.profitPerc").value(DEFAULT_PROFIT_PERC.intValue()))
            .andExpect(jsonPath("$.designPrefix").value(DEFAULT_DESIGN_PREFIX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDesign() throws Exception {
        // Get the design
        restDesignMockMvc.perform(get("/api/designs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDesign() throws Exception {
        // Initialize the database
        designService.save(design);

        int databaseSizeBeforeUpdate = designRepository.findAll().size();

        // Update the design
        Design updatedDesign = designRepository.findOne(design.getId());
        updatedDesign
            .designDescription(UPDATED_DESIGN_DESCRIPTION)
            .designCode(UPDATED_DESIGN_CODE)
            .profitPerc(UPDATED_PROFIT_PERC)
            .designPrefix(UPDATED_DESIGN_PREFIX);

        restDesignMockMvc.perform(put("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDesign)))
            .andExpect(status().isOk());

        // Validate the Design in the database
        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeUpdate);
        Design testDesign = designList.get(designList.size() - 1);
        assertThat(testDesign.getDesignDescription()).isEqualTo(UPDATED_DESIGN_DESCRIPTION);
        assertThat(testDesign.getDesignCode()).isEqualTo(UPDATED_DESIGN_CODE);
        assertThat(testDesign.getProfitPerc()).isEqualTo(UPDATED_PROFIT_PERC);
        assertThat(testDesign.getDesignPrefix()).isEqualTo(UPDATED_DESIGN_PREFIX);

        // Validate the Design in Elasticsearch
        Design designEs = designSearchRepository.findOne(testDesign.getId());
        assertThat(designEs).isEqualToComparingFieldByField(testDesign);
    }

    @Test
    @Transactional
    public void updateNonExistingDesign() throws Exception {
        int databaseSizeBeforeUpdate = designRepository.findAll().size();

        // Create the Design

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDesignMockMvc.perform(put("/api/designs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(design)))
            .andExpect(status().isCreated());

        // Validate the Design in the database
        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDesign() throws Exception {
        // Initialize the database
        designService.save(design);

        int databaseSizeBeforeDelete = designRepository.findAll().size();

        // Get the design
        restDesignMockMvc.perform(delete("/api/designs/{id}", design.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean designExistsInEs = designSearchRepository.exists(design.getId());
        assertThat(designExistsInEs).isFalse();

        // Validate the database is empty
        List<Design> designList = designRepository.findAll();
        assertThat(designList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDesign() throws Exception {
        // Initialize the database
        designService.save(design);

        // Search the design
        restDesignMockMvc.perform(get("/api/_search/designs?query=id:" + design.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(design.getId().intValue())))
            .andExpect(jsonPath("$.[*].designDescription").value(hasItem(DEFAULT_DESIGN_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].designCode").value(hasItem(DEFAULT_DESIGN_CODE.toString())))
            .andExpect(jsonPath("$.[*].profitPerc").value(hasItem(DEFAULT_PROFIT_PERC.intValue())))
            .andExpect(jsonPath("$.[*].designPrefix").value(hasItem(DEFAULT_DESIGN_PREFIX.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Design.class);
        Design design1 = new Design();
        design1.setId(1L);
        Design design2 = new Design();
        design2.setId(design1.getId());
        assertThat(design1).isEqualTo(design2);
        design2.setId(2L);
        assertThat(design1).isNotEqualTo(design2);
        design1.setId(null);
        assertThat(design1).isNotEqualTo(design2);
    }
}
