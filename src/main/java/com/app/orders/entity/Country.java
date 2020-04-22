package com.app.orders.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "COUNTRY_DIR")
public class Country {

    @Id
    @Column(name = "COUNTRY_CODE_3")
    private String countryCode3;

    @Column(name = "COUNTRY_CODE_2")
    private String countryCode2;

    @Column(name = "COUNTRY_NAME")
    private String countryName;
}
