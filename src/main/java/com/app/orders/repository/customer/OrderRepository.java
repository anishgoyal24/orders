package com.app.orders.repository.customer;

import com.app.orders.entity.OrderDetail;
import com.app.orders.entity.OrderHeader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderHeader, String> {

    public List<OrderHeader> findByPartyDetailsPartyId(Integer partyId, Pageable pageable);

    @Query("select orderHeader.orderDetails from OrderHeader orderHeader where orderHeader.orderId=:orderId")
    List<OrderDetail> findOrderDetails(@Param("orderId") String orderId);
}
