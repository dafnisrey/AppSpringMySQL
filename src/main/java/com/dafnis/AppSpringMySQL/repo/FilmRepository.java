package com.dafnis.AppSpringMySQL.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dafnis.AppSpringMySQL.models.Film;

public interface FilmRepository extends JpaRepository<Film, Integer>{

    Page<Film> findAll(Pageable pageable);
    Page<Film> findByLanguageId(Integer id, Pageable pageable);  

    
}
