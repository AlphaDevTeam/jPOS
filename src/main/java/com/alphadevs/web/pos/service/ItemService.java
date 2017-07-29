package com.alphadevs.web.pos.service;

import com.alphadevs.web.pos.domain.Item;
import com.alphadevs.web.pos.domain.Stock;
import com.alphadevs.web.pos.repository.ItemRepository;
import com.alphadevs.web.pos.repository.StockRepository;
import com.alphadevs.web.pos.repository.search.ItemSearchRepository;
import com.alphadevs.web.pos.repository.search.StockSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Item.
 */
@Service
@Transactional
public class ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;
    private final StockRepository stockRepository;

    private final ItemSearchRepository itemSearchRepository;
    private final StockSearchRepository stockSearchRepository;

    public ItemService(ItemRepository itemRepository, StockRepository stockRepository, ItemSearchRepository itemSearchRepository, StockSearchRepository stockSearchRepository) {
        this.itemRepository = itemRepository;
        this.stockRepository = stockRepository;
        this.itemSearchRepository = itemSearchRepository;
        this.stockSearchRepository = stockSearchRepository;
    }

    /**
     * Save a item.
     *
     * @param item the entity to save
     * @return the persisted entity
     */
    public Item save(Item item) {
        log.debug("Request to save Item : {}", item);
        Item result = itemRepository.save(item);
        itemSearchRepository.save(result);
        Stock stock = new Stock();
        stock.setStockItem(item);
        stock.setStockLocation(item.getItemLocation());
        stock.setStockQty(new BigDecimal(0));
        Stock stockResult = stockRepository.save(stock);
        stockSearchRepository.save(stockResult);
        return result;
    }

    /**
     *  Get all the items.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Item> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        Page<Item> result = itemRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one item by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Item findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        Item item = itemRepository.findOne(id);
        return item;
    }

    /**
     *  Delete the  item by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.delete(id);
        itemSearchRepository.delete(id);
    }

    /**
     * Search for the item corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Item> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Items for query {}", query);
        Page<Item> result = itemSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
