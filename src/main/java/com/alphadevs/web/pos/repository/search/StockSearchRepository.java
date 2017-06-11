package com.alphadevs.web.pos.repository.search;

import com.alphadevs.web.pos.domain.Stock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Stock entity.
 */
public interface StockSearchRepository extends ElasticsearchRepository<Stock, Long> {
}
