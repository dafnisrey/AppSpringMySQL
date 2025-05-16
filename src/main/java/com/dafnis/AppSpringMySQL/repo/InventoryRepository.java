package com.dafnis.AppSpringMySQL.repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer>{

    public Page<Inventory> findAll(Pageable pageable);
    public List<Inventory> findByFilmId(Integer id);
    public Page<Inventory> findByStoreId(Integer id, Pageable pageable);

}