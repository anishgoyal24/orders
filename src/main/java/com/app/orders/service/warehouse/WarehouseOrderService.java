package com.app.orders.service.warehouse;

import com.app.orders.entity.*;
import com.app.orders.repository.customer.DetailsRepository;
import com.app.orders.repository.customer.PackingRepository;
import com.app.orders.repository.warehouse.WarehouseDetailsRepository;
import com.app.orders.repository.warehouse.WarehouseOrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private WarehouseStockService warehouseStockService;
    private PackingRepository packingRepository;
    private DetailsRepository detailsRepository;

    @Autowired
    public WarehouseOrderService(WarehouseOrdersRepository warehouseOrdersRepository, WarehouseDetailsRepository warehouseDetailsRepository, WarehouseStockService warehouseStockService, PackingRepository packingRepository, DetailsRepository detailsRepository) {
        this.warehouseOrdersRepository = warehouseOrdersRepository;
        this.warehouseDetailsRepository = warehouseDetailsRepository;
        this.warehouseStockService = warehouseStockService;
        this.packingRepository = packingRepository;
        this.detailsRepository = detailsRepository;
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
                    returnObject.put("date error", "date parse exception");
                }
                warehouseStockService.minus(orderHeader);
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
            if (String.valueOf(body.get("status")).equals("Closed")){
                orderHeader.setDeliveryDate(new Date());
                orderHeader.setClosedBy(orderHeader.getWarehouseDetails().getWarehouseName());
            }
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

    @Transactional(rollbackFor = Exception.class)
    public HashMap<String, Object> closeOrder(HashMap<String, Object> transferObject) {
        returnObject = new HashMap<>();
        Optional<OrderHeader> optionalOrderHeader = warehouseOrdersRepository.findById(String.valueOf(transferObject.get("orderId")));
        if (optionalOrderHeader.isPresent()){
            OrderHeader orderHeader = optionalOrderHeader.get();
            orderHeader.setClosedBy(String.valueOf(transferObject.get("closedBy")));
            orderHeader.setReceivedBy(String.valueOf(transferObject.get("receivedBy")));
            orderHeader.setStatus("Closed");
            orderHeader.setDeliveryDate(new Date());
            warehouseOrdersRepository.save(orderHeader);
            returnObject.put("message", "success");
        }
        else returnObject.put("message", "no such order");
        return returnObject;
    }

    @Transactional(rollbackFor = Exception.class)
    public HashMap<String, Object> addOrder(OrderHeader orderHeader) {
        returnObject = new HashMap<>();
        PartyDetails cashParty = detailsRepository.findByPartyEmail("cashcounter@na.com");
        orderHeader.setOrderId(createOrderId(orderHeader.getPartyDetails().getPartyId()));
        orderHeader.setPartyDetails(cashParty);
        Optional<WarehouseDetails> found = warehouseDetailsRepository.findById(orderHeader.getWarehouseDetails().getWarehouseId());
        if (found.isPresent()){
            WarehouseDetails warehouseDetails = found.get();
            orderHeader.setWarehouseDetails(warehouseDetails);
            for (OrderDetail orderDetail: orderHeader.getOrderDetails()){
                ItemPackingDetails itemPackingDetails = packingRepository.findById(orderDetail.getItemDetails().getId()).get();
                if (itemPackingDetails.getStatus()=='y')
                    orderDetail.setItemDetails(itemPackingDetails);
                else orderHeader.getOrderDetails().remove(orderDetail);
            }
            orderHeader.setClosedBy(warehouseDetails.getWarehouseName());
            orderHeader.setDeliveryDate(new Date());
            orderHeader.setOrderDate(orderHeader.getDeliveryDate());
            orderHeader.setExpectedDeliveryDate(orderHeader.getDeliveryDate());
            orderHeader.setReceivedBy("Cash Counter");
            warehouseOrdersRepository.save(orderHeader);
            returnObject.put("message", "success");
        }
        else returnObject.put("message", "failure");
        return returnObject;
    }



    //  Helper function to generate orderId
    private String createOrderId(int partyId){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddssMMmmyyyyHH");
        Date date = new Date();
        String orderId = simpleDateFormat.format(date) + partyId;
        return orderId;
    }
}
