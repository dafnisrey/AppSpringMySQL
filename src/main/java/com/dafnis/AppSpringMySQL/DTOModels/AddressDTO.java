package com.dafnis.AppSpringMySQL.DTOModels;

import lombok.Data;

@Data
public class AddressDTO {

    private String address;
    private String address2;
    private String district;
    private Integer city_id;
    private String postal_code;
    private String phone;
    

}
