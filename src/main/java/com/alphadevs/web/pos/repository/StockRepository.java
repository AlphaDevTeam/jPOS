package com.alphadevs.web.pos.repository;

import com.alphadevs.web.pos.domain.Stock;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Stock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRepository extends JpaRepository<Stock,Long> {

}
