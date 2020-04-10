package com.app.orders.service.customer;

import com.app.orders.entity.OrderDetail;
import com.app.orders.entity.OrderHeader;
import com.app.orders.repository.customer.DetailsRepository;
import com.app.orders.repository.customer.OrderRepository;
import com.app.orders.repository.customer.PartyStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderService {

    private DetailsRepository detailsRepository;
    private PartyStockRepository partyStockRepository;
    private ProductService productService;
    private HashMap<String, Object> returnObject;
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(DetailsRepository detailsRepository, PartyStockRepository partyStockRepository, ProductService productService, OrderRepository orderRepository) {
        this.detailsRepository = detailsRepository;
        this.partyStockRepository = partyStockRepository;
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

//  Place order
    public HashMap<String, Object> placeOrder(OrderHeader orderHeader){
        returnObject = new HashMap<>();
//      Check if party exists
        if (detailsRepository.findByPartyId(orderHeader.getPartyDetails().getPartyId())!=null) {
//          Generate a new order ID
            orderHeader.setOrderId(createOrderId(orderHeader.getPartyDetails().getPartyId()));
//          Create a thread for booking the order
//            TODO
            returnObject.put("message", "waiting for confimation");
            returnObject.put("data", orderHeader);
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
        ArrayList<Float> discount = new ArrayList<>();
        for (OrderDetail orderDetails: orderHeader.getOrderDetails()){
//          Check stock of item
            Object[][] objects = partyStockRepository.findStockAndPrice(orderHeader.getPartyDetails().getState(), orderDetails.getItemDetails().getId());
            if ((int)objects[0][0] < orderDetails.getQuantity()){
                outOfStock.put(orderDetails.getItemDetails().getId(), (int)objects[0][0]);
            }
            discount.add((float)productService.getDiscount(orderDetails.getItemDetails().getId()).get("discount"));
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
    public HashMap<String, Object> getOrders(String email) {
        returnObject = new HashMap<>();
        List<OrderHeader> orders = orderRepository.findByPartyDetailsPartyEmail(email, PageRequest.of(0,10));
        returnObject.put("message", "success");
        returnObject.put("data", orders);
        return returnObject;
    }
}
