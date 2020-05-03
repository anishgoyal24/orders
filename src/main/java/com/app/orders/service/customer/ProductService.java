package com.app.orders.service.customer;

import com.app.orders.entity.ItemDetails;
import com.app.orders.entity.PincodeWarehouseMapping;
import com.app.orders.repository.customer.DiscountRepository;
import com.app.orders.repository.customer.PartyStockRepository;
import com.app.orders.repository.customer.ProductRepository;
import com.app.orders.repository.employee.PincodeMappingRepository;
import com.app.orders.utils.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    private HashMap<String, Object> returnObject;
    @Autowired
    private PartyStockRepository partyStockRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private PincodeMappingRepository pincodeMappingRepository;

    public HashMap<String, Object> searchItem(String searchQuery, String type){
        returnObject = new HashMap<>();
        List<ItemDetails> itemDetailsList = productRepository.findByItemNameContainingIgnoreCase(searchQuery, type);
        if (itemDetailsList!=null){
            List<ProductDTO> productDTOS = new ArrayList<>();
            returnObject.put("message", "success");
            for (ItemDetails itemDetails: itemDetailsList){
                productDTOS.add(new ProductDTO(itemDetails, itemDetails.getItemPackingDetails()));
            }
            returnObject.put("data", productDTOS);
        }
        else
            returnObject.put("message", "no such item");
        return returnObject;
    }

    public HashMap retrieveItem(Integer itemId, String state){
        returnObject = new HashMap<>();
        Object[][] objects = partyStockRepository.findStockAndPrice("", itemId, new ArrayList<>(), state);
        if (objects.length>0 && objects!=null){
            if ((long)objects[0][0]>=0) {
                returnObject.put("data", productRepository.findById(itemId));
                returnObject.put("message", "success");
                returnObject.put("itemId", itemId);
                returnObject.put("stock", objects[0][0]);
                returnObject.put("price", objects[0][1]);
            }
            else
                returnObject.put("message", "Out of stock");
        }
        else
            returnObject.put("message", "Not available in your area");
        return returnObject;
    }

    public HashMap<String, Object> getDiscount(Integer itemId){
        returnObject = new HashMap<>();
        float discount = discountRepository.findExistingDiscount(itemId).getDiscount();
        if (discount>=0)
            returnObject.put("discount", discount);
        else
            returnObject.put("discount", 0);
        return returnObject;
    }

    public HashMap<String, Object> listProducts(String type) {
        returnObject = new HashMap<>();
        List<ItemDetails> items = productRepository.findByCustomerAllowedAndStatus(type, 'y');
        if (items!=null){
            returnObject.put("message", "success");
            returnObject.put("data", items);
        }
        else returnObject.put("message", "no products");
        return returnObject;
    }

    public HashMap<String, Object> getStockAndPrice(int productId, String pincode, String state){
        returnObject = new HashMap<>();
        List<PincodeWarehouseMapping> dynamic = pincodeMappingRepository.findByPincodeContaining(pincode);
        List<Integer> dynamicIds = new ArrayList<>();
        for (PincodeWarehouseMapping mapping: dynamic){
            dynamicIds.add(mapping.getWarehouseDetails().getWarehouseId());
        }
        Object[][] found = partyStockRepository.findStockAndPrice(pincode, productId, dynamicIds, state);
        if (found.length>0){
            returnObject.put("stock", found[0][0]);
            returnObject.put("price", found[0][1]);
            returnObject.put("message", "success");
        }
        else returnObject.put("message", "Not available in your area.");
        return returnObject;

    }
}
