package com.app.orders.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(targetEntity = PartyDetails.class)
    @JoinColumn(name = "party_id")
    private PartyDetails partyDetails;
    @OneToOne(targetEntity = ItemPackingDetails.class)
    private ItemPackingDetails itemPackingDetails;
    private int quantity;
    private double price;
}
