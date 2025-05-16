package com.dafnis.AppSpringMySQL.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.FilmCategory;
import com.dafnis.AppSpringMySQL.models.FilmCategoryId;

public interface FilmCategoryRepository extends JpaRepository<FilmCategory, FilmCategoryId>{

    public FilmCategory findByFilmId(Integer id);
    public Page<FilmCategory> findByCategoryId(Integer id, Pageable pageable);
    public boolean existsByCategoryId(Integer id);
    public boolean existsByFilmId(Integer id);
    public void deleteByCategoryId(Integer id);
    public void deleteByFilmId(Integer id);
    
}
