package com.dafnis.AppSpringMySQL.aux;

import com.dafnis.AppSpringMySQL.models.Film.Rating;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String>{
    @Override
    public String convertToDatabaseColumn(Rating rating){
        if(rating == null){
            return null;
        }
        return rating.getData();
    }

    @Override
    public Rating convertToEntityAttribute(String dbValue){
        if(dbValue == null){
            return null;
        }
        String trimeado = dbValue.trim();
        for(Rating rating : Rating.values()){
            if(rating.getData().equalsIgnoreCase(trimeado)){
                return rating;
            }
        }
        throw new IllegalArgumentException("Rating desconocido: " + dbValue);
    }
    
}
