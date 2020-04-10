package com.app.orders.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    private int id;
    @ManyToOne(targetEntity = PartyDetails.class)
    @JoinColumn(name = "party_id")
    private PartyDetails partyDetails;
    @OneToOne(targetEntity = ItemPackingDetails.class)
    private ItemPackingDetails itemPackingDetails;
    private int quantity;
    private double price;
}
