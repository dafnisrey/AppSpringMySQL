package com.dafnis.AppSpringMySQL.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

    Page<Customer> findAll(Pageable pageable);
    Page<Customer> findByStoreId(Integer id, Pageable pageable);
    
}
