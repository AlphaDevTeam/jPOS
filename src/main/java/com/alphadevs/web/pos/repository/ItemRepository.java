package com.alphadevs.web.pos.repository;

import com.alphadevs.web.pos.domain.Item;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

}
