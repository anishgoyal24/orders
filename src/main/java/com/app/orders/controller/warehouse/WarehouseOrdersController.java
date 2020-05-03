package com.app.orders.controller.warehouse;

import com.app.orders.service.warehouse.WarehouseOrderService;
import com.app.orders.utils.View;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/order")
public class WarehouseOrdersController {

    private WarehouseOrderService warehouseOrderService;

    @Autowired
    public WarehouseOrdersController(WarehouseOrderService warehouseOrderService) {
        this.warehouseOrderService = warehouseOrderService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_warehouse', 'ROLE_manager')")
    @GetMapping(value = "/details")
    @JsonView(View.OrderDetailView.class)
    public HashMap<String, Object> getOrderDetails(@RequestParam String orderId){
        return warehouseOrderService.getOrderDetails(orderId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_warehouse', 'ROLE_manager')")
    @PostMapping(value = "/accept")
    public HashMap<String, Object> acceptOrder(@RequestBody HashMap<String, Object> body){
        return warehouseOrderService.acceptOrder(body);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_warehouse', 'ROLE_manager')")
    @PostMapping(value = "/status")
    public HashMap<String, Object> changeStatus(@RequestBody HashMap<String, Object> body){
        return warehouseOrderService.changeStatus(body);
    }

}
