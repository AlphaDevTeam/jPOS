package com.alphadevs.web.pos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alphadevs.web.pos.domain.CashBookBalance;
import com.alphadevs.web.pos.service.CashBookBalanceService;
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
 * REST controller for managing CashBookBalance.
 */
@RestController
@RequestMapping("/api")
public class CashBookBalanceResource {

    private final Logger log = LoggerFactory.getLogger(CashBookBalanceResource.class);

    private static final String ENTITY_NAME = "cashBookBalance";
        
    private final CashBookBalanceService cashBookBalanceService;

    public CashBookBalanceResource(CashBookBalanceService cashBookBalanceService) {
        this.cashBookBalanceService = cashBookBalanceService;
    }

    /**
     * POST  /cash-book-balances : Create a new cashBookBalance.
     *
     * @param cashBookBalance the cashBookBalance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cashBookBalance, or with status 400 (Bad Request) if the cashBookBalance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cash-book-balances")
    @Timed
    public ResponseEntity<CashBookBalance> createCashBookBalance(@Valid @RequestBody CashBookBalance cashBookBalance) throws URISyntaxException {
        log.debug("REST request to save CashBookBalance : {}", cashBookBalance);
        if (cashBookBalance.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cashBookBalance cannot already have an ID")).body(null);
        }
        CashBookBalance result = cashBookBalanceService.save(cashBookBalance);
        return ResponseEntity.created(new URI("/api/cash-book-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cash-book-balances : Updates an existing cashBookBalance.
     *
     * @param cashBookBalance the cashBookBalance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cashBookBalance,
     * or with status 400 (Bad Request) if the cashBookBalance is not valid,
     * or with status 500 (Internal Server Error) if the cashBookBalance couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cash-book-balances")
    @Timed
    public ResponseEntity<CashBookBalance> updateCashBookBalance(@Valid @RequestBody CashBookBalance cashBookBalance) throws URISyntaxException {
        log.debug("REST request to update CashBookBalance : {}", cashBookBalance);
        if (cashBookBalance.getId() == null) {
            return createCashBookBalance(cashBookBalance);
        }
        CashBookBalance result = cashBookBalanceService.save(cashBookBalance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cashBookBalance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cash-book-balances : get all the cashBookBalances.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cashBookBalances in body
     */
    @GetMapping("/cash-book-balances")
    @Timed
    public ResponseEntity<List<CashBookBalance>> getAllCashBookBalances(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CashBookBalances");
        Page<CashBookBalance> page = cashBookBalanceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cash-book-balances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cash-book-balances/:id : get the "id" cashBookBalance.
     *
     * @param id the id of the cashBookBalance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cashBookBalance, or with status 404 (Not Found)
     */
    @GetMapping("/cash-book-balances/{id}")
    @Timed
    public ResponseEntity<CashBookBalance> getCashBookBalance(@PathVariable Long id) {
        log.debug("REST request to get CashBookBalance : {}", id);
        CashBookBalance cashBookBalance = cashBookBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cashBookBalance));
    }

    /**
     * DELETE  /cash-book-balances/:id : delete the "id" cashBookBalance.
     *
     * @param id the id of the cashBookBalance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cash-book-balances/{id}")
    @Timed
    public ResponseEntity<Void> deleteCashBookBalance(@PathVariable Long id) {
        log.debug("REST request to delete CashBookBalance : {}", id);
        cashBookBalanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cash-book-balances?query=:query : search for the cashBookBalance corresponding
     * to the query.
     *
     * @param query the query of the cashBookBalance search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cash-book-balances")
    @Timed
    public ResponseEntity<List<CashBookBalance>> searchCashBookBalances(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CashBookBalances for query {}", query);
        Page<CashBookBalance> page = cashBookBalanceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cash-book-balances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
