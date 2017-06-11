package com.alphadevs.web.pos.repository;

import com.alphadevs.web.pos.domain.CashBook;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CashBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashBookRepository extends JpaRepository<CashBook,Long> {

}
