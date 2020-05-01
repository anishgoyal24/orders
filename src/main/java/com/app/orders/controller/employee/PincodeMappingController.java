package com.app.orders.controller.employee;

import com.app.orders.entity.PincodeWarehouseMapping;
import com.app.orders.service.employee.PincodeMappingService;
import com.app.orders.utils.View;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/pincode-mapping")
public class PincodeMappingController {

    private PincodeMappingService pincodeMappingService;

    @Autowired
    public PincodeMappingController(PincodeMappingService pincodeMappingService) {
        this.pincodeMappingService = pincodeMappingService;
    }

    @GetMapping(value = "/search")
    @PreAuthorize("hasAnyAuthority('ROLE_admin', 'ROLE_owner', 'ROLE_employee')")
    public HashMap<String, Object> search(@RequestParam String pincode){
        return pincodeMappingService.search(pincode);
    }

    @PostMapping(value = "/new")
    @PreAuthorize("hasAnyAuthority('ROLE_admin', 'ROLE_owner', 'ROLE_employee')")
    public HashMap<String, Object> add(@RequestBody PincodeWarehouseMapping pincodeWarehouseMapping){
        return pincodeMappingService.addMapping(pincodeWarehouseMapping);
    }

    @PostMapping(value = "/toggle")
    @PreAuthorize("hasAnyAuthority('ROLE_admin', 'ROLE_owner', 'ROLE_employee')")
    public HashMap<String, Object> toggleStatus(@RequestBody Integer id){
        return pincodeMappingService.toggleMapping(id);
    }

    @GetMapping(value = "/")
    @JsonView(View.PincodeMappingView.class)
    @PreAuthorize("hasAnyAuthority('ROLE_admin', 'ROLE_owner', 'ROLE_employee')")
    public HashMap<String, Object> list(){
        return pincodeMappingService.listAll();
    }

    @GetMapping(value = "/first10")
    @JsonView(View.PincodeMappingView.class)
    @PreAuthorize("hasAnyAuthority('ROLE_admin', 'ROLE_owner', 'ROLE_employee')")
    public HashMap<String, Object> getFirst10(){
        return pincodeMappingService.findTop10();
    }

    @GetMapping(value = "/next10")
    @JsonView(View.PincodeMappingView.class)
    @PreAuthorize("hasAnyAuthority('ROLE_admin', 'ROLE_owner', 'ROLE_employee')")
    public HashMap<String, Object> findNext10(@RequestParam Integer id){
        return pincodeMappingService.findNext10(id);
    }
}
