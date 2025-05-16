package com.dafnis.AppSpringMySQL.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Debes proporcionar un username")
    @Column(unique = true, nullable = false)
    private String username;
    @NotBlank(message = "Debes proporcionar un password.")
    @Column(unique = true, nullable = false)
    private String password;
    @NotNull(message = "Debes proporcionar un tipo: normal o admin.")
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    public Usuario(Integer id, String username, String password, Tipo tipo){
        this.id = id;
        this.username = username;
        this.password = password;
        this.tipo = tipo;
    }

    public Usuario(){}

    public enum Tipo{
        normal,
        admin
    }  
}
