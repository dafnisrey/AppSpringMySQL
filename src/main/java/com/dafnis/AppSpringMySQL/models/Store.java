package com.dafnis.AppSpringMySQL.models;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Integer id;
    @Transient
    private Integer staffIdIntroducido;
    @OneToOne
    @JoinColumn(name = "manager_staff_id")
    @JsonManagedReference
    private Staff staff;
    @Transient
    private Integer addressIdIntroducido;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private List<Customer> customers;
    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private List<Inventory> inventory;
    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private List<Staff> staffList;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private Date last_update;

    @JsonProperty("address_id")
    public void setAddressIdIntroducido(Integer id){
        addressIdIntroducido = id;
    }

    @JsonIgnore
    public Integer getAddressIdIntroducido(){
        return addressIdIntroducido;
    }

    @JsonProperty("staff_id")
    public void setStaffIdIntroducido(Integer id){
        staffIdIntroducido = id;
    }

    @JsonIgnore
    public Integer getStaffIdIntroducido(){
        return staffIdIntroducido;
    }
    
}
