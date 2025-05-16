package com.dafnis.AppSpringMySQL.DTOModels;

import lombok.Data;

@Data
public class CustomerDTO {

    private String first_name;
    private String last_name;
    private String email;
    private Integer active;
    private Integer store_id;
    private Integer address_id;
    
}
