package com.dafnis.AppSpringMySQL.models;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.DynamicInsert;
import com.dafnis.AppSpringMySQL.aux.RatingConverter;
import com.dafnis.AppSpringMySQL.aux.SpecialFeaturesConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import lombok.Data;

@Entity
@Data
@DynamicInsert
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Integer id;
    @NotBlank(message = "Debes proporcionar un title.")
    private String title;
    private String description;
    private Year release_year;
    @Transient
    private Integer languageIdIntroducido;
    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;
    private Integer original_language_id;
    @Column(nullable = false)
    private Integer rental_duration;
    @Column(nullable = false)
    private BigDecimal rental_rate;
    private Integer length;
    @Column(nullable = false)
    private BigDecimal replacement_cost;
    @Convert(converter = RatingConverter.class)
    @Column(columnDefinition = "enum('G', 'PG', 'PG-13', 'R', 'NC-17')")
    private Rating rating;
    @Convert(converter = SpecialFeaturesConverter.class)
    @Column(columnDefinition = "set('Trailers','Commentaries','Deleted Scenes','Behind the Scenes')")
    private Set<String> special_features;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private Date last_update;
    @OneToMany(mappedBy = "film")
    @JsonIgnore
    private List<Inventory> inventories;
    @JsonIgnore
    public Integer getLanguageIdIntroducido(){
        return languageIdIntroducido;
    }

    @JsonProperty("language_id")
    public void setLanguageIdIntroducido(Integer id){
        languageIdIntroducido = id;
    }

    public enum Rating{
        G,
        PG,
        PG_13("PG-13"),
        R,
        NC_17("NC-17");
        private String data;
        Rating(){
            this.data = name();
        }
        Rating(String data){
            this.data = data;
        }
        public String getData(){
            return data;
        }
    }
}
