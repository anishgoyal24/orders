package com.app.orders.repository.warehouse;

import com.app.orders.entity.WarehouseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WarehouseDetailsRepository extends JpaRepository<WarehouseDetails, Integer> {

    @Query("select warehouse from WarehouseDetails warehouse where warehouse.ownerWarehouse=:owner")
    List<WarehouseDetails> findDynamic(@Param("owner") Integer owner);
}
