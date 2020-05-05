package com.app.orders.service.warehouse;

import com.app.orders.entity.OrderDetail;
import com.app.orders.entity.OrderHeader;
import com.app.orders.entity.PartyDetails;
import com.app.orders.entity.WarehouseDetails;
import com.app.orders.repository.customer.ProductRepository;
import com.app.orders.repository.warehouse.WarehouseDetailsRepository;
import com.app.orders.repository.warehouse.WarehouseOrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WarehouseOrderService {

    Logger logger = LoggerFactory.getLogger(WarehouseOrderService.class);

    private WarehouseOrdersRepository warehouseOrdersRepository;
    private HashMap<String, Object> returnObject;
    private WarehouseDetailsRepository warehouseDetailsRepository;

    @Autowired
    public WarehouseOrderService(WarehouseOrdersRepository warehouseOrdersRepository, WarehouseDetailsRepository warehouseDetailsRepository) {
        this.warehouseOrdersRepository = warehouseOrdersRepository;
        this.warehouseDetailsRepository = warehouseDetailsRepository;
    }

    public HashMap<String, Object> getOrderDetails(String orderId){
        returnObject = new HashMap<>();
        List<OrderDetail> orderDetails = warehouseOrdersRepository.findOrderDetails(orderId);
        List<HashMap<String, Object>> data = new ArrayList<>();
        if (orderDetails.size()>0){
            for (OrderDetail orderDetail : orderDetails){
                HashMap<String, Object> items = new HashMap<>();
                items.put("orderDetail", orderDetail);
                items.put("itemName", orderDetail.getItemDetails().getItemDetails().getItemName());
                data.add(items);
            }
            PartyDetails partyDetails = warehouseOrdersRepository.findPartyDetailsByOrderId(orderId);
            returnObject.put("message", "success");
            returnObject.put("data", data);
            returnObject.put("partyDetails", partyDetails);
        }
        else returnObject.put("message", "failure");
        return returnObject;
    }

    @Transactional(rollbackFor=Exception.class)
    public HashMap<String, Object> acceptOrder(HashMap<String, Object> body) {
        returnObject = new HashMap<>();
        Optional<OrderHeader> optionalOrderHeader = warehouseOrdersRepository.findById(String.valueOf(body.get("orderId")));
        if (optionalOrderHeader.isPresent()){
            OrderHeader orderHeader = optionalOrderHeader.get();
            if (orderHeader.getStatus().equals("Waiting for Confirmation")){
                orderHeader.setStatus("Confirmed");
                orderHeader.setWarehouseDetails(warehouseDetailsRepository.findById(Integer.parseInt(String.valueOf(body.get("warehouseId")))).get());
                DateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                try {
                    orderHeader.setExpectedDeliveryDate(simpleDateFormat.parse(String.valueOf(body.get("expectedDeliveryDate"))));
                } catch (ParseException e) {
                    returnObject.put("message", "date parse exception");
                }
                warehouseOrdersRepository.save(orderHeader);
                returnObject.put("message", "success");
            }
            else returnObject.put("message", "already assigned");
        }
        else returnObject.put("message", "failure");
        return returnObject;
    }

    @Transactional(rollbackFor=Exception.class)
    public HashMap<String, Object> changeStatus(HashMap<String, Object> body) {
        returnObject = new HashMap<>();
        Optional<OrderHeader> optionalOrderHeader = warehouseOrdersRepository.findById(String.valueOf(body.get("orderId")));
        if (optionalOrderHeader.isPresent()){
            OrderHeader orderHeader = optionalOrderHeader.get();
            orderHeader.setStatus(String.valueOf(body.get("status")));
            warehouseOrdersRepository.save(orderHeader);
            returnObject.put("message", "success");
        }
        else returnObject.put("message", "failure");
        return returnObject;
    }

    public HashMap<String, Object> getOrderIds(Integer warehouseId) {
        returnObject = new HashMap<>();
        List<String> orderIds = warehouseOrdersRepository.findByWarehouseDetailsWarehouseId(warehouseId);
        returnObject.put("message", "success");
        returnObject.put("data", orderIds);
        return returnObject;
    }

    @Transactional(rollbackFor=Exception.class)
    public HashMap<String, Object> transfer(HashMap<String, Object> transferObject) {
        returnObject = new HashMap<>();
        Optional<OrderHeader> optionalOrderHeader = warehouseOrdersRepository.findById(String.valueOf(transferObject.get("orderId")));
        if (optionalOrderHeader.isPresent()){
            OrderHeader orderHeader = optionalOrderHeader.get();
            Optional<WarehouseDetails> optionalWarehouseDetails = warehouseDetailsRepository.findById((int)transferObject.get("dynamicWarehouseId"));
            if (optionalWarehouseDetails.isPresent()){
                WarehouseDetails warehouseDetails = optionalWarehouseDetails.get();
                orderHeader.setWarehouseDetails(warehouseDetails);
                warehouseOrdersRepository.save(orderHeader);
                returnObject.put("message", "success");
            }
            else returnObject.put("message", "no such warehouse");
        }
        else returnObject.put("message", "no such order");
        return returnObject;
    }
}
