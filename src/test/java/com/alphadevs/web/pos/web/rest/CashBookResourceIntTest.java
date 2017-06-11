package com.alphadevs.web.pos.web.rest;

import com.alphadevs.web.pos.JPosApp;

import com.alphadevs.web.pos.domain.CashBook;
import com.alphadevs.web.pos.repository.CashBookRepository;
import com.alphadevs.web.pos.service.CashBookService;
import com.alphadevs.web.pos.repository.search.CashBookSearchRepository;
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
 * Test class for the CashBookResource REST controller.
 *
 * @see CashBookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JPosApp.class)
public class CashBookResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CR_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CR_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DR_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DR_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BALANCE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE_AMOUNT = new BigDecimal(2);

    private static final LocalDate DEFAULT_RELATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RELATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CashBookRepository cashBookRepository;

    @Autowired
    private CashBookService cashBookService;

    @Autowired
    private CashBookSearchRepository cashBookSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCashBookMockMvc;

    private CashBook cashBook;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CashBookResource cashBookResource = new CashBookResource(cashBookService);
        this.restCashBookMockMvc = MockMvcBuilders.standaloneSetup(cashBookResource)
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
    public static CashBook createEntity(EntityManager em) {
        CashBook cashBook = new CashBook()
            .description(DEFAULT_DESCRIPTION)
            .crAmount(DEFAULT_CR_AMOUNT)
            .drAmount(DEFAULT_DR_AMOUNT)
            .balanceAmount(DEFAULT_BALANCE_AMOUNT)
            .relatedDate(DEFAULT_RELATED_DATE);
        return cashBook;
    }

    @Before
    public void initTest() {
        cashBookSearchRepository.deleteAll();
        cashBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashBook() throws Exception {
        int databaseSizeBeforeCreate = cashBookRepository.findAll().size();

        // Create the CashBook
        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isCreated());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeCreate + 1);
        CashBook testCashBook = cashBookList.get(cashBookList.size() - 1);
        assertThat(testCashBook.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCashBook.getCrAmount()).isEqualTo(DEFAULT_CR_AMOUNT);
        assertThat(testCashBook.getDrAmount()).isEqualTo(DEFAULT_DR_AMOUNT);
        assertThat(testCashBook.getBalanceAmount()).isEqualTo(DEFAULT_BALANCE_AMOUNT);
        assertThat(testCashBook.getRelatedDate()).isEqualTo(DEFAULT_RELATED_DATE);

        // Validate the CashBook in Elasticsearch
        CashBook cashBookEs = cashBookSearchRepository.findOne(testCashBook.getId());
        assertThat(cashBookEs).isEqualToComparingFieldByField(testCashBook);
    }

    @Test
    @Transactional
    public void createCashBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashBookRepository.findAll().size();

        // Create the CashBook with an existing ID
        cashBook.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setDescription(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCrAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setCrAmount(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDrAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setDrAmount(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBalanceAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setBalanceAmount(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRelatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setRelatedDate(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCashBooks() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList
        restCashBookMockMvc.perform(get("/api/cash-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].crAmount").value(hasItem(DEFAULT_CR_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].drAmount").value(hasItem(DEFAULT_DR_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].balanceAmount").value(hasItem(DEFAULT_BALANCE_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].relatedDate").value(hasItem(DEFAULT_RELATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getCashBook() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get the cashBook
        restCashBookMockMvc.perform(get("/api/cash-books/{id}", cashBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashBook.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.crAmount").value(DEFAULT_CR_AMOUNT.intValue()))
            .andExpect(jsonPath("$.drAmount").value(DEFAULT_DR_AMOUNT.intValue()))
            .andExpect(jsonPath("$.balanceAmount").value(DEFAULT_BALANCE_AMOUNT.intValue()))
            .andExpect(jsonPath("$.relatedDate").value(DEFAULT_RELATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCashBook() throws Exception {
        // Get the cashBook
        restCashBookMockMvc.perform(get("/api/cash-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashBook() throws Exception {
        // Initialize the database
        cashBookService.save(cashBook);

        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();

        // Update the cashBook
        CashBook updatedCashBook = cashBookRepository.findOne(cashBook.getId());
        updatedCashBook
            .description(UPDATED_DESCRIPTION)
            .crAmount(UPDATED_CR_AMOUNT)
            .drAmount(UPDATED_DR_AMOUNT)
            .balanceAmount(UPDATED_BALANCE_AMOUNT)
            .relatedDate(UPDATED_RELATED_DATE);

        restCashBookMockMvc.perform(put("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashBook)))
            .andExpect(status().isOk());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
        CashBook testCashBook = cashBookList.get(cashBookList.size() - 1);
        assertThat(testCashBook.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCashBook.getCrAmount()).isEqualTo(UPDATED_CR_AMOUNT);
        assertThat(testCashBook.getDrAmount()).isEqualTo(UPDATED_DR_AMOUNT);
        assertThat(testCashBook.getBalanceAmount()).isEqualTo(UPDATED_BALANCE_AMOUNT);
        assertThat(testCashBook.getRelatedDate()).isEqualTo(UPDATED_RELATED_DATE);

        // Validate the CashBook in Elasticsearch
        CashBook cashBookEs = cashBookSearchRepository.findOne(testCashBook.getId());
        assertThat(cashBookEs).isEqualToComparingFieldByField(testCashBook);
    }

    @Test
    @Transactional
    public void updateNonExistingCashBook() throws Exception {
        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();

        // Create the CashBook

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCashBookMockMvc.perform(put("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isCreated());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCashBook() throws Exception {
        // Initialize the database
        cashBookService.save(cashBook);

        int databaseSizeBeforeDelete = cashBookRepository.findAll().size();

        // Get the cashBook
        restCashBookMockMvc.perform(delete("/api/cash-books/{id}", cashBook.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cashBookExistsInEs = cashBookSearchRepository.exists(cashBook.getId());
        assertThat(cashBookExistsInEs).isFalse();

        // Validate the database is empty
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCashBook() throws Exception {
        // Initialize the database
        cashBookService.save(cashBook);

        // Search the cashBook
        restCashBookMockMvc.perform(get("/api/_search/cash-books?query=id:" + cashBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].crAmount").value(hasItem(DEFAULT_CR_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].drAmount").value(hasItem(DEFAULT_DR_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].balanceAmount").value(hasItem(DEFAULT_BALANCE_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].relatedDate").value(hasItem(DEFAULT_RELATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashBook.class);
        CashBook cashBook1 = new CashBook();
        cashBook1.setId(1L);
        CashBook cashBook2 = new CashBook();
        cashBook2.setId(cashBook1.getId());
        assertThat(cashBook1).isEqualTo(cashBook2);
        cashBook2.setId(2L);
        assertThat(cashBook1).isNotEqualTo(cashBook2);
        cashBook1.setId(null);
        assertThat(cashBook1).isNotEqualTo(cashBook2);
    }
}
