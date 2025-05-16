package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.ActorDTO;
import com.dafnis.AppSpringMySQL.models.Actor;
import com.dafnis.AppSpringMySQL.repo.ActorRepository;
import com.dafnis.AppSpringMySQL.repo.FilmActorRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class ActorService {
    
    private ActorRepository actorRepository;
    private FilmActorRepository filmActorRepository;
    private final EntityManager  entityManager;
    public ActorService(ActorRepository actorRepository, FilmActorRepository filmActorRepository, EntityManager entityManager){
        this.actorRepository = actorRepository;
        this.filmActorRepository = filmActorRepository;
        this.entityManager = entityManager;
    }

    public Page<Actor> getAll(int numPagina, int tamañoPagina){
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return actorRepository.findAll(pageable);
    }

    public Actor getActorById(Integer id)throws NoSuchElementException{
        Optional<Actor> opt = actorRepository.findById(id);
        Actor actor = null;
        if(opt.isPresent()){
            actor = opt.get();
            return actor;
        }
        throw new NoSuchElementException("Actor no encontrado.");
    }

    public List<Actor> getActorsByFirstName(String firstName){
        return actorRepository.findByFirstName(firstName);
    }

    @Transactional
    public Actor saveActor(Actor actor){
        Actor actorGuardado = actorRepository.save(actor);
        int id = actorGuardado.getId();
        entityManager.flush();
        entityManager.clear();
        return actorRepository.findById(id).orElse(null);
    }

    @Transactional
    public Actor updateActor(Integer id, ActorDTO actorDTO)throws NoSuchElementException{
        Optional<Actor> opt1 = actorRepository.findById(id);
        if(!opt1.isPresent()){
            throw new NoSuchElementException("No existe un actor con ese ID.");
        }
        Actor actor = opt1.get();
        if(actorDTO.getFirst_name() != null){
            actor.setFirstName(actorDTO.getFirst_name());
        }
        if(actorDTO.getLast_name() != null){
            actor.setLast_name(actorDTO.getLast_name());
        }
        actorRepository.save(actor);
        entityManager.flush();
        entityManager.clear();
        Optional<Actor> opt2 = actorRepository.findById(id);
        return opt2.get();
    }
    
    @Transactional
    public void deleteActor(Integer id)throws NoSuchElementException, InternalException{
        if(!actorRepository.existsById(id)){
            throw new NoSuchElementException("Actor no encontrado.");
        }
        if(filmActorRepository.existsByActorId(id)){
            throw new InternalException("Imposible eliminar. Tiene registros FilmActor asociados.");
        }
        actorRepository.deleteById(id);
    }
}
