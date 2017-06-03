package com.alphadevs.web.pos.repository;

import com.alphadevs.web.pos.domain.Design;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Design entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesignRepository extends JpaRepository<Design,Long> {

}
