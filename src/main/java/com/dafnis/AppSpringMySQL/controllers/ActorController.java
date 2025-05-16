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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dafnis.AppSpringMySQL.DTOModels.ActorDTO;
import com.dafnis.AppSpringMySQL.models.Actor;
import com.dafnis.AppSpringMySQL.services.ActorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/actor")
public class ActorController {

    private ActorService actorService;
    private ActorController(ActorService actorService){
        this.actorService = actorService;
    }

    @GetMapping
    public ResponseEntity<Page<Actor>> getAll(@RequestParam int numPagina, @RequestParam int tamañoPagina){
        return ResponseEntity.ok().body(actorService.getAll(numPagina, tamañoPagina));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getActorById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok().body(actorService.getActorById(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Actor>> getActorsByFirstName(@PathVariable String name){
        return ResponseEntity.ok(actorService.getActorsByFirstName(name));
    }

    @PostMapping
    public ResponseEntity<Actor> saveActor(@RequestBody @Valid Actor actor){
        Actor actorCreado = actorService.saveActor(actor);
        return ResponseEntity.status(HttpStatus.CREATED).body(actorCreado);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateActor(@PathVariable Integer id, @RequestBody ActorDTO actorDTO){
        try{
            return ResponseEntity.ok(actorService.updateActor(id, actorDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActor(@PathVariable Integer id){
        try{
            actorService.deleteActor(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    
}
