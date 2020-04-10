package com.app.orders.repository.customer;

import com.app.orders.entity.ItemDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ItemDetails, Integer> {

    @Query("select item.itemId, item.itemName from ItemDetails item where item.status='y' and item.itemName like %:searchQuery% and item.customerAllowed=:type")
    public List<ItemDetails> findByItemNameContainingIgnoreCase(@Param("searchQuery") String queryString, @Param("type") String type);

    public List<ItemDetails> findByCustomerAllowedAndStatus(String customerAllowed, char status);

}
