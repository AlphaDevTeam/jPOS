package com.alphadevs.web.pos.repository.search;

import com.alphadevs.web.pos.domain.ContactInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ContactInfo entity.
 */
public interface ContactInfoSearchRepository extends ElasticsearchRepository<ContactInfo, Long> {
}
