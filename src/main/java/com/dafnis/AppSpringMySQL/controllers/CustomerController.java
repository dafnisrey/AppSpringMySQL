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
import com.dafnis.AppSpringMySQL.DTOModels.CustomerDTO;
import com.dafnis.AppSpringMySQL.models.Customer;
import com.dafnis.AppSpringMySQL.services.CustomerService;
import jakarta.validation.Valid;

@RestController
public class CustomerController {

    private CustomerService customerService;
    private CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<Page<Customer>> getAll(@RequestParam int numPagina, @RequestParam int tamañoPagina){
        return ResponseEntity.ok(customerService.getAll(numPagina, tamañoPagina));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(customerService.getCustomerById(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/store/{id}/customers")
    public ResponseEntity<?> getCustomersByStoreId(@PathVariable Integer id, @RequestParam int numPagina, @RequestParam int tamañoPagina){
        try{
            return ResponseEntity.ok(customerService.getCustomersByStoreId(id, numPagina, tamañoPagina));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/customer")
    public ResponseEntity<?> saveCustomer(@RequestBody @Valid Customer customer){
        if(customer.getStoreIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un store_id.");
        }
        if(customer.getAddressIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un address_id.");
        }
        try{
            return ResponseEntity.ok(customerService.saveCustomer(customer));
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/customer/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody CustomerDTO customerDTO){
        try{
            return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id){
        try{
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    
}
