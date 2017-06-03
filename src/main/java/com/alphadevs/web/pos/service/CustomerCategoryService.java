package com.alphadevs.web.pos.service;

import com.alphadevs.web.pos.domain.CustomerCategory;
import com.alphadevs.web.pos.repository.CustomerCategoryRepository;
import com.alphadevs.web.pos.repository.search.CustomerCategorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CustomerCategory.
 */
@Service
@Transactional
public class CustomerCategoryService {

    private final Logger log = LoggerFactory.getLogger(CustomerCategoryService.class);
    
    private final CustomerCategoryRepository customerCategoryRepository;

    private final CustomerCategorySearchRepository customerCategorySearchRepository;

    public CustomerCategoryService(CustomerCategoryRepository customerCategoryRepository, CustomerCategorySearchRepository customerCategorySearchRepository) {
        this.customerCategoryRepository = customerCategoryRepository;
        this.customerCategorySearchRepository = customerCategorySearchRepository;
    }

    /**
     * Save a customerCategory.
     *
     * @param customerCategory the entity to save
     * @return the persisted entity
     */
    public CustomerCategory save(CustomerCategory customerCategory) {
        log.debug("Request to save CustomerCategory : {}", customerCategory);
        CustomerCategory result = customerCategoryRepository.save(customerCategory);
        customerCategorySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the customerCategories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomerCategory> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerCategories");
        Page<CustomerCategory> result = customerCategoryRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one customerCategory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CustomerCategory findOne(Long id) {
        log.debug("Request to get CustomerCategory : {}", id);
        CustomerCategory customerCategory = customerCategoryRepository.findOne(id);
        return customerCategory;
    }

    /**
     *  Delete the  customerCategory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerCategory : {}", id);
        customerCategoryRepository.delete(id);
        customerCategorySearchRepository.delete(id);
    }

    /**
     * Search for the customerCategory corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomerCategory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerCategories for query {}", query);
        Page<CustomerCategory> result = customerCategorySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
