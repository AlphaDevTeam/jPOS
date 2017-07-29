package com.alphadevs.web.pos.repository.search;

import com.alphadevs.web.pos.domain.CashBookBalance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CashBookBalance entity.
 */
public interface CashBookBalanceSearchRepository extends ElasticsearchRepository<CashBookBalance, Long> {
}
