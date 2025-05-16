package com.dafnis.AppSpringMySQL.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.management.relation.RelationNotFoundException;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.models.Category;
import com.dafnis.AppSpringMySQL.models.Film;
import com.dafnis.AppSpringMySQL.models.FilmCategory;
import com.dafnis.AppSpringMySQL.models.FilmCategoryId;
import com.dafnis.AppSpringMySQL.repo.CategoryRepository;
import com.dafnis.AppSpringMySQL.repo.FilmCategoryRepository;
import com.dafnis.AppSpringMySQL.repo.FilmRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class FilmCategoryService {
    
    private FilmCategoryRepository filmCategoryRepository;
    private CategoryRepository categoryRepository;
    private FilmRepository filmRepository;
    private EntityManager entityManager;
    public FilmCategoryService(FilmCategoryRepository filmCategoryRepository, CategoryRepository categoryRepository, 
                                EntityManager entityManager, FilmRepository filmRepository){
        this.filmCategoryRepository = filmCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.entityManager = entityManager;
        this.filmRepository = filmRepository;
    }

    public Category getCategoriesByFilmId(Integer filmId){
        FilmCategory filmCategory = filmCategoryRepository.findByFilmId(filmId);
        return filmCategory.getCategory();
    }

    public List<Film> getFilmsByCategoryId(Integer categoryId, int numPagina, int tamañoPagina)throws NoSuchElementException{
        if(!categoryRepository.existsById(categoryId)){
            throw new NoSuchElementException("Category no encontrada.");
        }
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        Page<FilmCategory> page = filmCategoryRepository.findByCategoryId(categoryId, pageable);
        List<FilmCategory> listaInicial = new ArrayList<>();
        if(page != null && page.hasContent()){
            listaInicial = page.getContent();
        }
        List<Film> lista = new ArrayList<>();
        for(FilmCategory filmCategory : listaInicial){
            lista.add(filmCategory.getFilm());
        }
        return lista;
    }

    @Transactional
    public FilmCategory saveFilmCategory(FilmCategory filmCategory)throws InternalException{
        Optional<Film> optFilm = filmRepository.findById(filmCategory.getId().getFilmId());
        if(optFilm.isPresent()){
            filmCategory.setFilm(optFilm.get());
        }else{
            throw new InternalException("Film no encontrada.");
        }
        Optional<Category> optCategory = categoryRepository.findById(filmCategory.getId().getCategoryId());
        if(optCategory.isPresent()){
            filmCategory.setCategory(optCategory.get());
        }else{
            throw new InternalException("Category no encontrada.");
        }
        try{
            FilmCategory filmCategoryGuardada = filmCategoryRepository.save(filmCategory);
            entityManager.flush();
            entityManager.clear();
            Optional<FilmCategory> opt = filmCategoryRepository.findById(filmCategoryGuardada.getId());
            if(opt.isPresent()){
                return opt.get();
            }
            throw new InternalException("Error al recuperar después de guardar.");
        }catch(PersistenceException e){
            throw new InternalException("Error de persistencia.");
        }
    }

    @Transactional
    public FilmCategory updateFilmCategory(Integer oldFilmId, Integer oldCategoryId, Integer newFilmId, Integer newCategoryId)throws RelationNotFoundException, InternalException{
        FilmCategoryId oldId = new FilmCategoryId(oldFilmId, oldCategoryId);
        if(!filmCategoryRepository.existsById(oldId)){
            throw new RelationNotFoundException("Esa relación no existe.");
        }
        FilmCategoryId newId = new FilmCategoryId(newFilmId, newCategoryId);
        if(filmCategoryRepository.existsById(newId)){
            throw new InternalException("Ese Film y esa Category ya estan vinculados.");
        }
        if(!filmRepository.existsById(newFilmId)){
            throw new NoSuchElementException("Pelicula no encontrada.");
        }
        if(!categoryRepository.existsById(newCategoryId)){
            throw new NoSuchElementException("Category no encontrada.");
        }
        filmCategoryRepository.deleteById(oldId);
        FilmCategory newFilmCategory = new FilmCategory();
        newFilmCategory.setId(newId);
        newFilmCategory.setFilm(filmRepository.findById(newFilmId).get());
        newFilmCategory.setCategory(categoryRepository.findById(newCategoryId).get());
        return filmCategoryRepository.save(newFilmCategory);
    }

    @Transactional
    public void deleteFilmCategoryByCategoryId(Integer id)throws NoSuchElementException{
        if(filmCategoryRepository.existsByCategoryId(id)){
            filmCategoryRepository.deleteByCategoryId(id);
        }else{
            throw new NoSuchElementException("No hay filmCategory con ese category_id.");
        }
    }

    @Transactional
    public void deleteFilmCategoryByFilmId(Integer id)throws NoSuchElementException{
        if(filmCategoryRepository.existsByFilmId(id)){
            filmCategoryRepository.deleteByFilmId(id);
        }else{
            throw new NoSuchElementException("No hay filmCategory con ese film_id.");
        }
    }

}
