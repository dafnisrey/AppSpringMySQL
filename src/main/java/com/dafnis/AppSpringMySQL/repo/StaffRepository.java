package com.dafnis.AppSpringMySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Staff;

public interface StaffRepository extends JpaRepository<Staff, Integer>{
    
}
