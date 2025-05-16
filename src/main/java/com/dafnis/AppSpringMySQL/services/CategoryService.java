package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.CategoryDTO;
import com.dafnis.AppSpringMySQL.models.Category;
import com.dafnis.AppSpringMySQL.repo.CategoryRepository;
import com.dafnis.AppSpringMySQL.repo.FilmCategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {
    
    private CategoryRepository categoryRepository;
    private FilmCategoryRepository filmCategoryRepository;
    private EntityManager entityManager;
    public CategoryService(CategoryRepository categoryRepository, FilmCategoryRepository filmCategoryRepository, EntityManager entityManager){
        this.categoryRepository = categoryRepository;
        this.filmCategoryRepository = filmCategoryRepository;
        this.entityManager = entityManager;
    }

    public List<Category> getAll(){
        return categoryRepository.findAll();

    }

    @Transactional
    public Category saveCategory(Category category)throws InternalException{
        try{
            Category categoriaGuardada = categoryRepository.save(category);
            entityManager.flush();
            entityManager.clear();
            Optional<Category> opt = categoryRepository.findById(categoriaGuardada.getId());
            if(opt.isPresent()){
                return opt.get();
            }
            throw new InternalException("Error al guardar.");
        }catch(DataIntegrityViolationException e){
            throw new InternalException("Error al guardar. Violación de integridad de los datos.");
        }catch(PersistenceException e){
            throw new InternalException("Error al guardar. Problema de persistencia.");
        }
    }

    @Transactional
    public Category updateCategory(Integer id, CategoryDTO categoryDTO)throws NoSuchElementException, InternalException{
        Optional<Category> opt1 = categoryRepository.findById(id);
        if(!opt1.isPresent()){
            throw new NoSuchElementException("Categoria no encontrada.");
        }
        Category category = opt1.get();
        category.setName(categoryDTO.getName());
        categoryRepository.save(category);
        entityManager.flush();
        entityManager.clear();
        Optional<Category> opt2 = categoryRepository.findById(id);
        return opt2.orElseThrow(() -> new InternalException("Error al recuperar despues de guardar."));
    }

    @Transactional
    public void deleteCategory(Integer id)throws NoSuchElementException, InternalException{
        if(!categoryRepository.existsById(id)){
            throw new NoSuchElementException("Categoría no encontrada.");
        }
        if(filmCategoryRepository.existsByCategoryId(id)){
            throw new InternalException("Imposible eliminar. Tiene registros FilmCategory asociados.");
        }
        categoryRepository.deleteById(id);
    }
    
}
