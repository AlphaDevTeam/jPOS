package com.alphadevs.web.pos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alphadevs.web.pos.domain.CashPaymentVoucher;
import com.alphadevs.web.pos.service.CashPaymentVoucherService;
import com.alphadevs.web.pos.web.rest.util.HeaderUtil;
import com.alphadevs.web.pos.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CashPaymentVoucher.
 */
@RestController
@RequestMapping("/api")
public class CashPaymentVoucherResource {

    private final Logger log = LoggerFactory.getLogger(CashPaymentVoucherResource.class);

    private static final String ENTITY_NAME = "cashPaymentVoucher";
        
    private final CashPaymentVoucherService cashPaymentVoucherService;

    public CashPaymentVoucherResource(CashPaymentVoucherService cashPaymentVoucherService) {
        this.cashPaymentVoucherService = cashPaymentVoucherService;
    }

    /**
     * POST  /cash-payment-vouchers : Create a new cashPaymentVoucher.
     *
     * @param cashPaymentVoucher the cashPaymentVoucher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cashPaymentVoucher, or with status 400 (Bad Request) if the cashPaymentVoucher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cash-payment-vouchers")
    @Timed
    public ResponseEntity<CashPaymentVoucher> createCashPaymentVoucher(@Valid @RequestBody CashPaymentVoucher cashPaymentVoucher) throws URISyntaxException {
        log.debug("REST request to save CashPaymentVoucher : {}", cashPaymentVoucher);
        if (cashPaymentVoucher.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cashPaymentVoucher cannot already have an ID")).body(null);
        }
        CashPaymentVoucher result = cashPaymentVoucherService.save(cashPaymentVoucher);
        return ResponseEntity.created(new URI("/api/cash-payment-vouchers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cash-payment-vouchers : Updates an existing cashPaymentVoucher.
     *
     * @param cashPaymentVoucher the cashPaymentVoucher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cashPaymentVoucher,
     * or with status 400 (Bad Request) if the cashPaymentVoucher is not valid,
     * or with status 500 (Internal Server Error) if the cashPaymentVoucher couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cash-payment-vouchers")
    @Timed
    public ResponseEntity<CashPaymentVoucher> updateCashPaymentVoucher(@Valid @RequestBody CashPaymentVoucher cashPaymentVoucher) throws URISyntaxException {
        log.debug("REST request to update CashPaymentVoucher : {}", cashPaymentVoucher);
        if (cashPaymentVoucher.getId() == null) {
            return createCashPaymentVoucher(cashPaymentVoucher);
        }
        CashPaymentVoucher result = cashPaymentVoucherService.save(cashPaymentVoucher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cashPaymentVoucher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cash-payment-vouchers : get all the cashPaymentVouchers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cashPaymentVouchers in body
     */
    @GetMapping("/cash-payment-vouchers")
    @Timed
    public ResponseEntity<List<CashPaymentVoucher>> getAllCashPaymentVouchers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CashPaymentVouchers");
        Page<CashPaymentVoucher> page = cashPaymentVoucherService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cash-payment-vouchers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cash-payment-vouchers/:id : get the "id" cashPaymentVoucher.
     *
     * @param id the id of the cashPaymentVoucher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cashPaymentVoucher, or with status 404 (Not Found)
     */
    @GetMapping("/cash-payment-vouchers/{id}")
    @Timed
    public ResponseEntity<CashPaymentVoucher> getCashPaymentVoucher(@PathVariable Long id) {
        log.debug("REST request to get CashPaymentVoucher : {}", id);
        CashPaymentVoucher cashPaymentVoucher = cashPaymentVoucherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cashPaymentVoucher));
    }

    /**
     * DELETE  /cash-payment-vouchers/:id : delete the "id" cashPaymentVoucher.
     *
     * @param id the id of the cashPaymentVoucher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cash-payment-vouchers/{id}")
    @Timed
    public ResponseEntity<Void> deleteCashPaymentVoucher(@PathVariable Long id) {
        log.debug("REST request to delete CashPaymentVoucher : {}", id);
        cashPaymentVoucherService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cash-payment-vouchers?query=:query : search for the cashPaymentVoucher corresponding
     * to the query.
     *
     * @param query the query of the cashPaymentVoucher search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cash-payment-vouchers")
    @Timed
    public ResponseEntity<List<CashPaymentVoucher>> searchCashPaymentVouchers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CashPaymentVouchers for query {}", query);
        Page<CashPaymentVoucher> page = cashPaymentVoucherService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cash-payment-vouchers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
