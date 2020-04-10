package com.app.orders.repository.customer;

import com.app.orders.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {

    @Query("select d from Discount d where d.itemPackingDetails.id=:id")
    Discount findExistingDiscount(@Param("id") int id);
}
