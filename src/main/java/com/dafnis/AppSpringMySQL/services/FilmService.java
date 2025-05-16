package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.FilmDTO;
import com.dafnis.AppSpringMySQL.models.Film;
import com.dafnis.AppSpringMySQL.models.Inventory;
import com.dafnis.AppSpringMySQL.models.Language;
import com.dafnis.AppSpringMySQL.repo.FilmActorRepository;
import com.dafnis.AppSpringMySQL.repo.FilmCategoryRepository;
import com.dafnis.AppSpringMySQL.repo.FilmRepository;
import com.dafnis.AppSpringMySQL.repo.LanguageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class FilmService {

    private FilmRepository filmRepository;
    private LanguageRepository languageRepository;
    private FilmActorRepository filmActorRepository;
    private FilmCategoryRepository filmCategoryRepository;
    private EntityManager entityManager;
    public FilmService(FilmRepository filmRepository, LanguageRepository languageRepository, 
                            FilmActorRepository filmActorRepository, FilmCategoryRepository filmCategoryRepository, EntityManager enttEntityManager){
        this.filmRepository = filmRepository;
        this.entityManager = enttEntityManager;
        this.languageRepository = languageRepository;
        this.filmActorRepository = filmActorRepository;
        this.filmCategoryRepository = filmCategoryRepository;
    }

    public Page<Film> getAll(int numPagina, int tamañoPagina){
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return filmRepository.findAll(pageable);
    }

    public Film getFilmById(Integer filmId){
        Optional<Film> opt = filmRepository.findById(filmId);
        if(opt.isPresent()){
            return opt.get();
        }else{
            throw new NoSuchElementException("Película no encontrada.");
        }
    }

    public Page<Film> getFilmsByLanguageId(Integer id, int numPagina, int tamañoPagina)throws NoSuchElementException{
        if(!languageRepository.existsById(id)){
            throw new NoSuchElementException("Idioma no encontrado.");
        }
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return filmRepository.findByLanguageId(id, pageable);
    }

    @Transactional
    public Film saveFilm(Film film)throws InternalException{
        Optional<Language> optLanguage = languageRepository.findById(film.getLanguageIdIntroducido());
        if(optLanguage.isPresent()){
            film.setLanguage(optLanguage.get());
        }else{
            throw new InternalException("Idioma no encontrado.");
        }
        try{
            Film filmGuardado = filmRepository.save(film);
            entityManager.flush();
            entityManager.clear();
            Optional<Film> optFilm = filmRepository.findById(filmGuardado.getId());
            if(optFilm.isPresent()){
                return optFilm.get();
            }
            throw new InternalException("Error al recuperar despues de guardar.");
        }catch(PersistenceException e){
            throw new InternalException("Error de persistencia.");
        }
    }

    @Transactional
    public Film updateFilm(Integer id, FilmDTO filmDTO)throws NoSuchElementException, InternalException{
        Optional<Film> opt1 = filmRepository.findById(id);
        if(!opt1.isPresent()){
            throw new NoSuchElementException("Película no encontrada.");
        }
        Film film = opt1.get();
        if(filmDTO.getTitle() != null){
            film.setTitle(filmDTO.getTitle());
        }
        if(filmDTO.getDescription() != null){
            film.setDescription(filmDTO.getDescription());
        }
        if(filmDTO.getLength() != null){
            film.setLength(filmDTO.getLength());
        }
        if(filmDTO.getRelease_year() != null){
            film.setRelease_year(filmDTO.getRelease_year());
        }
        if(filmDTO.getRental_duration() != null){
            film.setRental_duration(filmDTO.getRental_duration());
        }
        if(filmDTO.getRental_rate() != null){
            film.setRental_rate(filmDTO.getRental_rate());
        }
        if(filmDTO.getReplacement_cost() != null){
            film.setReplacement_cost(filmDTO.getReplacement_cost());
        }
        if(filmDTO.getOriginal_language_id() != null){
            film.setOriginal_language_id(filmDTO.getOriginal_language_id());
        }
        if(filmDTO.getLanguage_id() != null){
            Optional<Language> optLang = languageRepository.findById(filmDTO.getLanguage_id());
            if(optLang.isPresent()){
                film.setLanguage(optLang.get());
            }else{
                throw new NoSuchElementException("Language no encontrado.");
            }
        }
        filmRepository.save(film);
        entityManager.flush();
        entityManager.clear();
        Optional<Film> optFilm = filmRepository.findById(id);
        return optFilm.orElseThrow(() -> new InternalException("Error al recuperar después de guardar."));
    }

    @Transactional
    public void deleteFilm(Integer id)throws NoSuchElementException, InternalException{
        Optional<Film> opt = filmRepository.findById(id);
        Film film = opt.orElseThrow(() -> new NoSuchElementException("Película no encontrada."));
        List<Inventory> listaInventories = film.getInventories();
        if(!listaInventories.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Inventory asociados.");
        }
        if(filmCategoryRepository.existsByFilmId(id)){
            throw new InternalException("Imposible eliminar. Tiene registros FilmCategory asociados.");
        }
        if(filmActorRepository.existsByFilmId(id)){
            throw new InternalException("Imposible eliminar. Tiene registros FilmActor asociados.");
        }
        filmRepository.deleteById(id);
    }

    
}
