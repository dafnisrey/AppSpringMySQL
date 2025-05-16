package com.dafnis.AppSpringMySQL.controllers;

import java.util.NoSuchElementException;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
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
import com.dafnis.AppSpringMySQL.DTOModels.FilmDTO;
import com.dafnis.AppSpringMySQL.models.Film;
import com.dafnis.AppSpringMySQL.services.FilmService;
import jakarta.validation.Valid;

@RestController
public class FilmController {
    
    private FilmService filmService;
    private FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public ResponseEntity<Page<Film>> getAll(@RequestParam int numPagina, @RequestParam int tamañoPagina){
        return ResponseEntity.ok(filmService.getAll(numPagina, tamañoPagina));
    }
    
    @GetMapping("/film/{id}")
    public ResponseEntity<?> getFilmById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok().body(filmService.getFilmById(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/language/{id}/films")
    public ResponseEntity<?> getFilmsByLanguageId(@PathVariable Integer id, @RequestParam int numPagina, @RequestParam int tamañoPagina){
        try{
            return ResponseEntity.ok(filmService.getFilmsByLanguageId(id, numPagina, tamañoPagina));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/film")
    public ResponseEntity<?> saveFilm(@RequestBody @Valid Film film){
        if(film.getLanguageIdIntroducido() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Debes introducir un language_id.");
        }
        try{
            return ResponseEntity.ok(filmService.saveFilm(film));
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/film/{id}")
    public ResponseEntity<?> updateFilm(@PathVariable Integer id, @RequestBody FilmDTO filmDTO){
        try{
            return ResponseEntity.ok(filmService.updateFilm(id, filmDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFilm(@PathVariable Integer id){
        try{
            filmService.deleteFilm(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

   
}
