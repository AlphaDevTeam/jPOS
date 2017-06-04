package com.alphadevs.web.pos.repository;

import com.alphadevs.web.pos.domain.Design;
import com.alphadevs.web.pos.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Design entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesignRepository extends JpaRepository<Design,Long> {

    Page<Design> findAllByRelatedProduct(Pageable pageable, Product product);
}
