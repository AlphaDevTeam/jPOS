package com.alphadevs.web.pos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alphadevs.web.pos.domain.CashBook;
import com.alphadevs.web.pos.service.CashBookService;
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
 * REST controller for managing CashBook.
 */
@RestController
@RequestMapping("/api")
public class CashBookResource {

    private final Logger log = LoggerFactory.getLogger(CashBookResource.class);

    private static final String ENTITY_NAME = "cashBook";
        
    private final CashBookService cashBookService;

    public CashBookResource(CashBookService cashBookService) {
        this.cashBookService = cashBookService;
    }

    /**
     * POST  /cash-books : Create a new cashBook.
     *
     * @param cashBook the cashBook to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cashBook, or with status 400 (Bad Request) if the cashBook has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cash-books")
    @Timed
    public ResponseEntity<CashBook> createCashBook(@Valid @RequestBody CashBook cashBook) throws URISyntaxException {
        log.debug("REST request to save CashBook : {}", cashBook);
        if (cashBook.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cashBook cannot already have an ID")).body(null);
        }
        CashBook result = cashBookService.save(cashBook);
        return ResponseEntity.created(new URI("/api/cash-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cash-books : Updates an existing cashBook.
     *
     * @param cashBook the cashBook to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cashBook,
     * or with status 400 (Bad Request) if the cashBook is not valid,
     * or with status 500 (Internal Server Error) if the cashBook couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cash-books")
    @Timed
    public ResponseEntity<CashBook> updateCashBook(@Valid @RequestBody CashBook cashBook) throws URISyntaxException {
        log.debug("REST request to update CashBook : {}", cashBook);
        if (cashBook.getId() == null) {
            return createCashBook(cashBook);
        }
        CashBook result = cashBookService.save(cashBook);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cashBook.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cash-books : get all the cashBooks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cashBooks in body
     */
    @GetMapping("/cash-books")
    @Timed
    public ResponseEntity<List<CashBook>> getAllCashBooks(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CashBooks");
        Page<CashBook> page = cashBookService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cash-books");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cash-books/:id : get the "id" cashBook.
     *
     * @param id the id of the cashBook to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cashBook, or with status 404 (Not Found)
     */
    @GetMapping("/cash-books/{id}")
    @Timed
    public ResponseEntity<CashBook> getCashBook(@PathVariable Long id) {
        log.debug("REST request to get CashBook : {}", id);
        CashBook cashBook = cashBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cashBook));
    }

    /**
     * DELETE  /cash-books/:id : delete the "id" cashBook.
     *
     * @param id the id of the cashBook to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cash-books/{id}")
    @Timed
    public ResponseEntity<Void> deleteCashBook(@PathVariable Long id) {
        log.debug("REST request to delete CashBook : {}", id);
        cashBookService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cash-books?query=:query : search for the cashBook corresponding
     * to the query.
     *
     * @param query the query of the cashBook search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cash-books")
    @Timed
    public ResponseEntity<List<CashBook>> searchCashBooks(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CashBooks for query {}", query);
        Page<CashBook> page = cashBookService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cash-books");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
