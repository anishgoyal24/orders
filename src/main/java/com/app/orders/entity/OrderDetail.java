package com.app.orders.entity;

import com.app.orders.utils.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @JsonView(View.OrderDetailView.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    private long id;
    @OneToOne
    @JsonView(View.OrderDetailView.class)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private ItemPackingDetails itemDetails;
    @JsonView(View.OrderDetailView.class)
    private int quantity;
    @Column(name = "actual_cost")
    private double actualCost;
    @JsonView(View.OrderDetailView.class)
    @Column(name = "discounted_cost")
    private double discountedCost;

}
