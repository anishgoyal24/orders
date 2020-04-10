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
@Table(name = "emp_mst")
public class EmployeeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    @Column(name = "emp_id")
    private int empId;
    @Column(length = 60)
    private String password;
    @Column(name = "emp_name", nullable = false)
    private String empName;
    @Column(name = "emp_email", unique = true)
    private String empEmail;
    private String address;
    @Column(name = "primary_phone", nullable = false, unique = true)
    private String primaryPhone;
    private String city;
    private String state;
    private String country;
    private String pincode;
    @Column(columnDefinition = "char default 'n'")
    private char status;
    private String role;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false)
    private Date createDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDetails that = (EmployeeDetails) o;
        return empId == that.empId &&
                status == that.status &&
                Objects.equals(password, that.password) &&
                Objects.equals(empName, that.empName) &&
                Objects.equals(empEmail, that.empEmail) &&
                Objects.equals(address, that.address) &&
                Objects.equals(primaryPhone, that.primaryPhone) &&
                Objects.equals(city, that.city) &&
                Objects.equals(state, that.state) &&
                Objects.equals(country, that.country) &&
                Objects.equals(pincode, that.pincode) &&
                Objects.equals(role, that.role) &&
                Objects.equals(createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, password, empName, empEmail, address, primaryPhone, city, state, country, pincode, status, role, createDate);
    }

    public void updateDetails(EmployeeDetails employeeDetails) {
        this.address = employeeDetails.getAddress();
        this.primaryPhone = employeeDetails.getPrimaryPhone();
        this.city = employeeDetails.getCity();
        this.state = employeeDetails.getState();
        this.country = employeeDetails.getCountry();
        this.pincode = employeeDetails.getPincode();
    }
}
