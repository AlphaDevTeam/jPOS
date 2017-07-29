package com.alphadevs.web.pos.service;

import com.alphadevs.web.pos.domain.CashPaymentVoucher;
import com.alphadevs.web.pos.repository.CashPaymentVoucherRepository;
import com.alphadevs.web.pos.repository.search.CashPaymentVoucherSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CashPaymentVoucher.
 */
@Service
@Transactional
public class CashPaymentVoucherService {

    private final Logger log = LoggerFactory.getLogger(CashPaymentVoucherService.class);
    
    private final CashPaymentVoucherRepository cashPaymentVoucherRepository;

    private final CashPaymentVoucherSearchRepository cashPaymentVoucherSearchRepository;

    public CashPaymentVoucherService(CashPaymentVoucherRepository cashPaymentVoucherRepository, CashPaymentVoucherSearchRepository cashPaymentVoucherSearchRepository) {
        this.cashPaymentVoucherRepository = cashPaymentVoucherRepository;
        this.cashPaymentVoucherSearchRepository = cashPaymentVoucherSearchRepository;
    }

    /**
     * Save a cashPaymentVoucher.
     *
     * @param cashPaymentVoucher the entity to save
     * @return the persisted entity
     */
    public CashPaymentVoucher save(CashPaymentVoucher cashPaymentVoucher) {
        log.debug("Request to save CashPaymentVoucher : {}", cashPaymentVoucher);
        CashPaymentVoucher result = cashPaymentVoucherRepository.save(cashPaymentVoucher);
        cashPaymentVoucherSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the cashPaymentVouchers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CashPaymentVoucher> findAll(Pageable pageable) {
        log.debug("Request to get all CashPaymentVouchers");
        Page<CashPaymentVoucher> result = cashPaymentVoucherRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one cashPaymentVoucher by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CashPaymentVoucher findOne(Long id) {
        log.debug("Request to get CashPaymentVoucher : {}", id);
        CashPaymentVoucher cashPaymentVoucher = cashPaymentVoucherRepository.findOne(id);
        return cashPaymentVoucher;
    }

    /**
     *  Delete the  cashPaymentVoucher by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CashPaymentVoucher : {}", id);
        cashPaymentVoucherRepository.delete(id);
        cashPaymentVoucherSearchRepository.delete(id);
    }

    /**
     * Search for the cashPaymentVoucher corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CashPaymentVoucher> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CashPaymentVouchers for query {}", query);
        Page<CashPaymentVoucher> result = cashPaymentVoucherSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
