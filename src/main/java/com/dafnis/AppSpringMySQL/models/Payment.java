package com.dafnis.AppSpringMySQL.models;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer id;
    @Transient
    private Integer customerIdIntroducido;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Transient
    private Integer staffIdIntroducido;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    @Transient
    private Integer rentalIdIntroducido;
    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;
    @NotNull(message = "Debes proporcionar un amount.")
    private BigDecimal amount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date payment_date;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private Date last_update;
    
    @JsonIgnore
    public Integer getCustomerIdIntroducido(){
        return customerIdIntroducido;
    }

    @JsonProperty("customer_id")
    public void setCustomerIdIntroducido(Integer id){
        customerIdIntroducido = id;
    }

    @JsonIgnore
    public Integer getStaffIdIntroducido(){
        return staffIdIntroducido;
    }

    @JsonProperty("staff_id")
    public void setStaffIdIntroducido(Integer id){
        staffIdIntroducido = id;
    }

    @JsonIgnore
    public Integer getRentalIdIntroducido(){
        return rentalIdIntroducido;
    }

    @JsonProperty("rental_id")
    public void setRentalIdIntroducido(Integer id){
        rentalIdIntroducido = id;
    }
}


