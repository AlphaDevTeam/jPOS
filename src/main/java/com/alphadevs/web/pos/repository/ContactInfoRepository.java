package com.alphadevs.web.pos.repository;

import com.alphadevs.web.pos.domain.ContactInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ContactInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo,Long> {

}
