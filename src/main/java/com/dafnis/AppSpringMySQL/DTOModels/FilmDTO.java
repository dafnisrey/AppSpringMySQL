package com.dafnis.AppSpringMySQL.DTOModels;

import java.math.BigDecimal;
import java.time.Year;
import lombok.Data;

@Data
public class FilmDTO {

    private String title;
    private String description;
    private Year release_year;
    private Integer language_id;
    private Integer original_language_id;
    private Integer rental_duration;
    private BigDecimal rental_rate;
    private Integer length;
    private BigDecimal replacement_cost;
    
}
