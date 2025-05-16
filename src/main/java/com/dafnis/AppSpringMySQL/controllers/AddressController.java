package com.dafnis.AppSpringMySQL.controllers;

import java.util.NoSuchElementException;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dafnis.AppSpringMySQL.DTOModels.AddressDTO;
import com.dafnis.AppSpringMySQL.models.Address;
import com.dafnis.AppSpringMySQL.services.AddressService;
import jakarta.validation.Valid;

@RestController
public class AddressController {
    
    private AddressService addressService;
    private AddressController(AddressService addressService){
        this.addressService = addressService;
    }

    @GetMapping("/addresses")
    public ResponseEntity<Page<Address>> getAll(@RequestParam int numPagina, @RequestParam int tamañoPagina){
        return ResponseEntity.ok(addressService.getAll(numPagina, tamañoPagina));
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(addressService.getAddressById(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/address/{id}/stores")
    public ResponseEntity<?> getStoresByAddressId(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(addressService.getStoresByAddressId(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/address/{id}/customers")
    public ResponseEntity<?> getCustomersByAddressId(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(addressService.getCustomersByAddressId(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/address")
    public ResponseEntity<?> saveAddress(@RequestBody @Valid Address address){
        if(address.getLatitud() == null || address.getLongitud() == null){
            return ResponseEntity.badRequest().body("Debes introducir latitud y longitud.");
        }
        if(address.getCityIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un city_id.");
        }
        try{
            return ResponseEntity.ok(addressService.saveAddress(address));
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/address/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Integer id, @RequestBody AddressDTO addressDTO){

        try{
            return ResponseEntity.ok(addressService.updateAddress(id, addressDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Integer id){
        try{
            addressService.deleteAddress(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> gestionExcepcionesValid(MethodArgumentNotValidException ex){
        java.util.Map<String, String> errores = new java.util.HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores); 
    }


    
}
