package com.alphadevs.web.pos.repository;

import com.alphadevs.web.pos.domain.CustomerCategory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CustomerCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerCategoryRepository extends JpaRepository<CustomerCategory,Long> {

}
