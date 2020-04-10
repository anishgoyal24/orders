package com.app.orders.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "party_points")
public class PartyPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    private double id;
    @ManyToOne
    @JoinColumn(name = "party_id")
    private PartyDetails partyDetails;
    private int points;
    private String reference;
    @Column(name = "reference_id")
    private String referenceId;

}
