package com.alphadevs.web.pos.repository;

import com.alphadevs.web.pos.domain.Address;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

}
