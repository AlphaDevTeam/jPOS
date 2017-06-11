package com.alphadevs.web.pos.web.rest;

import com.alphadevs.web.pos.JPosApp;

import com.alphadevs.web.pos.domain.Item;
import com.alphadevs.web.pos.domain.Product;
import com.alphadevs.web.pos.domain.Design;
import com.alphadevs.web.pos.domain.Location;
import com.alphadevs.web.pos.repository.ItemRepository;
import com.alphadevs.web.pos.service.ItemService;
import com.alphadevs.web.pos.repository.search.ItemSearchRepository;
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
 * Test class for the ItemResource REST controller.
 *
 * @see ItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JPosApp.class)
public class ItemResourceIntTest {

    private static final String DEFAULT_ITEM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ITEM_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_ITEM_COST = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ITEM_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ITEM_UNIT_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ITEM_REORDER_LEVEL = new BigDecimal(1);
    private static final BigDecimal UPDATED_ITEM_REORDER_LEVEL = new BigDecimal(2);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemSearchRepository itemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItemMockMvc;

    private Item item;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemResource itemResource = new ItemResource(itemService, stockService);
        this.restItemMockMvc = MockMvcBuilders.standaloneSetup(itemResource)
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
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
            .itemCode(DEFAULT_ITEM_CODE)
            .itemTitle(DEFAULT_ITEM_TITLE)
            .itemBarcode(DEFAULT_ITEM_BARCODE)
            .itemDescription(DEFAULT_ITEM_DESCRIPTION)
            .itemCost(DEFAULT_ITEM_COST)
            .itemUnitPrice(DEFAULT_ITEM_UNIT_PRICE)
            .itemReorderLevel(DEFAULT_ITEM_REORDER_LEVEL);
        // Add required entity
        Product relatedProduct = ProductResourceIntTest.createEntity(em);
        em.persist(relatedProduct);
        em.flush();
        item.setRelatedProduct(relatedProduct);
        // Add required entity
        Design relatedDesign = DesignResourceIntTest.createEntity(em);
        em.persist(relatedDesign);
        em.flush();
        item.setRelatedDesign(relatedDesign);
        // Add required entity
        Location itemLocation = LocationResourceIntTest.createEntity(em);
        em.persist(itemLocation);
        em.flush();
        item.setItemLocation(itemLocation);
        return item;
    }

    @Before
    public void initTest() {
        itemSearchRepository.deleteAll();
        item = createEntity(em);
    }

    @Test
    @Transactional
    public void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // Create the Item
        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getItemCode()).isEqualTo(DEFAULT_ITEM_CODE);
        assertThat(testItem.getItemTitle()).isEqualTo(DEFAULT_ITEM_TITLE);
        assertThat(testItem.getItemBarcode()).isEqualTo(DEFAULT_ITEM_BARCODE);
        assertThat(testItem.getItemDescription()).isEqualTo(DEFAULT_ITEM_DESCRIPTION);
        assertThat(testItem.getItemCost()).isEqualTo(DEFAULT_ITEM_COST);
        assertThat(testItem.getItemUnitPrice()).isEqualTo(DEFAULT_ITEM_UNIT_PRICE);
        assertThat(testItem.getItemReorderLevel()).isEqualTo(DEFAULT_ITEM_REORDER_LEVEL);

        // Validate the Item in Elasticsearch
        Item itemEs = itemSearchRepository.findOne(testItem.getId());
        assertThat(itemEs).isEqualToComparingFieldByField(testItem);
    }

    @Test
    @Transactional
    public void createItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // Create the Item with an existing ID
        item.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkItemCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setItemCode(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setItemTitle(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setItemDescription(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setItemCost(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setItemUnitPrice(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc.perform(get("/api/items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE.toString())))
            .andExpect(jsonPath("$.[*].itemTitle").value(hasItem(DEFAULT_ITEM_TITLE.toString())))
            .andExpect(jsonPath("$.[*].itemBarcode").value(hasItem(DEFAULT_ITEM_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].itemDescription").value(hasItem(DEFAULT_ITEM_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].itemCost").value(hasItem(DEFAULT_ITEM_COST.intValue())))
            .andExpect(jsonPath("$.[*].itemUnitPrice").value(hasItem(DEFAULT_ITEM_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].itemReorderLevel").value(hasItem(DEFAULT_ITEM_REORDER_LEVEL.intValue())));
    }

    @Test
    @Transactional
    public void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.itemCode").value(DEFAULT_ITEM_CODE.toString()))
            .andExpect(jsonPath("$.itemTitle").value(DEFAULT_ITEM_TITLE.toString()))
            .andExpect(jsonPath("$.itemBarcode").value(DEFAULT_ITEM_BARCODE.toString()))
            .andExpect(jsonPath("$.itemDescription").value(DEFAULT_ITEM_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.itemCost").value(DEFAULT_ITEM_COST.intValue()))
            .andExpect(jsonPath("$.itemUnitPrice").value(DEFAULT_ITEM_UNIT_PRICE.intValue()))
            .andExpect(jsonPath("$.itemReorderLevel").value(DEFAULT_ITEM_REORDER_LEVEL.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItem() throws Exception {
        // Initialize the database
        itemService.save(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findOne(item.getId());
        updatedItem
            .itemCode(UPDATED_ITEM_CODE)
            .itemTitle(UPDATED_ITEM_TITLE)
            .itemBarcode(UPDATED_ITEM_BARCODE)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .itemCost(UPDATED_ITEM_COST)
            .itemUnitPrice(UPDATED_ITEM_UNIT_PRICE)
            .itemReorderLevel(UPDATED_ITEM_REORDER_LEVEL);

        restItemMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItem)))
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getItemCode()).isEqualTo(UPDATED_ITEM_CODE);
        assertThat(testItem.getItemTitle()).isEqualTo(UPDATED_ITEM_TITLE);
        assertThat(testItem.getItemBarcode()).isEqualTo(UPDATED_ITEM_BARCODE);
        assertThat(testItem.getItemDescription()).isEqualTo(UPDATED_ITEM_DESCRIPTION);
        assertThat(testItem.getItemCost()).isEqualTo(UPDATED_ITEM_COST);
        assertThat(testItem.getItemUnitPrice()).isEqualTo(UPDATED_ITEM_UNIT_PRICE);
        assertThat(testItem.getItemReorderLevel()).isEqualTo(UPDATED_ITEM_REORDER_LEVEL);

        // Validate the Item in Elasticsearch
        Item itemEs = itemSearchRepository.findOne(testItem.getId());
        assertThat(itemEs).isEqualToComparingFieldByField(testItem);
    }

    @Test
    @Transactional
    public void updateNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Create the Item

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restItemMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteItem() throws Exception {
        // Initialize the database
        itemService.save(item);

        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Get the item
        restItemMockMvc.perform(delete("/api/items/{id}", item.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean itemExistsInEs = itemSearchRepository.exists(item.getId());
        assertThat(itemExistsInEs).isFalse();

        // Validate the database is empty
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchItem() throws Exception {
        // Initialize the database
        itemService.save(item);

        // Search the item
        restItemMockMvc.perform(get("/api/_search/items?query=id:" + item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE.toString())))
            .andExpect(jsonPath("$.[*].itemTitle").value(hasItem(DEFAULT_ITEM_TITLE.toString())))
            .andExpect(jsonPath("$.[*].itemBarcode").value(hasItem(DEFAULT_ITEM_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].itemDescription").value(hasItem(DEFAULT_ITEM_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].itemCost").value(hasItem(DEFAULT_ITEM_COST.intValue())))
            .andExpect(jsonPath("$.[*].itemUnitPrice").value(hasItem(DEFAULT_ITEM_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].itemReorderLevel").value(hasItem(DEFAULT_ITEM_REORDER_LEVEL.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Item.class);
        Item item1 = new Item();
        item1.setId(1L);
        Item item2 = new Item();
        item2.setId(item1.getId());
        assertThat(item1).isEqualTo(item2);
        item2.setId(2L);
        assertThat(item1).isNotEqualTo(item2);
        item1.setId(null);
        assertThat(item1).isNotEqualTo(item2);
    }
}
