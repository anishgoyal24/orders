package com.app.orders.repository.employee;

import com.app.orders.entity.PincodeWarehouseMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PincodeMappingRepository extends JpaRepository<PincodeWarehouseMapping, Integer> {

    List<PincodeWarehouseMapping> findByPincodeContaining(String pincode);

    PincodeWarehouseMapping findByPincodeAndWarehouseDetailsWarehouseId(String pincode, Integer warehouseId);

    List<PincodeWarehouseMapping> findFirst10ByOrderById();

    List<PincodeWarehouseMapping> findFirst10ByIdLessThan(Integer id);

    @Query("select count(mapping) from PincodeWarehouseMapping mapping where mapping.pincode=:pincode")
    int findCount(@Param("pincode") String pincode);

    PincodeWarehouseMapping findByWarehouseDetailsWarehouseIdAndPincode(Integer warehouseId, String pincode);
}
