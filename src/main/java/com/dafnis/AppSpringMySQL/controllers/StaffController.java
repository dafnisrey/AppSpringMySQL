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
import com.dafnis.AppSpringMySQL.DTOModels.StaffDTO;
import com.dafnis.AppSpringMySQL.models.Staff;
import com.dafnis.AppSpringMySQL.services.StaffService;
import jakarta.validation.Valid;

@RestController
public class StaffController {

    private StaffService staffService;
    private StaffController(StaffService staffService){
        this.staffService = staffService;
    }

    @GetMapping("/staff")
    public ResponseEntity<List<Staff>> getAll(){
        return ResponseEntity.ok(staffService.getAll());
    }

    @GetMapping("/staff/{id}")
    public ResponseEntity<?> getStaffById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(staffService.getStaffById(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/staff/{id}/stores")
    public ResponseEntity<?> getStoresByStaffId(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(staffService.getStoresByStaffId(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/staff")
    public ResponseEntity<?> saveStaff(@RequestBody @Valid Staff staff){
        if(staff.getAddressIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un address_id.");
        }
        if(staff.getStoreIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un store_id.");
        }
        try{
            return ResponseEntity.ok(staffService.saveStaff(staff));
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/staff/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable Integer id, @RequestBody StaffDTO staffDTO){
        try{
            return ResponseEntity.ok(staffService.updateStaff(id, staffDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/staff/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Integer id){
        try{
            staffService.deleteStaff(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    
}
