package com.app.orders.utils;

import com.app.orders.entity.ItemDetails;
import com.app.orders.entity.ItemPackingDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO {

    private ItemDetails itemDetails;
    private List<ItemPackingDetails> itemPackingDetails;

    public ProductDTO(ItemDetails itemDetails, List<ItemPackingDetails> itemPackingDetails) {
        this.itemDetails = itemDetails;
        this.itemPackingDetails = itemPackingDetails;
    }
}
