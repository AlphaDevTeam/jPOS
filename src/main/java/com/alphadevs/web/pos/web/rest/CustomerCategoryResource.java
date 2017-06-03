package com.alphadevs.web.pos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alphadevs.web.pos.domain.CustomerCategory;
import com.alphadevs.web.pos.service.CustomerCategoryService;
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
 * REST controller for managing CustomerCategory.
 */
@RestController
@RequestMapping("/api")
public class CustomerCategoryResource {

    private final Logger log = LoggerFactory.getLogger(CustomerCategoryResource.class);

    private static final String ENTITY_NAME = "customerCategory";
        
    private final CustomerCategoryService customerCategoryService;

    public CustomerCategoryResource(CustomerCategoryService customerCategoryService) {
        this.customerCategoryService = customerCategoryService;
    }

    /**
     * POST  /customer-categories : Create a new customerCategory.
     *
     * @param customerCategory the customerCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerCategory, or with status 400 (Bad Request) if the customerCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer-categories")
    @Timed
    public ResponseEntity<CustomerCategory> createCustomerCategory(@Valid @RequestBody CustomerCategory customerCategory) throws URISyntaxException {
        log.debug("REST request to save CustomerCategory : {}", customerCategory);
        if (customerCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new customerCategory cannot already have an ID")).body(null);
        }
        CustomerCategory result = customerCategoryService.save(customerCategory);
        return ResponseEntity.created(new URI("/api/customer-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-categories : Updates an existing customerCategory.
     *
     * @param customerCategory the customerCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerCategory,
     * or with status 400 (Bad Request) if the customerCategory is not valid,
     * or with status 500 (Internal Server Error) if the customerCategory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customer-categories")
    @Timed
    public ResponseEntity<CustomerCategory> updateCustomerCategory(@Valid @RequestBody CustomerCategory customerCategory) throws URISyntaxException {
        log.debug("REST request to update CustomerCategory : {}", customerCategory);
        if (customerCategory.getId() == null) {
            return createCustomerCategory(customerCategory);
        }
        CustomerCategory result = customerCategoryService.save(customerCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-categories : get all the customerCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customerCategories in body
     */
    @GetMapping("/customer-categories")
    @Timed
    public ResponseEntity<List<CustomerCategory>> getAllCustomerCategories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CustomerCategories");
        Page<CustomerCategory> page = customerCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customer-categories/:id : get the "id" customerCategory.
     *
     * @param id the id of the customerCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerCategory, or with status 404 (Not Found)
     */
    @GetMapping("/customer-categories/{id}")
    @Timed
    public ResponseEntity<CustomerCategory> getCustomerCategory(@PathVariable Long id) {
        log.debug("REST request to get CustomerCategory : {}", id);
        CustomerCategory customerCategory = customerCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(customerCategory));
    }

    /**
     * DELETE  /customer-categories/:id : delete the "id" customerCategory.
     *
     * @param id the id of the customerCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customer-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomerCategory(@PathVariable Long id) {
        log.debug("REST request to delete CustomerCategory : {}", id);
        customerCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/customer-categories?query=:query : search for the customerCategory corresponding
     * to the query.
     *
     * @param query the query of the customerCategory search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/customer-categories")
    @Timed
    public ResponseEntity<List<CustomerCategory>> searchCustomerCategories(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CustomerCategories for query {}", query);
        Page<CustomerCategory> page = customerCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customer-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
