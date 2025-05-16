package com.dafnis.AppSpringMySQL.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.management.relation.RelationNotFoundException;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.models.Actor;
import com.dafnis.AppSpringMySQL.models.Film;
import com.dafnis.AppSpringMySQL.models.FilmActor;
import com.dafnis.AppSpringMySQL.models.FilmActorId;
import com.dafnis.AppSpringMySQL.repo.ActorRepository;
import com.dafnis.AppSpringMySQL.repo.FilmActorRepository;
import com.dafnis.AppSpringMySQL.repo.FilmRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class FilmActorService {

    private FilmActorRepository filmActorRepository;
    private FilmRepository filmRepository;
    private ActorRepository actorRepository;
    private EntityManager entityManager;
    public FilmActorService(FilmActorRepository filmActorRepository, FilmRepository filmRepository,
                                ActorRepository actorRepository, EntityManager entityManager){
        this.filmActorRepository = filmActorRepository;
        this.actorRepository = actorRepository;
        this.filmRepository = filmRepository;
        this.entityManager = entityManager;
    }

    public List<Actor> getActorsByFilmId(Integer filmId){
        List<Actor> lista = new ArrayList<>();
        List<FilmActor> listaFilmActors = filmActorRepository.findByFilmId(filmId);
        for(FilmActor filmActor : listaFilmActors){
            lista.add(filmActor.getActor());
        }
        return lista;
    }

    public List<Film> getFilmsByActorId(Integer actorId){
        List<Film> lista = new ArrayList<>();
        List<FilmActor> listaFilmActors = filmActorRepository.findByActorId(actorId);
        for(FilmActor filmActor : listaFilmActors){
            lista.add(filmActor.getFilm());
        }
        return lista;
    }

    public List<FilmActor> getFilmActorsByActorId(Integer actorId){
        return filmActorRepository.findByActorId(actorId);
    }

    public List<FilmActor> getFilmActorsByFilmId(Integer filmId){
        return filmActorRepository.findByFilmId(filmId);
    }

    @Transactional
    public FilmActor saveFilmActor(FilmActor filmActor)throws InternalException{
        Optional<Film> optFilm = filmRepository.findById(filmActor.getId().getFilmId());
        if(optFilm.isPresent()){
            filmActor.setFilm(optFilm.get());
        }else{
            throw new InternalException("Film no encontrada.");
        }
        Optional<Actor> optActor = actorRepository.findById(filmActor.getId().getActorId());
        if(optActor.isPresent()){
            filmActor.setActor(optActor.get());
        }else{
            throw new InternalException("Actor no encontrado.");
        }
        try{
            FilmActor filmActorGuardado = filmActorRepository.save(filmActor);
            entityManager.flush();
            entityManager.clear();
            Optional<FilmActor> opt = filmActorRepository.findById(filmActorGuardado.getId());
            return opt.get();
        }catch(PersistenceException e){
            throw new InternalException("Error de persistencia.");
        }
    }

    @Transactional
    public FilmActor updateFilmActor(Integer oldFilmId, Integer oldActorId, Integer newFilmId, Integer newActorId)throws RelationNotFoundException, InternalException{
        FilmActorId oldId = new FilmActorId(oldFilmId, oldActorId);
        if(!filmActorRepository.existsById(oldId)){
            throw new RelationNotFoundException("Esa relación no existe.");
        }
        FilmActorId newId = new FilmActorId(newFilmId, newActorId);
        if(filmActorRepository.existsById(newId)){
            throw new InternalException("Ese Film y ese Actor ya estan vinculados.");
        }
        if(!filmRepository.existsById(newFilmId)){
            throw new NoSuchElementException("Pelicula no encontrada.");
        }
        if(!actorRepository.existsById(newActorId)){
            throw new NoSuchElementException("Actor no encontrado.");
        }
        filmActorRepository.deleteById(oldId);
        FilmActor newFilmActor = new FilmActor();
        newFilmActor.setId(newId);
        newFilmActor.setFilm(filmRepository.findById(newFilmId).get());
        newFilmActor.setActor(actorRepository.findById(newActorId).get());
        return filmActorRepository.save(newFilmActor);
    }

    @Transactional
    public void deleteFilmActorByActorId(Integer id)throws NoSuchElementException{
        if(filmActorRepository.existsByActorId(id)){
            filmActorRepository.deleteByActorId(id);
        }else{
            throw new NoSuchElementException("No hay filmactors con ese actor_id.");
        }
    }

    @Transactional
    public void deleteFilmActorByFilmId(Integer id)throws NoSuchElementException{
        if(filmActorRepository.existsByFilmId(id)){
            filmActorRepository.deleteByFilmId(id);
        }else{
            throw new NoSuchElementException("No hay filmactors con ese film_id.");
        }
    }
    
}
