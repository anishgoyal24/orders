package com.app.orders.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "STATE_DIR")
public class State {

    @Id
    @Column(name = "STATE_FULL_CODE")
    private String stateFullCode;

    @Column(name = "STATE_CODE_3")
    private String stateCode3;

    @OneToOne
    @JoinColumn(referencedColumnName = "COUNTRY_CODE_3", name = "COUNTRY_CODE_3")
    private Country countryCode3;

    @Column(name = "STATE_NAME")
    private String stateName;
}
