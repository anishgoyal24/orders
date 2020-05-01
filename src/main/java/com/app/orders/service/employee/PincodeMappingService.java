package com.app.orders.service.employee;


import com.app.orders.entity.PincodeWarehouseMapping;
import com.app.orders.entity.WarehouseDetails;
import com.app.orders.repository.employee.PincodeMappingRepository;
import com.app.orders.repository.warehouse.WarehouseDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class PincodeMappingService {

    private WarehouseDetailsRepository warehouseDetailsRepository;
    private PincodeMappingRepository pincodeMappingRepository;
    private HashMap<String, Object> returnObject;

    @Autowired
    public PincodeMappingService(PincodeMappingRepository pincodeMappingRepository, WarehouseDetailsRepository warehouseDetailsRepository) {
        this.pincodeMappingRepository = pincodeMappingRepository;
        this.warehouseDetailsRepository = warehouseDetailsRepository;
    }

    public HashMap<String, Object> search(String pincode){
        returnObject = new HashMap<>();
        List<PincodeWarehouseMapping> mappings = pincodeMappingRepository.findByPincodeContaining(pincode);
        if (mappings!=null){
            returnObject.put("data", mappings);
            returnObject.put("message", "success");
        }
        else {
            returnObject.put("message", "No warehouse found!");
            returnObject.put("status", 404);
        }
        return returnObject;
    }

    public HashMap<String, Object> addMapping(PincodeWarehouseMapping pincodeWarehouseMapping){
        returnObject = new HashMap<>();
        PincodeWarehouseMapping found = pincodeMappingRepository.findByPincodeAndWarehouseDetailsWarehouseId(pincodeWarehouseMapping.getPincode(), pincodeWarehouseMapping.getWarehouseDetails().getWarehouseId());
        if (found==null){
            pincodeWarehouseMapping.setWarehouseDetails(warehouseDetailsRepository.findById(pincodeWarehouseMapping.getWarehouseDetails().getWarehouseId()).get());
            pincodeWarehouseMapping.setEnabled(1);
            pincodeMappingRepository.save(pincodeWarehouseMapping);
            returnObject.put("message", "success");
        }
        else {
            if (found.getEnabled()==0){
                found.setEnabled(1);
                pincodeMappingRepository.save(found);
                returnObject.put("message", "success");
            }
            else returnObject.put("message", "Mapping already exists!");
        }
        return returnObject;
    }

    public HashMap<String, Object> toggleMapping(Integer id){
        returnObject = new HashMap<>();
        Optional<PincodeWarehouseMapping> optional = pincodeMappingRepository.findById(id);
        if (optional.isPresent()){
            PincodeWarehouseMapping found = optional.get();
            if (found.getEnabled()==1)found.setEnabled(0);
            else found.setEnabled(1);
            pincodeMappingRepository.save(found);
            returnObject.put("message", "success");
        }
        else returnObject.put("message", "does not exist");
        return returnObject;
    }

    public HashMap<String, Object> listAll() {
        returnObject = new HashMap<>();
        returnObject.put("data", pincodeMappingRepository.findAll());
        return returnObject;
    }

    public HashMap<String, Object> findTop10(){
        returnObject = new HashMap<>();
        returnObject.put("data", pincodeMappingRepository.findFirst10ByOrderById());
        return returnObject;
    }

    public HashMap<String, Object> findNext10(Integer id){
        returnObject = new HashMap<>();
        returnObject.put("data", pincodeMappingRepository.findFirst10ByIdLessThan(id));
        return returnObject;
    }
}
