package com.app.orders.entity;


import com.app.orders.utils.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "packing_details")
public class ItemPackingDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    private int id;
    @JsonView(View.OrderDetailView.class)
    private int size;
    private char status;
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonView({View.OrderDetailView.class})
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
