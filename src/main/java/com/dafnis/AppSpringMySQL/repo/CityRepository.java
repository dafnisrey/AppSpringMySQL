package com.dafnis.AppSpringMySQL.repo;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.City;

public interface CityRepository extends JpaRepository<City, Integer>{
    Page<City> findAll(Pageable pageable);
    Optional<City> findByName(String city);
    
}
