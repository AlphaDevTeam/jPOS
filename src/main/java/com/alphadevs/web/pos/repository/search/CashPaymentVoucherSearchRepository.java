package com.alphadevs.web.pos.repository.search;

import com.alphadevs.web.pos.domain.CashPaymentVoucher;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CashPaymentVoucher entity.
 */
public interface CashPaymentVoucherSearchRepository extends ElasticsearchRepository<CashPaymentVoucher, Long> {
}
