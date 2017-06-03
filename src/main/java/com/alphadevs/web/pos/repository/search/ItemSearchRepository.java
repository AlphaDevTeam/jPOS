package com.alphadevs.web.pos.repository.search;

import com.alphadevs.web.pos.domain.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Item entity.
 */
public interface ItemSearchRepository extends ElasticsearchRepository<Item, Long> {
}
