package com.dafnis.AppSpringMySQL.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import javax.management.relation.RelationNotFoundException;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dafnis.AppSpringMySQL.models.Actor;
import com.dafnis.AppSpringMySQL.models.Film;
import com.dafnis.AppSpringMySQL.models.FilmActor;
import com.dafnis.AppSpringMySQL.services.FilmActorService;
import jakarta.validation.Valid;

@RestController
public class FilmActorController {
    
    private FilmActorService filmActorService;
    private FilmActorController(FilmActorService filmActorService){
        this.filmActorService = filmActorService;
    }

    @GetMapping("/film/{id}/actors")
    public ResponseEntity<List<Actor>> getActorsByFilmId(@PathVariable Integer id){
        return ResponseEntity.ok().body(filmActorService.getActorsByFilmId(id));
    }

    @GetMapping("/actor/{id}/films")
    public ResponseEntity<List<Film>> getFilmsByActorId(@PathVariable Integer id){
        return ResponseEntity.ok().body(filmActorService.getFilmsByActorId(id));

    }

    @GetMapping("/actor/{id}/filmactors")
    public ResponseEntity<List<FilmActor>> getFilmActorsByActorId(@PathVariable Integer id){
        return ResponseEntity.ok(filmActorService.getFilmActorsByActorId(id));
    }

    @GetMapping("/film/{id}/filmactors")
    public ResponseEntity<List<FilmActor>> getFilmActorsByFilmId(@PathVariable Integer id){
        return ResponseEntity.ok(filmActorService.getFilmActorsByFilmId(id));
    }

    @PostMapping("/filmactor")
    public ResponseEntity<?> saveFilmActor(@RequestBody @Valid FilmActor filmActor){
        try{
            return ResponseEntity.ok(filmActorService.saveFilmActor(filmActor));
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/filmactor")
    public ResponseEntity<?> updateFilmActor(@RequestParam Integer oldFilmId, @RequestParam Integer oldActorId,
                                                @RequestParam Integer newFilmId, @RequestParam Integer newActorId){
        try{
            return ResponseEntity.ok(filmActorService.updateFilmActor(oldFilmId, oldActorId, newFilmId, newActorId));
        }catch(RelationNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/actor/{id}/filmactors")
    public ResponseEntity<?> deleteFilmActorByActorId(@PathVariable Integer id){
        try{
            filmActorService.deleteFilmActorByActorId(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/film/{id}/filmactors")
    public ResponseEntity<?> deleteFilmActorByFilmId(@PathVariable Integer id){
        try{
            filmActorService.deleteFilmActorByFilmId(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    
}
