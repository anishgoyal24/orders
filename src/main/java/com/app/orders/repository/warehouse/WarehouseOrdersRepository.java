package com.app.orders.repository.warehouse;

import com.app.orders.entity.OrderDetail;
import com.app.orders.entity.OrderHeader;
import com.app.orders.entity.PartyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseOrdersRepository extends JpaRepository<OrderHeader, String> {

    @Query("select orderHeader.orderDetails from OrderHeader orderHeader where orderHeader.orderId=:orderId")
    List<OrderDetail> findOrderDetails(@Param("orderId") String orderId);

    @Query("select orderHeader.partyDetails from OrderHeader orderHeader where orderHeader.orderId=:orderId")
    PartyDetails findPartyDetailsByOrderId(@Param("orderId") String orderId);

    @Query("select orderHeader.orderId from OrderHeader orderHeader where orderHeader.warehouseDetails.warehouseId=:warehouseId and NOT (orderHeader.status = 'Closed')")
    List<String> findByWarehouseDetailsWarehouseId(@Param("warehouseId") Integer warehouseId);
}
