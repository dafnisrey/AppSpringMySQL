package com.dafnis.AppSpringMySQL.DTOModels;

import java.util.Date;
import lombok.Data;

@Data
public class RentalDTO {

    private Integer inventory_id;
    private Integer customer_id;
    private Integer staff_id;
    private Date return_date;
    
}
