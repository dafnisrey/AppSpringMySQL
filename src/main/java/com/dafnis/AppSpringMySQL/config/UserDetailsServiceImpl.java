package com.dafnis.AppSpringMySQL.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.models.Usuario;
import com.dafnis.AppSpringMySQL.repo.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Usuario usuario = usuarioRepository.findByUsername(username);
        if(usuario == null){
            throw new UsernameNotFoundException("Usuario no encontrado.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(usuario.getTipo() == Usuario.Tipo.admin){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else{
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));  
        }
        return new User(usuario.getUsername(), usuario.getPassword(), authorities);
    }
}
