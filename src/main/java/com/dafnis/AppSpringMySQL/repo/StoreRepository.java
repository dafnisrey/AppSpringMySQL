package com.dafnis.AppSpringMySQL.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Staff;
import com.dafnis.AppSpringMySQL.models.Store;

public interface StoreRepository extends JpaRepository<Store, Integer>{

    public Optional<Store> findByStaff(Staff staff);
    
}
