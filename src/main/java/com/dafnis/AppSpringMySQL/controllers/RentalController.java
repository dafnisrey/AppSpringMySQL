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
import com.dafnis.AppSpringMySQL.DTOModels.RentalDTO;
import com.dafnis.AppSpringMySQL.models.Rental;
import com.dafnis.AppSpringMySQL.services.RentalService;
import jakarta.validation.Valid;

@RestController
public class RentalController {

    private RentalService rentalService;
    private RentalController(RentalService rentalService){
        this.rentalService = rentalService;
    }

    @GetMapping("/rentals")
    public ResponseEntity<Page<Rental>> getAll(@RequestParam int numPagina, @RequestParam int tamañoPagina){
        return ResponseEntity.ok(rentalService.getAll(numPagina, tamañoPagina));
    }

    @GetMapping("/rental/{id}")
    public ResponseEntity<?> getRentalById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(rentalService.getRentalById(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/customer/{id}/rentals")
    public ResponseEntity<?> getRentalsByCustomerId(@PathVariable Integer id, 
                                        @RequestParam int numPagina, 
                                        @RequestParam int tamañoPagina){
        try{
            return ResponseEntity.ok(rentalService.getRentalsByCustomerId(id, numPagina, tamañoPagina));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/staff/{id}/rentals")
    public ResponseEntity<?> getRentalsByStaffId(@PathVariable Integer id, 
                                        @RequestParam int numPagina, 
                                        @RequestParam int tamañoPagina){
        try{
            return ResponseEntity.ok(rentalService.getRentalsByStaffId(id, numPagina, tamañoPagina));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/rental/{id}/payments")
    public ResponseEntity<?> getPaymentsByRentalId(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(rentalService.getPaymentslByRentalId(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/rental")
    public ResponseEntity<?> saveRental(@RequestBody @Valid Rental rental){
        if(rental.getCustomerIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un customer_id");
        }
        if(rental.getInventoryIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un inventory_id.");
        }
        if(rental.getStaffIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un staff_id.");
        }
        try{
            return ResponseEntity.ok(rentalService.saveRental(rental));
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/rental/{id}")
    public ResponseEntity<?> updateRental(@PathVariable Integer id, @RequestBody RentalDTO rentalDTO){
        try{
            return ResponseEntity.ok(rentalService.updateRental(id, rentalDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/rental/{id}")
    public ResponseEntity<?> deleteRental(@PathVariable Integer id){
        try{
            rentalService.deleteRental(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    
}
