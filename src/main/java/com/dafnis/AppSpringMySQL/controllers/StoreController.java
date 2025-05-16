package com.dafnis.AppSpringMySQL.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dafnis.AppSpringMySQL.DTOModels.StoreDTO;
import com.dafnis.AppSpringMySQL.models.Staff;
import com.dafnis.AppSpringMySQL.models.Store;
import com.dafnis.AppSpringMySQL.services.StoreService;
import jakarta.validation.Valid;

@RestController
public class StoreController {

    private StoreService storeService;
    private StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    @GetMapping("/stores")
    public ResponseEntity<List<Store>> getAll(){
        return ResponseEntity.ok(storeService.getAll());
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<?> getStoreById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(storeService.getStoreById(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/store/{id}/staff")
    public ResponseEntity<List<Staff>> getStaffByStoreId(@PathVariable Integer id){
        return ResponseEntity.ok(storeService.getStaffByStoreId(id));
    }

    @PostMapping("/store")
    public ResponseEntity<?> saveStore(@RequestBody @Valid Store store){
        if(store.getAddressIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un address_id.");
        }
        if(store.getStaffIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un staff_id.");
        }
        try{
            return ResponseEntity.ok(storeService.saveStore(store));
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }catch(DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/store/{id}")
    public ResponseEntity<?> updateStore(@PathVariable Integer id, @RequestBody StoreDTO storeDTO){
        try{
            return ResponseEntity.ok(storeService.updateStore(id, storeDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/store/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Integer id){
        try{
            storeService.deleteStore(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    
}
