package com.alphadevs.web.pos.repository.search;

import com.alphadevs.web.pos.domain.Design;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Design entity.
 */
public interface DesignSearchRepository extends ElasticsearchRepository<Design, Long> {
}
