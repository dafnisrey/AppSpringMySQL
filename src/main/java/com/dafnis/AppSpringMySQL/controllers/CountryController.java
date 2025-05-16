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
import com.dafnis.AppSpringMySQL.DTOModels.CountryDTO;
import com.dafnis.AppSpringMySQL.models.Country;
import com.dafnis.AppSpringMySQL.services.CountryService;
import jakarta.validation.Valid;

@RestController
public class CountryController {

    private CountryService countryService;
    private CountryController(CountryService countryService){
        this.countryService = countryService;
    }

    @GetMapping("/countries")
    public ResponseEntity<Page<Country>> getAll(@RequestParam int numPagina, @RequestParam int tamañoPagina){
        return ResponseEntity.ok(countryService.getAll(numPagina, tamañoPagina));
    }

    @GetMapping("/country/{id}")
    public ResponseEntity<?> getCountryById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(countryService.getCountryById(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @GetMapping("/country/{id}/cities")
    public ResponseEntity<?> getCitiesByCountryId(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(countryService.getCitiesByCountryId(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/country")
    public ResponseEntity<?> saveCountry(@RequestBody @Valid Country country){
        try{
            return ResponseEntity.ok(countryService.saveCountry(country));
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/country/{id}")
    public ResponseEntity<?> updateCountry(@PathVariable Integer id, @RequestBody CountryDTO countryDTO){
        try{
            return ResponseEntity.ok(countryService.updateCountry(id, countryDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/country/{id}")
    public ResponseEntity<?> deleteCountry(@PathVariable Integer id){
        try{
            countryService.deleteCountry(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
}
