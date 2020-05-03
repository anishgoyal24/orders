package com.app.orders.controller.customer;

import com.app.orders.entity.OrderHeader;
import com.app.orders.service.customer.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/get-price-stock")
    @PreAuthorize("hasAnyAuthority('ROLE_party')")
    public HashMap<String, Object> findStockAnsPrice(@RequestParam int itemId, @RequestParam String pincode, @RequestParam String state){
        return productService.getStockAndPrice(itemId, pincode, state);
    }
}
