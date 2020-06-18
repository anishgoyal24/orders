package com.app.orders.service.customer;

import com.app.orders.entity.*;
import com.app.orders.repository.customer.*;
import com.app.orders.service.warehouse.WarehouseStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService {

    private DetailsRepository detailsRepository;
    private PartyStockRepository partyStockRepository;
    private ProductService productService;
    private HashMap<String, Object> returnObject;
    private OrderRepository orderRepository;
    private PackingRepository packingRepository;
    private CartRepository cartRepository;
    private WarehouseStockService warehouseStockService;

    @Autowired
    public OrderService(DetailsRepository detailsRepository, PartyStockRepository partyStockRepository, ProductService productService, OrderRepository orderRepository, PackingRepository packingRepository, CartRepository cartRepository, WarehouseStockService warehouseStockService) {
        this.detailsRepository = detailsRepository;
        this.partyStockRepository = partyStockRepository;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.packingRepository = packingRepository;
        this.cartRepository = cartRepository;
        this.warehouseStockService = warehouseStockService;
    }

//  Place order
    @Transactional(rollbackFor=Exception.class)
    public HashMap<String, Object> placeOrder(OrderHeader orderHeader){
        returnObject = new HashMap<>();
        PartyDetails found = detailsRepository.findByPartyId(orderHeader.getPartyDetails().getPartyId());
//      Check if party exists
        if (found!=null && found.getStatus()=='y') {
//          Generate a new order ID
            orderHeader.setOrderId(createOrderId(orderHeader.getPartyDetails().getPartyId()));
            orderHeader.setPartyDetails(found);
//          TODO call notifications API
            orderHeader.setStatus("Waiting for Confirmation");
            for (OrderDetail orderDetail: orderHeader.getOrderDetails()){
                ItemPackingDetails itemPackingDetails = packingRepository.findById(orderDetail.getItemDetails().getId()).get();
                if (itemPackingDetails.getStatus()=='y')
                    orderDetail.setItemDetails(itemPackingDetails);
                else orderHeader.getOrderDetails().remove(orderDetail);
            }
            orderRepository.save(orderHeader);
            List<Cart> cartList = cartRepository.findByPartyDetailsPartyId(found.getPartyId());
            cartRepository.deleteAll(cartList);
            returnObject.put("message", "success");
            returnObject.put("status", "Waiting for Confirmation");
        }
        else
            returnObject.put("message", "failure");
        return returnObject;
    }


//  Helper function to generate orderId
    private String createOrderId(int partyId){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddssMMmmyyyyHH");
        Date date = new Date();
        String orderId = simpleDateFormat.format(date) + partyId;
        return orderId;
    }

//  Function to check if all items in a order are in stock or not
//  Also returns updated price and discounts
    public HashMap<String, Object> checkStock(OrderHeader orderHeader){
        returnObject = new HashMap<>();
        HashMap<Integer, Integer> outOfStock = new HashMap<>();
        HashMap<Long, Float> discount = new HashMap<>();
        for (OrderDetail orderDetails: orderHeader.getOrderDetails()){
//          Check stock of item
            Object[][] objects = partyStockRepository.findStockAndPrice("", orderDetails.getItemDetails().getId(), new ArrayList<>(), orderHeader.getPartyDetails().getState().getStateFullCode());
            if ((int)objects[0][0] < orderDetails.getQuantity()){
                outOfStock.put(orderDetails.getItemDetails().getId(), (int)objects[0][0]);
            }
            discount.put(orderDetails.getId(), (float)productService.getDiscount(orderDetails.getItemDetails().getId()).get("discount"));
            orderDetails.setActualCost((int)objects[0][1]);
        }
        if (outOfStock.size()>0){
            returnObject.put("message", "some items out of stock");
            returnObject.put("out of stock items", outOfStock);
        }
        returnObject.put("discounts", discount);
        returnObject.put("updated price", orderHeader);
        return returnObject;
    }

//  Get orders by order email
    public HashMap<String, Object> getOrders(Integer partyId, int page) {
        returnObject = new HashMap<>();
        List<OrderHeader> orders = orderRepository.findByPartyDetailsPartyIdOrderByOrderDateDesc(partyId, PageRequest.of(page,10));
        returnObject.put("message", "success");
        returnObject.put("data", orders);
        return returnObject;
    }

//  Get Order Details
    public HashMap<String, Object> getOrderDetails(String orderId){
        returnObject = new HashMap<>();
        List<OrderDetail> orderDetails = orderRepository.findOrderDetails(orderId);
        List<HashMap<String, Object>> data = new ArrayList<>();
        if (orderDetails.size()>0){
            for (OrderDetail orderDetail : orderDetails){
                HashMap<String, Object> items = new HashMap<>();
                items.put("orderDetail", orderDetail);
                items.put("itemName", orderDetail.getItemDetails().getItemDetails().getItemName());
                data.add(items);
            }
            returnObject.put("message", "success");
            returnObject.put("data", data);
        }
        else returnObject.put("message", "failure");
        return returnObject;
    }

    @Transactional(rollbackFor = Exception.class)
    public HashMap<String, Object> cancelOrder(String orderId) {
        returnObject = new HashMap<>();
        Optional<OrderHeader> orderHeaderOptional = orderRepository.findById(orderId);
        if (orderHeaderOptional.isPresent()){
            OrderHeader orderHeader = orderHeaderOptional.get();
            orderHeader.setStatus("Cancelled");
            warehouseStockService.plus(orderHeader);
            orderRepository.save(orderHeader);
            returnObject.put("message", "success");
        }
        else returnObject.put("message", "failure");
        return returnObject;
    }
}
