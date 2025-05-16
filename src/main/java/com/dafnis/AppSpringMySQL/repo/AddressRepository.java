package com.dafnis.AppSpringMySQL.repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>{
    
    Page<Address> findAll(Pageable pageable);
    List<Address> findByCityId(Integer id);
}
