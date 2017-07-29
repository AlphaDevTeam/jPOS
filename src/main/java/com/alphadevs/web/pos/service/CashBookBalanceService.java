package com.alphadevs.web.pos.service;

import com.alphadevs.web.pos.domain.CashBookBalance;
import com.alphadevs.web.pos.repository.CashBookBalanceRepository;
import com.alphadevs.web.pos.repository.search.CashBookBalanceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CashBookBalance.
 */
@Service
@Transactional
public class CashBookBalanceService {

    private final Logger log = LoggerFactory.getLogger(CashBookBalanceService.class);
    
    private final CashBookBalanceRepository cashBookBalanceRepository;

    private final CashBookBalanceSearchRepository cashBookBalanceSearchRepository;

    public CashBookBalanceService(CashBookBalanceRepository cashBookBalanceRepository, CashBookBalanceSearchRepository cashBookBalanceSearchRepository) {
        this.cashBookBalanceRepository = cashBookBalanceRepository;
        this.cashBookBalanceSearchRepository = cashBookBalanceSearchRepository;
    }

    /**
     * Save a cashBookBalance.
     *
     * @param cashBookBalance the entity to save
     * @return the persisted entity
     */
    public CashBookBalance save(CashBookBalance cashBookBalance) {
        log.debug("Request to save CashBookBalance : {}", cashBookBalance);
        CashBookBalance result = cashBookBalanceRepository.save(cashBookBalance);
        cashBookBalanceSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the cashBookBalances.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CashBookBalance> findAll(Pageable pageable) {
        log.debug("Request to get all CashBookBalances");
        Page<CashBookBalance> result = cashBookBalanceRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one cashBookBalance by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CashBookBalance findOne(Long id) {
        log.debug("Request to get CashBookBalance : {}", id);
        CashBookBalance cashBookBalance = cashBookBalanceRepository.findOne(id);
        return cashBookBalance;
    }

    /**
     *  Delete the  cashBookBalance by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CashBookBalance : {}", id);
        cashBookBalanceRepository.delete(id);
        cashBookBalanceSearchRepository.delete(id);
    }

    /**
     * Search for the cashBookBalance corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CashBookBalance> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CashBookBalances for query {}", query);
        Page<CashBookBalance> result = cashBookBalanceSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
