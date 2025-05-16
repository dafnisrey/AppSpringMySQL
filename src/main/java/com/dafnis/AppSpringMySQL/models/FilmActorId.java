package com.dafnis.AppSpringMySQL.models;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class FilmActorId implements Serializable{

    private Integer filmId;
    private Integer actorId;
    public FilmActorId(Integer filmId, Integer actorId){
        this.filmId = filmId;
        this.actorId = actorId;
        
    }

    public FilmActorId(){}
}
