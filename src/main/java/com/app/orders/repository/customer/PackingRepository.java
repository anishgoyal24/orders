package com.app.orders.repository.customer;

import com.app.orders.entity.ItemPackingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackingRepository extends JpaRepository<ItemPackingDetails, Integer> {

}
