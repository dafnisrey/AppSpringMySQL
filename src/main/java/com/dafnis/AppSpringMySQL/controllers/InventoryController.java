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
import com.dafnis.AppSpringMySQL.DTOModels.InventoryDTO;
import com.dafnis.AppSpringMySQL.models.Inventory;
import com.dafnis.AppSpringMySQL.services.InventoryService;
import jakarta.validation.Valid;

@RestController
public class InventoryController {

    private InventoryService inventoryService;
    private InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory")
    public ResponseEntity<Page<Inventory>> getAll(@RequestParam int numPagina, @RequestParam int tamañoPagina){
        return ResponseEntity.ok(inventoryService.getAll(numPagina, tamañoPagina));
    }

    @GetMapping("/film/{id}/inventory")
    public ResponseEntity<List<Inventory>> getInventoryByFilmId(@PathVariable Integer id){
        return ResponseEntity.ok(inventoryService.getInventoryByFilmId(id));
    }

    @GetMapping("/store/{id}/inventory")
    public ResponseEntity<?> getInventoriesByStoreId(@PathVariable Integer id, @RequestParam int numPagina, @RequestParam int tamañoPagina){
        try{
            return ResponseEntity.ok(inventoryService.getInventoriesByStoreId(id, numPagina, tamañoPagina));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/inventory")
    public ResponseEntity<?> saveInventory(@RequestBody @Valid Inventory inventory){
        if(inventory.getFilmIdIntroducido() == null || inventory.getStoreIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un film_id y un store_id.");
        }
        try{
            return ResponseEntity.ok(inventoryService.saveInventory(inventory));
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping(("/inventory/{id}"))
    public ResponseEntity<?> updateInventory(@PathVariable Integer id, @RequestBody InventoryDTO inventoryDTO){
        try{
            return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/inventory/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Integer id){
        try{
            inventoryService.deleteInventory(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    
}
