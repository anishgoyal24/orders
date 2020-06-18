package com.app.orders.repository.warehouse;

import com.app.orders.entity.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseStockRepository extends JpaRepository<ItemStock, Integer> {

    public ItemStock findByItemPackingDetailsId(Integer itemId);

    @Query("select itemStock from ItemStock itemStock where itemStock.itemPackingDetails.id=:itemId and itemStock.warehouseDetails.warehouseId=:warehouseId")
    public ItemStock findStock(@Param("itemId") Integer itemId, @Param("warehouseId") Integer warehouseId);

    @Query("select max(itemStock.price) from ItemStock itemStock where itemStock.warehouseDetails.state.stateFullCode=:state and itemStock.itemPackingDetails.id=:itemId")
    public Double findPrice(@Param("state") String state, @Param("itemId") Integer id);

    @Query("select itemStock.id, itemStock.itemPackingDetails.id, itemStock.itemPackingDetails.size, itemStock.itemPackingDetails.itemDetails.itemName, " +
            "itemStock.price, itemStock.quantity from ItemStock itemStock where itemStock.warehouseDetails.warehouseId=:warehouseId")
    public List<Object> findByWarehouseDetailsWarehouseId(@Param("warehouseId") int warehouseId);
}
