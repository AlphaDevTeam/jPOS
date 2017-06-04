package com.alphadevs.web.pos.service;

import com.alphadevs.web.pos.domain.Design;
import com.alphadevs.web.pos.domain.Product;
import com.alphadevs.web.pos.repository.DesignRepository;
import com.alphadevs.web.pos.repository.search.DesignSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Design.
 */
@Service
@Transactional
public class DesignService {

    private final Logger log = LoggerFactory.getLogger(DesignService.class);

    private final DesignRepository designRepository;

    private final DesignSearchRepository designSearchRepository;

    public DesignService(DesignRepository designRepository, DesignSearchRepository designSearchRepository) {
        this.designRepository = designRepository;
        this.designSearchRepository = designSearchRepository;
    }

    /**
     * Save a design.
     *
     * @param design the entity to save
     * @return the persisted entity
     */
    public Design save(Design design) {
        log.debug("Request to save Design : {}", design);
        Design result = designRepository.save(design);
        designSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the designs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Design> findAll(Pageable pageable) {
        log.debug("Request to get all Designs");
        Page<Design> result = designRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one design by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Design findOne(Long id) {
        log.debug("Request to get Design : {}", id);
        Design design = designRepository.findOne(id);
        return design;
    }

    /**
     *  Delete the  design by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Design : {}", id);
        designRepository.delete(id);
        designSearchRepository.delete(id);
    }

    /**
     * Search for the design corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Design> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Designs for query {}", query);
        Page<Design> result = designSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    /**
     *  Get all the designs by Product.
     *
     *  @param pageable the pagination information
     *  @param product the Product
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Design> findAllByRelatedProduct(Pageable pageable, Product product) {
        log.debug("Request to get all Designs by Product");
        Page<Design> result = designRepository.findAllByRelatedProduct(pageable,product);
        return result;
    }

}
