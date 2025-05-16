package com.dafnis.AppSpringMySQL.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.FilmActor;
import com.dafnis.AppSpringMySQL.models.FilmActorId;

public interface FilmActorRepository extends JpaRepository<FilmActor, FilmActorId>{

    public List<FilmActor> findByFilmId(Integer id);
    public List<FilmActor> findByActorId(Integer id);
    public void deleteByActorId(Integer id);
    public void deleteByFilmId(Integer id);
    public boolean existsByActorId(Integer id);
    public boolean existsByFilmId(Integer id);
    
}
