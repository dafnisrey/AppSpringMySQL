package com.dafnis.AppSpringMySQL.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Actor;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Page<Actor> findAll(Pageable pageable);
    List<Actor> findByFirstName(String firstName);

}
