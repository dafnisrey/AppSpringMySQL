package com.dafnis.AppSpringMySQL.models;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Integer id;
    @Transient
    private Integer inventoryIdIntroducido;
    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
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
    @OneToMany(mappedBy = "rental")
    @JsonIgnore
    private List<Payment> payments;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date rental_date;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false)
    private Date return_date;
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
    public Integer getInventoryIdIntroducido(){
        return inventoryIdIntroducido;
    }

    @JsonProperty("inventory_id")
    public void setInventoryIdIntroducido(Integer id){
        inventoryIdIntroducido = id;
    }

}
