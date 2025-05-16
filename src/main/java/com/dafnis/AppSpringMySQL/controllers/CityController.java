package com.dafnis.AppSpringMySQL.controllers;

import java.util.List;
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
import com.dafnis.AppSpringMySQL.DTOModels.CityDTO;
import com.dafnis.AppSpringMySQL.models.Address;
import com.dafnis.AppSpringMySQL.models.City;
import com.dafnis.AppSpringMySQL.services.CityService;
import jakarta.validation.Valid;

@RestController
public class CityController {

    private CityService cityService;
    private CityController(CityService cityService){
        this.cityService = cityService;
    }

    @GetMapping("/cities")
    public ResponseEntity<Page<City>> getAll(@RequestParam int numPagina, @RequestParam int tamañoPagina){
        return ResponseEntity.ok(cityService.getAll(numPagina, tamañoPagina));
    }

    @GetMapping("/city/name/{name}")
    public ResponseEntity<?> getCityByName(@PathVariable String name){
        try{
            return ResponseEntity.ok(cityService.getCityByName(name));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/city/{id}")
    public ResponseEntity<?> getCityById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(cityService.getCityById(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/city/{id}/addresses")
    public ResponseEntity<List<Address>> getAddressesByCityId(@PathVariable Integer id){
        return ResponseEntity.ok(cityService.getAddressesByCityId(id));
    }

    @PostMapping("/city")
    public ResponseEntity<?> saveCity(@RequestBody @Valid City city){
        if(city.getCountryIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un contry_id.");
        }
        try{
            return ResponseEntity.ok(cityService.saveCity(city));
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/city/{id}")
    public ResponseEntity<?> updateCity(@PathVariable Integer id, @RequestBody CityDTO cityDTO){
        try{
            return ResponseEntity.ok(cityService.updateCity(id, cityDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/city/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Integer id){
        try{
            cityService.deleteCity(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
}
