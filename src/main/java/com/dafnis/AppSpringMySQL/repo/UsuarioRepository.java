package com.dafnis.AppSpringMySQL.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dafnis.AppSpringMySQL.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

    public Usuario findByUsername(String username);
}
