package com.app.orders.controller.customer;

import com.app.orders.entity.OrderHeader;
import com.app.orders.service.customer.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

// Controller  to manage and place orders
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

//  Checks if all items in the order are available
    @GetMapping(value = "/check")
    @PreAuthorize("hasAnyAuthority('ROLE_party')")
    public HashMap<String, Object> checkOrder(@RequestBody OrderHeader orderHeader){
        return orderService.checkStock(orderHeader);
    }

//  Places the order
    @PostMapping(value = "/place")
    @PreAuthorize("hasAnyAuthority('ROLE_party')")
    public HashMap<String, Object> placeOrder(@RequestBody OrderHeader orderHeader){
        return orderService.placeOrder(orderHeader);
    }

//  Get the order list of a user
    @GetMapping(value = "/list")
    @PreAuthorize("hasAnyAuthority('ROLE_party')")
    public HashMap<String, Object> getOrder(@RequestParam Integer partyId, @RequestParam Integer page){
        return orderService.getOrders(partyId, page);
    }

//  Get Order Details
    @GetMapping(value = "/party/details")
    @PreAuthorize("hasAnyAuthority('ROLE_party')")
    public HashMap<String, Object> getOrderDetails(@RequestParam String orderId){
        return orderService.getOrderDetails(orderId);
    }


    @PostMapping(value = "/cancel")
    @PreAuthorize("hasAnyAuthority('ROLE_party')")
    public HashMap<String, Object> cancelOrder(@RequestBody String orderId){
        return orderService.cancelOrder(orderId);
    }
}
