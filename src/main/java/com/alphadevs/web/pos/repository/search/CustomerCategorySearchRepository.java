package com.alphadevs.web.pos.repository.search;

import com.alphadevs.web.pos.domain.CustomerCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CustomerCategory entity.
 */
public interface CustomerCategorySearchRepository extends ElasticsearchRepository<CustomerCategory, Long> {
}
