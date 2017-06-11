package com.alphadevs.web.pos.service;

import com.alphadevs.web.pos.domain.Stock;
import com.alphadevs.web.pos.repository.StockRepository;
import com.alphadevs.web.pos.repository.search.StockSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Stock.
 */
@Service
@Transactional
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);
    
    private final StockRepository stockRepository;

    private final StockSearchRepository stockSearchRepository;

    public StockService(StockRepository stockRepository, StockSearchRepository stockSearchRepository) {
        this.stockRepository = stockRepository;
        this.stockSearchRepository = stockSearchRepository;
    }

    /**
     * Save a stock.
     *
     * @param stock the entity to save
     * @return the persisted entity
     */
    public Stock save(Stock stock) {
        log.debug("Request to save Stock : {}", stock);
        Stock result = stockRepository.save(stock);
        stockSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the stocks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Stock> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        Page<Stock> result = stockRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one stock by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Stock findOne(Long id) {
        log.debug("Request to get Stock : {}", id);
        Stock stock = stockRepository.findOne(id);
        return stock;
    }

    /**
     *  Delete the  stock by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Stock : {}", id);
        stockRepository.delete(id);
        stockSearchRepository.delete(id);
    }

    /**
     * Search for the stock corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Stock> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stocks for query {}", query);
        Page<Stock> result = stockSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
