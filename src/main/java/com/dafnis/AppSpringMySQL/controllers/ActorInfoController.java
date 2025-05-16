package com.dafnis.AppSpringMySQL.controllers;

import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dafnis.AppSpringMySQL.models.ActorInfo;
import com.dafnis.AppSpringMySQL.services.ActorInfoService;

@RestController
@RequestMapping("/actorinfo")
public class ActorInfoController {
    
    private ActorInfoService actorInfoService;
    private ActorInfoController(ActorInfoService actorInfoService){
        this.actorInfoService = actorInfoService;
    }

    @GetMapping
    public ResponseEntity<Page<ActorInfo>> getAll(@RequestParam int numPagina, @RequestParam int tamañoPagina){
        return ResponseEntity.ok(actorInfoService.getAll(numPagina, tamañoPagina));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getActorInfoById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(actorInfoService.geActorInfoById(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
}
