package com.app.orders.service.warehouse;

import com.app.orders.entity.ItemStock;
import com.app.orders.entity.OrderDetail;
import com.app.orders.entity.OrderHeader;
import com.app.orders.entity.WarehouseDetails;
import com.app.orders.repository.warehouse.WarehouseStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseStockService {

    private WarehouseStockRepository warehouseStockRepository;

    @Autowired
    public WarehouseStockService(WarehouseStockRepository warehouseStockRepository) {
        this.warehouseStockRepository = warehouseStockRepository;
    }


    public void minus (OrderHeader orderHeader){
        WarehouseDetails warehouseDetails = orderHeader.getWarehouseDetails();
        for (OrderDetail orderDetail : orderHeader.getOrderDetails()) {
            ItemStock stock = warehouseStockRepository.findStock(orderDetail.getItemDetails().getId(), warehouseDetails.getWarehouseId());
            stock.setQuantity(stock.getQuantity() - orderDetail.getQuantity());
            warehouseStockRepository.save(stock);
        }
    }

    public void plus (OrderHeader orderHeader){
        WarehouseDetails warehouseDetails = orderHeader.getWarehouseDetails();
        if (warehouseDetails == null)return;
        for (OrderDetail orderDetail : orderHeader.getOrderDetails()) {
            ItemStock stock = warehouseStockRepository.findStock(orderDetail.getItemDetails().getId(), warehouseDetails.getWarehouseId());
            stock.setQuantity(stock.getQuantity() + orderDetail.getQuantity());
            warehouseStockRepository.save(stock);
        }
    }
}
