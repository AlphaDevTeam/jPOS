package com.alphadevs.web.pos.repository;

import com.alphadevs.web.pos.domain.Product;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

}
