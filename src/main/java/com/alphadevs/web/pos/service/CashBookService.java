package com.alphadevs.web.pos.service;

import com.alphadevs.web.pos.domain.CashBook;
import com.alphadevs.web.pos.repository.CashBookRepository;
import com.alphadevs.web.pos.repository.search.CashBookSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CashBook.
 */
@Service
@Transactional
public class CashBookService {

    private final Logger log = LoggerFactory.getLogger(CashBookService.class);
    
    private final CashBookRepository cashBookRepository;

    private final CashBookSearchRepository cashBookSearchRepository;

    public CashBookService(CashBookRepository cashBookRepository, CashBookSearchRepository cashBookSearchRepository) {
        this.cashBookRepository = cashBookRepository;
        this.cashBookSearchRepository = cashBookSearchRepository;
    }

    /**
     * Save a cashBook.
     *
     * @param cashBook the entity to save
     * @return the persisted entity
     */
    public CashBook save(CashBook cashBook) {
        log.debug("Request to save CashBook : {}", cashBook);
        CashBook result = cashBookRepository.save(cashBook);
        cashBookSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the cashBooks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CashBook> findAll(Pageable pageable) {
        log.debug("Request to get all CashBooks");
        Page<CashBook> result = cashBookRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one cashBook by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CashBook findOne(Long id) {
        log.debug("Request to get CashBook : {}", id);
        CashBook cashBook = cashBookRepository.findOne(id);
        return cashBook;
    }

    /**
     *  Delete the  cashBook by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CashBook : {}", id);
        cashBookRepository.delete(id);
        cashBookSearchRepository.delete(id);
    }

    /**
     * Search for the cashBook corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CashBook> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CashBooks for query {}", query);
        Page<CashBook> result = cashBookSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
