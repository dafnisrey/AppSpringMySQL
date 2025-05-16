package com.dafnis.AppSpringMySQL.models;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "film_category")
public class FilmCategory {

    @EmbeddedId
    private FilmCategoryId id;

    @ManyToOne
    @MapsId("filmId")
    @JoinColumn(name = "film_id")
    private Film film;
    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private Date last_update;

    public void setFilm_id(Integer filmId){
        if(this.id == null){
            this.id = new FilmCategoryId();
        }
        this.id.setFilmId(filmId);
    }
    public void setCategory_id(Integer categoryId){
        if(this.id == null){
            this.id = new FilmCategoryId();
        }
        this.id.setCategoryId(categoryId);
    }

    @NotNull(message = "Debes proporcionar un film_id.")
    public Integer getFilm_id(){
        return this.id != null ? this.id.getFilmId() : null;
    }

    @NotNull(message = "Debes proporcionar un category_id.")
    public Integer getCategory_id(){
        return this.id != null ? this.id.getCategoryId() : null;
    }
    
}
