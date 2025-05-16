package com.dafnis.AppSpringMySQL.models;

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
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Date;
import java.util.List;
import org.locationtech.jts.geom.Point;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer id;
    @NotBlank(message = "No has proporcionado address.")
    private String address;
    private String address2;
    @NotBlank(message = "No has introducido distrito.")
    private String district;
    @Transient
    private Integer cityIdIntroducido;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    @NotBlank(message = "No has proporcionado el código postal.")
    private String postal_code;
    @NotBlank(message = "No has proporcionado un teléfono.")
    private String phone;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double latitud;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double longitud;
    @Transient 
    @JsonProperty("latitude")
    public Double getLatitude() {
        return location != null ? location.getY() : null;
    }
    @Transient
    @JsonProperty("longitude")
    public Double getLongitude() {
        return location != null ? location.getX() : null;
    }
    @Column(columnDefinition = "geometry(Point, 4326)")
    @JsonIgnore
    private Point location;
    @OneToMany(mappedBy = "address")
    @JsonIgnore
    private List<Store> stores;
    @OneToMany(mappedBy = "address")
    @JsonIgnore
    private List<Customer> customers;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private Date last_update;

    @JsonIgnore
    public Integer getCityIdIntroducido(){
        return cityIdIntroducido;
    }

    @JsonProperty("city_id")
    public void setCityIdIntroducido(Integer id){
        cityIdIntroducido = id;
    }
    
}
