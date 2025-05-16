package com.dafnis.AppSpringMySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

    
}
