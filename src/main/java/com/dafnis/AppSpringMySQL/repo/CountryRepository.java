package com.dafnis.AppSpringMySQL.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Country;

public interface CountryRepository extends JpaRepository<Country, Integer>{
    Page<Country> findAll(Pageable pageable);
    
}
