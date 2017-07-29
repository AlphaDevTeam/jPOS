package com.alphadevs.web.pos.repository;

import com.alphadevs.web.pos.domain.CashBookBalance;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CashBookBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashBookBalanceRepository extends JpaRepository<CashBookBalance,Long> {

}
