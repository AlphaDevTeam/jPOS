package com.alphadevs.web.pos.web.rest;

import com.alphadevs.web.pos.JPosApp;

import com.alphadevs.web.pos.domain.Customer;
import com.alphadevs.web.pos.domain.CustomerCategory;
import com.alphadevs.web.pos.domain.Address;
import com.alphadevs.web.pos.domain.ContactInfo;
import com.alphadevs.web.pos.repository.CustomerRepository;
import com.alphadevs.web.pos.service.CustomerService;
import com.alphadevs.web.pos.repository.search.CustomerSearchRepository;
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
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JPosApp.class)
public class CustomerResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CUST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CUST_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NIC = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NIC = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CREDIT_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_LIMIT = new BigDecimal(2);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_VAT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_VAT_NUMBER = "BBBBBBBBBB";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerSearchRepository customerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerResource customerResource = new CustomerResource(customerService);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
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
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .custCode(DEFAULT_CUST_CODE)
            .customerNIC(DEFAULT_CUSTOMER_NIC)
            .creditLimit(DEFAULT_CREDIT_LIMIT)
            .isActive(DEFAULT_IS_ACTIVE)
            .vatNumber(DEFAULT_VAT_NUMBER);
        // Add required entity
        CustomerCategory customerCategory = CustomerCategoryResourceIntTest.createEntity(em);
        em.persist(customerCategory);
        em.flush();
        customer.setCustomerCategory(customerCategory);
        // Add required entity
        Address address = AddressResourceIntTest.createEntity(em);
        em.persist(address);
        em.flush();
        customer.setAddress(address);
        // Add required entity
        ContactInfo contactInfo = ContactInfoResourceIntTest.createEntity(em);
        em.persist(contactInfo);
        em.flush();
        customer.setContactInfo(contactInfo);
        return customer;
    }

    @Before
    public void initTest() {
        customerSearchRepository.deleteAll();
        customer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCustomer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCustomer.getCustCode()).isEqualTo(DEFAULT_CUST_CODE);
        assertThat(testCustomer.getCustomerNIC()).isEqualTo(DEFAULT_CUSTOMER_NIC);
        assertThat(testCustomer.getCreditLimit()).isEqualTo(DEFAULT_CREDIT_LIMIT);
        assertThat(testCustomer.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testCustomer.getVatNumber()).isEqualTo(DEFAULT_VAT_NUMBER);

        // Validate the Customer in Elasticsearch
        Customer customerEs = customerSearchRepository.findOne(testCustomer.getId());
        assertThat(customerEs).isEqualToComparingFieldByField(testCustomer);
    }

    @Test
    @Transactional
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setFirstName(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setLastName(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].custCode").value(hasItem(DEFAULT_CUST_CODE.toString())))
            .andExpect(jsonPath("$.[*].customerNIC").value(hasItem(DEFAULT_CUSTOMER_NIC.toString())))
            .andExpect(jsonPath("$.[*].creditLimit").value(hasItem(DEFAULT_CREDIT_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].vatNumber").value(hasItem(DEFAULT_VAT_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.custCode").value(DEFAULT_CUST_CODE.toString()))
            .andExpect(jsonPath("$.customerNIC").value(DEFAULT_CUSTOMER_NIC.toString()))
            .andExpect(jsonPath("$.creditLimit").value(DEFAULT_CREDIT_LIMIT.intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.vatNumber").value(DEFAULT_VAT_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findOne(customer.getId());
        updatedCustomer
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .custCode(UPDATED_CUST_CODE)
            .customerNIC(UPDATED_CUSTOMER_NIC)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .isActive(UPDATED_IS_ACTIVE)
            .vatNumber(UPDATED_VAT_NUMBER);

        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomer)))
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomer.getCustCode()).isEqualTo(UPDATED_CUST_CODE);
        assertThat(testCustomer.getCustomerNIC()).isEqualTo(UPDATED_CUSTOMER_NIC);
        assertThat(testCustomer.getCreditLimit()).isEqualTo(UPDATED_CREDIT_LIMIT);
        assertThat(testCustomer.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCustomer.getVatNumber()).isEqualTo(UPDATED_VAT_NUMBER);

        // Validate the Customer in Elasticsearch
        Customer customerEs = customerSearchRepository.findOne(testCustomer.getId());
        assertThat(customerEs).isEqualToComparingFieldByField(testCustomer);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Create the Customer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean customerExistsInEs = customerSearchRepository.exists(customer.getId());
        assertThat(customerExistsInEs).isFalse();

        // Validate the database is empty
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        // Search the customer
        restCustomerMockMvc.perform(get("/api/_search/customers?query=id:" + customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].custCode").value(hasItem(DEFAULT_CUST_CODE.toString())))
            .andExpect(jsonPath("$.[*].customerNIC").value(hasItem(DEFAULT_CUSTOMER_NIC.toString())))
            .andExpect(jsonPath("$.[*].creditLimit").value(hasItem(DEFAULT_CREDIT_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].vatNumber").value(hasItem(DEFAULT_VAT_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = new Customer();
        customer1.setId(1L);
        Customer customer2 = new Customer();
        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);
        customer2.setId(2L);
        assertThat(customer1).isNotEqualTo(customer2);
        customer1.setId(null);
        assertThat(customer1).isNotEqualTo(customer2);
    }
}
