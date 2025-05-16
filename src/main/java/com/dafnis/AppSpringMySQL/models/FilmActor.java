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

@Data
@Entity
@Table(name = "film_actor")
public class FilmActor {

    @EmbeddedId
    private FilmActorId id;
    @ManyToOne
    @MapsId("actorId")
    @JoinColumn(name = "actor_id")
    private Actor actor;
    @ManyToOne
    @MapsId("filmId")
    @JoinColumn(name = "film_id")
    private Film film;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private Date last_update;

    public void setFilm_id(Integer filmId){
        if(this.id == null){
            this.id = new FilmActorId();
        }
        this.id.setFilmId(filmId);
    }
    public void setActor_id(Integer actorId){
        if(this.id == null){
            this.id = new FilmActorId();
        }
        this.id.setActorId(actorId);
    }

    @NotNull(message = "Debes proporcionar un actor_id.")
    public Integer getActor_id(){
        return this.id != null ? this.id.getActorId() : null;
    }

    @NotNull(message = "Debes proporcionar un film_id.")
    public Integer getFilm_id(){
        return this.id != null ? this.id.getFilmId() : null;
    }
    
}


