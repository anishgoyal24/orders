package com.app.orders.repository.employee;

import com.app.orders.entity.PincodeWarehouseMapping;
import com.app.orders.entity.WarehouseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PincodeMappingRepository extends JpaRepository<PincodeWarehouseMapping, Integer> {

    List<PincodeWarehouseMapping> findByPincodeContaining(String pincode);

    PincodeWarehouseMapping findByPincodeAndWarehouseDetailsWarehouseId(String pincode, Integer warehouseId);

    List<PincodeWarehouseMapping> findTop10ByOrderById();

    List<PincodeWarehouseMapping> findFirst10ByIdLessThan(Integer id);
}
