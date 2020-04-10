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
@Table(name = "otp")
public class OTP {

    @Id
    @Column(name = "otp")
    private int otp;
    @Column(name = "email")
    private String email;

    public OTP(int otp, String email) {
        this.otp = otp;
        this.email = email;
    }

    OTP(){}
}
