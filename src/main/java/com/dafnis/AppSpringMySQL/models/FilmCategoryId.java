package com.dafnis.AppSpringMySQL.models;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class FilmCategoryId implements Serializable{

    private Integer filmId;
    private Integer categoryId;

    public FilmCategoryId(Integer filmId, Integer categoryId){
        this.filmId = filmId;
        this.categoryId = categoryId;
    }

    public FilmCategoryId(){}
}
