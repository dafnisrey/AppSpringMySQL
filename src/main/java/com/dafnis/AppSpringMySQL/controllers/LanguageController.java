package com.dafnis.AppSpringMySQL.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dafnis.AppSpringMySQL.models.Language;
import com.dafnis.AppSpringMySQL.services.LanguageService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/language")
public class LanguageController {
    
    private LanguageService languageService;
    private LanguageController(LanguageService languageService){
        this.languageService = languageService;
    }

    @GetMapping
    public ResponseEntity<List<Language>> getAll(){
        return ResponseEntity.ok(languageService.getAll());
    }

    @PostMapping
    public ResponseEntity<?> saveLanguage(@RequestBody @Valid Language language){
        try{
            return ResponseEntity.ok(languageService.saveLanguage(language));
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLanguage(@PathVariable Integer id){
        try{
            languageService.deleteLanguage(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    
}
