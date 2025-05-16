package com.dafnis.AppSpringMySQL.DTOModels;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentDTO {

    private BigDecimal amount;
    private Integer customer_id;
    private Integer staff_id;
    private Integer rental_id;
    
}
