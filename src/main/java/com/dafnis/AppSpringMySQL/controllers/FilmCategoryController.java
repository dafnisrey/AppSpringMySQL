package com.dafnis.AppSpringMySQL.controllers;

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
import com.dafnis.AppSpringMySQL.models.Category;
import com.dafnis.AppSpringMySQL.models.FilmCategory;
import com.dafnis.AppSpringMySQL.services.FilmCategoryService;
import jakarta.validation.Valid;

@RestController
public class FilmCategoryController {

    private FilmCategoryService filmCategoryService;
    private FilmCategoryController(FilmCategoryService filmCategoryService){
        this.filmCategoryService = filmCategoryService;
    }

    @GetMapping("/film/{id}/category")
    public ResponseEntity<Category> getCategoriesByFilmId(@PathVariable Integer id){
        return ResponseEntity.ok(filmCategoryService.getCategoriesByFilmId(id));
    }

    @GetMapping("/category/{id}/films")
    public ResponseEntity<?> getFilmsByCategoryId(@PathVariable Integer id, @RequestParam int numPagina, @RequestParam int tamañoPagina){
        try{
            return ResponseEntity.ok(filmCategoryService.getFilmsByCategoryId(id, numPagina, tamañoPagina));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/filmcategory")
    public ResponseEntity<?> saveFilmCategory(@RequestBody @Valid FilmCategory filmCategory){
        try{
            return ResponseEntity.ok(filmCategoryService.saveFilmCategory(filmCategory));
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/filmcategory")
    public ResponseEntity<?> updateFilmCategory(@RequestParam Integer oldFilmId, @RequestParam Integer oldCategoryId,
                                                    @RequestParam Integer newFilmId, @RequestParam Integer newCategoryId){
        try{
            return ResponseEntity.ok(filmCategoryService.updateFilmCategory(oldFilmId, oldCategoryId, newFilmId, newCategoryId));
        }catch(RelationNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/category/{id}/filmcategories")
    public ResponseEntity<?> deleteFilmCategoriesByCategoryId(@PathVariable Integer id){
        try{
            filmCategoryService.deleteFilmCategoryByCategoryId(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/film/{id}/filmcategories")
    public ResponseEntity<?> deleteFilmCategoryByFilmId(@PathVariable Integer id){
        try{
            filmCategoryService.deleteFilmCategoryByFilmId(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
