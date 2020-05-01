package com.app.orders.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "pincode_warehouse_mapping")
public class PincodeWarehouseMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    private int id;

    @Column(name = "pincode")
    private String pincode;

    @OneToOne
    @JoinColumn(name = "warehouse", referencedColumnName = "warehouse_id")
    private WarehouseDetails warehouseDetails;
}
