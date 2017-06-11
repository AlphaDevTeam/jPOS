package com.alphadevs.web.pos.repository.search;

import com.alphadevs.web.pos.domain.CashBook;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CashBook entity.
 */
public interface CashBookSearchRepository extends ElasticsearchRepository<CashBook, Long> {
}
