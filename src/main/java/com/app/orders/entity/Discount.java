package com.app.orders.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    private int id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemPackingDetails itemPackingDetails;
    private float discount;
    private char status;
}
