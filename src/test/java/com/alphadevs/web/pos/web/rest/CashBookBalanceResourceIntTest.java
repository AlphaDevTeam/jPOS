package com.alphadevs.web.pos.web.rest;

import com.alphadevs.web.pos.JPosApp;

import com.alphadevs.web.pos.domain.CashBookBalance;
import com.alphadevs.web.pos.domain.Location;
import com.alphadevs.web.pos.repository.CashBookBalanceRepository;
import com.alphadevs.web.pos.service.CashBookBalanceService;
import com.alphadevs.web.pos.repository.search.CashBookBalanceSearchRepository;
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
 * Test class for the CashBookBalanceResource REST controller.
 *
 * @see CashBookBalanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JPosApp.class)
public class CashBookBalanceResourceIntTest {

    private static final BigDecimal DEFAULT_CASH_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CASH_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BANK_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BANK_BALANCE = new BigDecimal(2);

    @Autowired
    private CashBookBalanceRepository cashBookBalanceRepository;

    @Autowired
    private CashBookBalanceService cashBookBalanceService;

    @Autowired
    private CashBookBalanceSearchRepository cashBookBalanceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCashBookBalanceMockMvc;

    private CashBookBalance cashBookBalance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CashBookBalanceResource cashBookBalanceResource = new CashBookBalanceResource(cashBookBalanceService);
        this.restCashBookBalanceMockMvc = MockMvcBuilders.standaloneSetup(cashBookBalanceResource)
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
    public static CashBookBalance createEntity(EntityManager em) {
        CashBookBalance cashBookBalance = new CashBookBalance()
            .cashBalance(DEFAULT_CASH_BALANCE)
            .bankBalance(DEFAULT_BANK_BALANCE);
        // Add required entity
        Location relatedLocation = LocationResourceIntTest.createEntity(em);
        em.persist(relatedLocation);
        em.flush();
        cashBookBalance.setRelatedLocation(relatedLocation);
        return cashBookBalance;
    }

    @Before
    public void initTest() {
        cashBookBalanceSearchRepository.deleteAll();
        cashBookBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashBookBalance() throws Exception {
        int databaseSizeBeforeCreate = cashBookBalanceRepository.findAll().size();

        // Create the CashBookBalance
        restCashBookBalanceMockMvc.perform(post("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isCreated());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        CashBookBalance testCashBookBalance = cashBookBalanceList.get(cashBookBalanceList.size() - 1);
        assertThat(testCashBookBalance.getCashBalance()).isEqualTo(DEFAULT_CASH_BALANCE);
        assertThat(testCashBookBalance.getBankBalance()).isEqualTo(DEFAULT_BANK_BALANCE);

        // Validate the CashBookBalance in Elasticsearch
        CashBookBalance cashBookBalanceEs = cashBookBalanceSearchRepository.findOne(testCashBookBalance.getId());
        assertThat(cashBookBalanceEs).isEqualToComparingFieldByField(testCashBookBalance);
    }

    @Test
    @Transactional
    public void createCashBookBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashBookBalanceRepository.findAll().size();

        // Create the CashBookBalance with an existing ID
        cashBookBalance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashBookBalanceMockMvc.perform(post("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCashBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookBalanceRepository.findAll().size();
        // set the field null
        cashBookBalance.setCashBalance(null);

        // Create the CashBookBalance, which fails.

        restCashBookBalanceMockMvc.perform(post("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isBadRequest());

        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBankBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookBalanceRepository.findAll().size();
        // set the field null
        cashBookBalance.setBankBalance(null);

        // Create the CashBookBalance, which fails.

        restCashBookBalanceMockMvc.perform(post("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isBadRequest());

        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCashBookBalances() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBookBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].cashBalance").value(hasItem(DEFAULT_CASH_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].bankBalance").value(hasItem(DEFAULT_BANK_BALANCE.intValue())));
    }

    @Test
    @Transactional
    public void getCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get the cashBookBalance
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances/{id}", cashBookBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashBookBalance.getId().intValue()))
            .andExpect(jsonPath("$.cashBalance").value(DEFAULT_CASH_BALANCE.intValue()))
            .andExpect(jsonPath("$.bankBalance").value(DEFAULT_BANK_BALANCE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCashBookBalance() throws Exception {
        // Get the cashBookBalance
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceService.save(cashBookBalance);

        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();

        // Update the cashBookBalance
        CashBookBalance updatedCashBookBalance = cashBookBalanceRepository.findOne(cashBookBalance.getId());
        updatedCashBookBalance
            .cashBalance(UPDATED_CASH_BALANCE)
            .bankBalance(UPDATED_BANK_BALANCE);

        restCashBookBalanceMockMvc.perform(put("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashBookBalance)))
            .andExpect(status().isOk());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
        CashBookBalance testCashBookBalance = cashBookBalanceList.get(cashBookBalanceList.size() - 1);
        assertThat(testCashBookBalance.getCashBalance()).isEqualTo(UPDATED_CASH_BALANCE);
        assertThat(testCashBookBalance.getBankBalance()).isEqualTo(UPDATED_BANK_BALANCE);

        // Validate the CashBookBalance in Elasticsearch
        CashBookBalance cashBookBalanceEs = cashBookBalanceSearchRepository.findOne(testCashBookBalance.getId());
        assertThat(cashBookBalanceEs).isEqualToComparingFieldByField(testCashBookBalance);
    }

    @Test
    @Transactional
    public void updateNonExistingCashBookBalance() throws Exception {
        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();

        // Create the CashBookBalance

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCashBookBalanceMockMvc.perform(put("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isCreated());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceService.save(cashBookBalance);

        int databaseSizeBeforeDelete = cashBookBalanceRepository.findAll().size();

        // Get the cashBookBalance
        restCashBookBalanceMockMvc.perform(delete("/api/cash-book-balances/{id}", cashBookBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cashBookBalanceExistsInEs = cashBookBalanceSearchRepository.exists(cashBookBalance.getId());
        assertThat(cashBookBalanceExistsInEs).isFalse();

        // Validate the database is empty
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceService.save(cashBookBalance);

        // Search the cashBookBalance
        restCashBookBalanceMockMvc.perform(get("/api/_search/cash-book-balances?query=id:" + cashBookBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBookBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].cashBalance").value(hasItem(DEFAULT_CASH_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].bankBalance").value(hasItem(DEFAULT_BANK_BALANCE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashBookBalance.class);
        CashBookBalance cashBookBalance1 = new CashBookBalance();
        cashBookBalance1.setId(1L);
        CashBookBalance cashBookBalance2 = new CashBookBalance();
        cashBookBalance2.setId(cashBookBalance1.getId());
        assertThat(cashBookBalance1).isEqualTo(cashBookBalance2);
        cashBookBalance2.setId(2L);
        assertThat(cashBookBalance1).isNotEqualTo(cashBookBalance2);
        cashBookBalance1.setId(null);
        assertThat(cashBookBalance1).isNotEqualTo(cashBookBalance2);
    }
}
