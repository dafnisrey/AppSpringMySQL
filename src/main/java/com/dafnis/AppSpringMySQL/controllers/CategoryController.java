package com.dafnis.AppSpringMySQL.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dafnis.AppSpringMySQL.DTOModels.CategoryDTO;
import com.dafnis.AppSpringMySQL.models.Category;
import com.dafnis.AppSpringMySQL.services.CategoryService;
import jakarta.validation.Valid;

@RestController
public class CategoryController {
    
    private CategoryService categoryService;
    private CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }

    @PostMapping("/category")
    public ResponseEntity<?> saveCategory(@RequestBody @Valid Category category){
        try{
            return ResponseEntity.ok(categoryService.saveCategory(category));
        }catch(InternalException e){
            return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR)).body(e.getMessage());
        }
    }

    @PatchMapping("/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO){
        try{
            return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id){
        try{
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    
}
