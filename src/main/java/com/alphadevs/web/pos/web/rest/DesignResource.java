package com.alphadevs.web.pos.web.rest;

import com.alphadevs.web.pos.domain.Product;
import com.codahale.metrics.annotation.Timed;
import com.alphadevs.web.pos.domain.Design;
import com.alphadevs.web.pos.service.DesignService;
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
 * REST controller for managing Design.
 */
@RestController
@RequestMapping("/api")
public class DesignResource {

    private final Logger log = LoggerFactory.getLogger(DesignResource.class);

    private static final String ENTITY_NAME = "design";

    private final DesignService designService;

    public DesignResource(DesignService designService) {
        this.designService = designService;
    }

    /**
     * POST  /designs : Create a new design.
     *
     * @param design the design to create
     * @return the ResponseEntity with status 201 (Created) and with body the new design, or with status 400 (Bad Request) if the design has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/designs")
    @Timed
    public ResponseEntity<Design> createDesign(@Valid @RequestBody Design design) throws URISyntaxException {
        log.debug("REST request to save Design : {}", design);
        if (design.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new design cannot already have an ID")).body(null);
        }
        Design result = designService.save(design);
        return ResponseEntity.created(new URI("/api/designs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /designs : Updates an existing design.
     *
     * @param design the design to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated design,
     * or with status 400 (Bad Request) if the design is not valid,
     * or with status 500 (Internal Server Error) if the design couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/designs")
    @Timed
    public ResponseEntity<Design> updateDesign(@Valid @RequestBody Design design) throws URISyntaxException {
        log.debug("REST request to update Design : {}", design);
        if (design.getId() == null) {
            return createDesign(design);
        }
        Design result = designService.save(design);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, design.getId().toString()))
            .body(result);
    }

    /**
     * GET  /designs : get all the designs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of designs in body
     */
    @GetMapping("/designs")
    @Timed
    public ResponseEntity<List<Design>> getAllDesigns(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Designs");
        Page<Design> page = designService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/designs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /designs/product/:product : get all the designs.
     *
     * @param pageable the pagination information
     * @param product the Product
     * @return the ResponseEntity with status 200 (OK) and the list of designs in body
     */
    @GetMapping("/designs/product")
    @Timed
    public ResponseEntity<List<Design>> geDesignsByProduct(@ApiParam Pageable pageable,Product product) {
        log.debug("REST request to get a page of Designs by Products");
        Page<Design> page = designService.findAllByRelatedProduct(pageable,product);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/designs/product");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /designs/:id : get the "id" design.
     *
     * @param id the id of the design to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the design, or with status 404 (Not Found)
     */
    @GetMapping("/designs/{id}")
    @Timed
    public ResponseEntity<Design> getDesign(@PathVariable Long id) {
        log.debug("REST request to get Design : {}", id);
        Design design = designService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(design));
    }

    /**
     * DELETE  /designs/:id : delete the "id" design.
     *
     * @param id the id of the design to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/designs/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesign(@PathVariable Long id) {
        log.debug("REST request to delete Design : {}", id);
        designService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/designs?query=:query : search for the design corresponding
     * to the query.
     *
     * @param query the query of the design search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/designs")
    @Timed
    public ResponseEntity<List<Design>> searchDesigns(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Designs for query {}", query);
        Page<Design> page = designService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/designs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
