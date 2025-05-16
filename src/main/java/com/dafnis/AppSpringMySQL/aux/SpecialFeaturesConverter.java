package com.dafnis.AppSpringMySQL.aux;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SpecialFeaturesConverter implements AttributeConverter<Set<String>, String> {
    public String convertToDatabaseColumn(Set<String> attribute){
        if(attribute == null || attribute.isEmpty()){
            return null;
        }
        return attribute.stream().collect(Collectors.joining(","));
    }

    public Set<String> convertToEntityAttribute(String dbData){
        if(dbData == null || dbData.isEmpty()){
            return null;
        }
        return new HashSet<>(Arrays.asList(dbData.split(",")));
    }
    
}
