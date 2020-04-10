package com.app.orders.repository.customer;

import com.app.orders.entity.PartyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsRepository extends JpaRepository<PartyDetails, Integer> {

    PartyDetails findByPartyEmail(String email);
    PartyDetails findByPartyId(Integer partyId);

    PartyDetails findByPrimaryPhone(String primaryPhone);
}
