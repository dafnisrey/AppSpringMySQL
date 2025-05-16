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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer id;
    @Transient
    private Integer filmIdIntroducido;
    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;
    @Transient
    private Integer storeIdIntroducido;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    @OneToMany(mappedBy = "inventory")
    @JsonIgnore
    private List<Rental> rentals;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private Date last_update;

    @JsonIgnore
    public Integer getFilmIdIntroducido(){
        return filmIdIntroducido;
    }

    @JsonProperty("film_id")
    public void setFilmIdIntroducido(Integer id){
        filmIdIntroducido = id;
    }

    @JsonIgnore
    public Integer getStoreIdIntroducido(){
        return storeIdIntroducido;
    }

    @JsonProperty("store_id")
    public void setStoreIdIntroducido(Integer id){
        storeIdIntroducido = id;
    }
    
}
