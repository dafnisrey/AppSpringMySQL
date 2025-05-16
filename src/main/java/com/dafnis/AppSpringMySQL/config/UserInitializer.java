package com.dafnis.AppSpringMySQL.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.dafnis.AppSpringMySQL.models.Usuario;
import com.dafnis.AppSpringMySQL.repo.UsuarioRepository;

@Component
public class UserInitializer implements CommandLineRunner{

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args)throws Exception{
        if(usuarioRepository.count() == 0){
            insertarUsuarios();
        }
    }

    private void insertarUsuarios(){
        List<Usuario> usuarios = List.of(
            new Usuario(null, "usuario1", passwordEncoder.encode("pass1"), Usuario.Tipo.normal),
            new Usuario(null, "usuario2", passwordEncoder.encode("pass2"), Usuario.Tipo.normal),
            new Usuario(null, "usuario3", passwordEncoder.encode("pass3"), Usuario.Tipo.normal),
            new Usuario(null, "admin1", passwordEncoder.encode("adminpass1"), Usuario.Tipo.admin),
            new Usuario(null, "admin2", passwordEncoder.encode("adminpass2"), Usuario.Tipo.admin));

        usuarioRepository.saveAll(usuarios);
    }
    
}
