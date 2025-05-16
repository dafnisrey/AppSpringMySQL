package com.dafnis.AppSpringMySQL.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "actor_info")
public class ActorInfo {

    @Id
    private Integer actor_id;
    private String first_name;
    private String last_name;
    private String film_info;

    
}
