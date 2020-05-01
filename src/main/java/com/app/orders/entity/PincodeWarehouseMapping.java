package com.app.orders.entity;

import com.app.orders.utils.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "pincode_warehouse_mapping")
public class PincodeWarehouseMapping {

    @Id
    @JsonView(View.PincodeMappingView.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    private int id;

    @JsonView(View.PincodeMappingView.class)
    @Column(name = "pincode")
    private String pincode;

    @OneToOne
    @JsonView(View.PincodeMappingView.class)
    @JoinColumn(name = "warehouse", referencedColumnName = "warehouse_id")
    private WarehouseDetails warehouseDetails;

    @JsonView(View.PincodeMappingView.class)
    private int enabled;
}
