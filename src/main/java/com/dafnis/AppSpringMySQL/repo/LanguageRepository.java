package com.dafnis.AppSpringMySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Language;

public interface LanguageRepository extends JpaRepository<Language, Integer>{

    
}
