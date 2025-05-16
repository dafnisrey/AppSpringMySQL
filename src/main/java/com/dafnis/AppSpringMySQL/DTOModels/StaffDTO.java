package com.dafnis.AppSpringMySQL.DTOModels;

import lombok.Data;

@Data
public class StaffDTO {

    private String first_name;
    private String last_name;
    private Integer address_id;
    private Integer store_id;
    private String email;
    private Integer active;
    private String username;
    private String password;
    
}
