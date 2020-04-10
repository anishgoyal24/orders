package com.app.orders.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    private double id;
    @OneToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private ItemPackingDetails itemDetails;
    private int quantity;
    @Column(name = "actual_cost")
    private double actualCost;
    @Column(name = "discounted_cost")
    private double discountedCost;

}
