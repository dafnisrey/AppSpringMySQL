package com.dafnis.AppSpringMySQL.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.ActorInfo;

public interface ActorInfoRepository extends JpaRepository<ActorInfo, Integer> {

    Page<ActorInfo> findAll(Pageable pageable);

}
