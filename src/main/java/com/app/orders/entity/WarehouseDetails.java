package com.app.orders.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "warehouse_mst")
public class WarehouseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    @Column(name = "warehouse_id", nullable = false)
    private int warehouseId;
    @Column(length = 60)
    private String password;
    @Column(name = "warehouse_name", nullable = false)
    private String warehouseName;
    @Column(name = "warehouse_email", unique = true)
    private String warehouseEmail;
    @Column(name = "person_of_contact", nullable = false)
    private String personOfContact;
    private String address;
    @Column(name = "primary_phone", nullable = false, unique = true)
    private String primaryPhone;
    @Column(name = "secondary_phone")
    private String secondaryPhone;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private char status;
    private String type;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false)
    private Date createDate;
    private String role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarehouseDetails that = (WarehouseDetails) o;
        return warehouseId == that.warehouseId &&
                status == that.status &&
                Objects.equals(warehouseName, that.warehouseName) &&
                Objects.equals(warehouseEmail, that.warehouseEmail) &&
                Objects.equals(personOfContact, that.personOfContact) &&
                Objects.equals(address, that.address) &&
                Objects.equals(primaryPhone, that.primaryPhone) &&
                Objects.equals(secondaryPhone, that.secondaryPhone) &&
                Objects.equals(city, that.city) &&
                Objects.equals(state, that.state) &&
                Objects.equals(country, that.country) &&
                Objects.equals(pincode, that.pincode) &&
                Objects.equals(type, that.type) &&
                Objects.equals(createDate, that.createDate);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseId, warehouseName, warehouseEmail, personOfContact, address, primaryPhone, secondaryPhone, city, state, country, pincode, status, type, createDate);
    }

    public void updateDetails(WarehouseDetails warehouseDetails) {
        this.warehouseName = warehouseDetails.getWarehouseName();
        this.warehouseEmail = warehouseDetails.getWarehouseEmail();
        this.personOfContact = warehouseDetails.getPersonOfContact();
        this.address = warehouseDetails.getAddress();
        this.primaryPhone = warehouseDetails.getPrimaryPhone();
        this.secondaryPhone = warehouseDetails.getSecondaryPhone();
        this.city = warehouseDetails.getCity();
        this.state = warehouseDetails.getState();
        this.country = warehouseDetails.getCountry();
        this.pincode = warehouseDetails.getPincode();
    }

    public WarehouseDetails() {
    }
}
