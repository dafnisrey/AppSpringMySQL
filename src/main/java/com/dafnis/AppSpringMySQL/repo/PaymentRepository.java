package com.dafnis.AppSpringMySQL.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{

    public Page<Payment> findAll(Pageable pageable);
    public Page<Payment> findByCustomerId(Integer id, Pageable pageable);
    public Page<Payment> findByStaffId(Integer id, Pageable pageable);
    
}
