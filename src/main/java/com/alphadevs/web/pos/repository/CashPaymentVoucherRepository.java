package com.alphadevs.web.pos.repository;

import com.alphadevs.web.pos.domain.CashPaymentVoucher;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CashPaymentVoucher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashPaymentVoucherRepository extends JpaRepository<CashPaymentVoucher,Long> {

}
