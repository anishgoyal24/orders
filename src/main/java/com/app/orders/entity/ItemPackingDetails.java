package com.app.orders.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "packing_details")
public class ItemPackingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    private int id;
    private int size;
    private char status;
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemDetails itemDetails;

    @Override
    public String toString() {
        return "ItemPackingDetails{" +
                "size=" + size +
                ", status=" + status +
                ", itemDetails=" + itemDetails.getItemId() +
                '}';
    }
}
