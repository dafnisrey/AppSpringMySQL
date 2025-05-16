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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer id;
    @NotBlank(message = "Debes proporcionar un first_name.")
    private String first_name;
    @NotBlank(message = "Debes proporcionar un last_name.")
    private String last_name;
    @NotBlank(message = "Debes proporcionar un email.")
    private String email;
    @NotNull(message = "Debes indicar si es un customer activo. 1 o 0.")
    private Integer active;
    @Transient
    private Integer storeIdIntroducido;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    @Transient
    private Integer addressIdIntroducido;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Rental> rentals;
    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Payment> payments;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date create_date;
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

    @JsonProperty("store_id")
    public void setStoreIdIntroducido(Integer id){
        storeIdIntroducido = id;
    }

    @JsonIgnore
    public Integer getStoreIdIntroducido(){
        return storeIdIntroducido;
    }
    
}


