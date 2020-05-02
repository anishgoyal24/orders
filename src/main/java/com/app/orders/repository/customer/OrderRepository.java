package com.app.orders.repository.customer;

import com.app.orders.entity.OrderHeader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderHeader, Double> {

    public List<OrderHeader> findByPartyDetailsPartyEmail(String email, Pageable pageable);
}
