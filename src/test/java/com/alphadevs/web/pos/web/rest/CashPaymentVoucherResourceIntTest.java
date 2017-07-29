package com.alphadevs.web.pos.web.rest;

import com.alphadevs.web.pos.JPosApp;

import com.alphadevs.web.pos.domain.CashPaymentVoucher;
import com.alphadevs.web.pos.domain.Location;
import com.alphadevs.web.pos.domain.Customer;
import com.alphadevs.web.pos.repository.CashPaymentVoucherRepository;
import com.alphadevs.web.pos.service.CashPaymentVoucherService;
import com.alphadevs.web.pos.repository.search.CashPaymentVoucherSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CashPaymentVoucherResource REST controller.
 *
 * @see CashPaymentVoucherResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JPosApp.class)
public class CashPaymentVoucherResourceIntTest {

    private static final String DEFAULT_PAYMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_REF_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REF_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);

    @Autowired
    private CashPaymentVoucherRepository cashPaymentVoucherRepository;

    @Autowired
    private CashPaymentVoucherService cashPaymentVoucherService;

    @Autowired
    private CashPaymentVoucherSearchRepository cashPaymentVoucherSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCashPaymentVoucherMockMvc;

    private CashPaymentVoucher cashPaymentVoucher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CashPaymentVoucherResource cashPaymentVoucherResource = new CashPaymentVoucherResource(cashPaymentVoucherService);
        this.restCashPaymentVoucherMockMvc = MockMvcBuilders.standaloneSetup(cashPaymentVoucherResource)
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
    public static CashPaymentVoucher createEntity(EntityManager em) {
        CashPaymentVoucher cashPaymentVoucher = new CashPaymentVoucher()
            .paymentNumber(DEFAULT_PAYMENT_NUMBER)
            .paymentRefNumber(DEFAULT_PAYMENT_REF_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT);
        // Add required entity
        Location relatedLocation = LocationResourceIntTest.createEntity(em);
        em.persist(relatedLocation);
        em.flush();
        cashPaymentVoucher.setRelatedLocation(relatedLocation);
        // Add required entity
        Customer relatedCustomer = CustomerResourceIntTest.createEntity(em);
        em.persist(relatedCustomer);
        em.flush();
        cashPaymentVoucher.setRelatedCustomer(relatedCustomer);
        return cashPaymentVoucher;
    }

    @Before
    public void initTest() {
        cashPaymentVoucherSearchRepository.deleteAll();
        cashPaymentVoucher = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashPaymentVoucher() throws Exception {
        int databaseSizeBeforeCreate = cashPaymentVoucherRepository.findAll().size();

        // Create the CashPaymentVoucher
        restCashPaymentVoucherMockMvc.perform(post("/api/cash-payment-vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucher)))
            .andExpect(status().isCreated());

        // Validate the CashPaymentVoucher in the database
        List<CashPaymentVoucher> cashPaymentVoucherList = cashPaymentVoucherRepository.findAll();
        assertThat(cashPaymentVoucherList).hasSize(databaseSizeBeforeCreate + 1);
        CashPaymentVoucher testCashPaymentVoucher = cashPaymentVoucherList.get(cashPaymentVoucherList.size() - 1);
        assertThat(testCashPaymentVoucher.getPaymentNumber()).isEqualTo(DEFAULT_PAYMENT_NUMBER);
        assertThat(testCashPaymentVoucher.getPaymentRefNumber()).isEqualTo(DEFAULT_PAYMENT_REF_NUMBER);
        assertThat(testCashPaymentVoucher.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCashPaymentVoucher.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testCashPaymentVoucher.getPaymentAmount()).isEqualTo(DEFAULT_PAYMENT_AMOUNT);

        // Validate the CashPaymentVoucher in Elasticsearch
        CashPaymentVoucher cashPaymentVoucherEs = cashPaymentVoucherSearchRepository.findOne(testCashPaymentVoucher.getId());
        assertThat(cashPaymentVoucherEs).isEqualToComparingFieldByField(testCashPaymentVoucher);
    }

    @Test
    @Transactional
    public void createCashPaymentVoucherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashPaymentVoucherRepository.findAll().size();

        // Create the CashPaymentVoucher with an existing ID
        cashPaymentVoucher.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashPaymentVoucherMockMvc.perform(post("/api/cash-payment-vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucher)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CashPaymentVoucher> cashPaymentVoucherList = cashPaymentVoucherRepository.findAll();
        assertThat(cashPaymentVoucherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPaymentNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherRepository.findAll().size();
        // set the field null
        cashPaymentVoucher.setPaymentNumber(null);

        // Create the CashPaymentVoucher, which fails.

        restCashPaymentVoucherMockMvc.perform(post("/api/cash-payment-vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucher)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucher> cashPaymentVoucherList = cashPaymentVoucherRepository.findAll();
        assertThat(cashPaymentVoucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherRepository.findAll().size();
        // set the field null
        cashPaymentVoucher.setDescription(null);

        // Create the CashPaymentVoucher, which fails.

        restCashPaymentVoucherMockMvc.perform(post("/api/cash-payment-vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucher)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucher> cashPaymentVoucherList = cashPaymentVoucherRepository.findAll();
        assertThat(cashPaymentVoucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherRepository.findAll().size();
        // set the field null
        cashPaymentVoucher.setPaymentDate(null);

        // Create the CashPaymentVoucher, which fails.

        restCashPaymentVoucherMockMvc.perform(post("/api/cash-payment-vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucher)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucher> cashPaymentVoucherList = cashPaymentVoucherRepository.findAll();
        assertThat(cashPaymentVoucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherRepository.findAll().size();
        // set the field null
        cashPaymentVoucher.setPaymentAmount(null);

        // Create the CashPaymentVoucher, which fails.

        restCashPaymentVoucherMockMvc.perform(post("/api/cash-payment-vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucher)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucher> cashPaymentVoucherList = cashPaymentVoucherRepository.findAll();
        assertThat(cashPaymentVoucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVouchers() throws Exception {
        // Initialize the database
        cashPaymentVoucherRepository.saveAndFlush(cashPaymentVoucher);

        // Get all the cashPaymentVoucherList
        restCashPaymentVoucherMockMvc.perform(get("/api/cash-payment-vouchers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashPaymentVoucher.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].paymentRefNumber").value(hasItem(DEFAULT_PAYMENT_REF_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getCashPaymentVoucher() throws Exception {
        // Initialize the database
        cashPaymentVoucherRepository.saveAndFlush(cashPaymentVoucher);

        // Get the cashPaymentVoucher
        restCashPaymentVoucherMockMvc.perform(get("/api/cash-payment-vouchers/{id}", cashPaymentVoucher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashPaymentVoucher.getId().intValue()))
            .andExpect(jsonPath("$.paymentNumber").value(DEFAULT_PAYMENT_NUMBER.toString()))
            .andExpect(jsonPath("$.paymentRefNumber").value(DEFAULT_PAYMENT_REF_NUMBER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCashPaymentVoucher() throws Exception {
        // Get the cashPaymentVoucher
        restCashPaymentVoucherMockMvc.perform(get("/api/cash-payment-vouchers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashPaymentVoucher() throws Exception {
        // Initialize the database
        cashPaymentVoucherService.save(cashPaymentVoucher);

        int databaseSizeBeforeUpdate = cashPaymentVoucherRepository.findAll().size();

        // Update the cashPaymentVoucher
        CashPaymentVoucher updatedCashPaymentVoucher = cashPaymentVoucherRepository.findOne(cashPaymentVoucher.getId());
        updatedCashPaymentVoucher
            .paymentNumber(UPDATED_PAYMENT_NUMBER)
            .paymentRefNumber(UPDATED_PAYMENT_REF_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT);

        restCashPaymentVoucherMockMvc.perform(put("/api/cash-payment-vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashPaymentVoucher)))
            .andExpect(status().isOk());

        // Validate the CashPaymentVoucher in the database
        List<CashPaymentVoucher> cashPaymentVoucherList = cashPaymentVoucherRepository.findAll();
        assertThat(cashPaymentVoucherList).hasSize(databaseSizeBeforeUpdate);
        CashPaymentVoucher testCashPaymentVoucher = cashPaymentVoucherList.get(cashPaymentVoucherList.size() - 1);
        assertThat(testCashPaymentVoucher.getPaymentNumber()).isEqualTo(UPDATED_PAYMENT_NUMBER);
        assertThat(testCashPaymentVoucher.getPaymentRefNumber()).isEqualTo(UPDATED_PAYMENT_REF_NUMBER);
        assertThat(testCashPaymentVoucher.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCashPaymentVoucher.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testCashPaymentVoucher.getPaymentAmount()).isEqualTo(UPDATED_PAYMENT_AMOUNT);

        // Validate the CashPaymentVoucher in Elasticsearch
        CashPaymentVoucher cashPaymentVoucherEs = cashPaymentVoucherSearchRepository.findOne(testCashPaymentVoucher.getId());
        assertThat(cashPaymentVoucherEs).isEqualToComparingFieldByField(testCashPaymentVoucher);
    }

    @Test
    @Transactional
    public void updateNonExistingCashPaymentVoucher() throws Exception {
        int databaseSizeBeforeUpdate = cashPaymentVoucherRepository.findAll().size();

        // Create the CashPaymentVoucher

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCashPaymentVoucherMockMvc.perform(put("/api/cash-payment-vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucher)))
            .andExpect(status().isCreated());

        // Validate the CashPaymentVoucher in the database
        List<CashPaymentVoucher> cashPaymentVoucherList = cashPaymentVoucherRepository.findAll();
        assertThat(cashPaymentVoucherList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCashPaymentVoucher() throws Exception {
        // Initialize the database
        cashPaymentVoucherService.save(cashPaymentVoucher);

        int databaseSizeBeforeDelete = cashPaymentVoucherRepository.findAll().size();

        // Get the cashPaymentVoucher
        restCashPaymentVoucherMockMvc.perform(delete("/api/cash-payment-vouchers/{id}", cashPaymentVoucher.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cashPaymentVoucherExistsInEs = cashPaymentVoucherSearchRepository.exists(cashPaymentVoucher.getId());
        assertThat(cashPaymentVoucherExistsInEs).isFalse();

        // Validate the database is empty
        List<CashPaymentVoucher> cashPaymentVoucherList = cashPaymentVoucherRepository.findAll();
        assertThat(cashPaymentVoucherList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCashPaymentVoucher() throws Exception {
        // Initialize the database
        cashPaymentVoucherService.save(cashPaymentVoucher);

        // Search the cashPaymentVoucher
        restCashPaymentVoucherMockMvc.perform(get("/api/_search/cash-payment-vouchers?query=id:" + cashPaymentVoucher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashPaymentVoucher.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].paymentRefNumber").value(hasItem(DEFAULT_PAYMENT_REF_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashPaymentVoucher.class);
        CashPaymentVoucher cashPaymentVoucher1 = new CashPaymentVoucher();
        cashPaymentVoucher1.setId(1L);
        CashPaymentVoucher cashPaymentVoucher2 = new CashPaymentVoucher();
        cashPaymentVoucher2.setId(cashPaymentVoucher1.getId());
        assertThat(cashPaymentVoucher1).isEqualTo(cashPaymentVoucher2);
        cashPaymentVoucher2.setId(2L);
        assertThat(cashPaymentVoucher1).isNotEqualTo(cashPaymentVoucher2);
        cashPaymentVoucher1.setId(null);
        assertThat(cashPaymentVoucher1).isNotEqualTo(cashPaymentVoucher2);
    }
}
