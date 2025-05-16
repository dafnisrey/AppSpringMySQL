package com.dafnis.AppSpringMySQL.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Rental;

public interface RentalRepository extends JpaRepository<Rental, Integer>{
    public Page<Rental> findAll(Pageable pageable);
    public Page<Rental> findByCustomerId(Integer id, Pageable pageable);
    public Page<Rental> findByStaffId(Integer id, Pageable pageable);
}
