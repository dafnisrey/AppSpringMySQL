package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.CountryDTO;
import com.dafnis.AppSpringMySQL.models.City;
import com.dafnis.AppSpringMySQL.models.Country;
import com.dafnis.AppSpringMySQL.repo.CountryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class CountryService {

    private CountryRepository countryRepository;
    private EntityManager entityManager;
    public CountryService(CountryRepository countryRepository, EntityManager entityManager){
        this.countryRepository =countryRepository;
        this.entityManager = entityManager;
    }

    public Page<Country> getAll(int numPagina, int tamañoPagina){
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return countryRepository.findAll(pageable);
    }

    public Country getCountryById(Integer id)throws NoSuchElementException{
        Optional<Country> opt = countryRepository.findById(id);
        return opt.orElseThrow(() -> new NoSuchElementException("Country no encontrado."));
    }

    public List<City> getCitiesByCountryId(Integer id){
        Optional<Country> opt = countryRepository.findById(id);
        if(opt.isPresent()){
            Country country = opt.get();
            return country.getCities();
        }
        throw new NoSuchElementException("País no encontrado.");
    }

    @Transactional
    public Country saveCountry(Country country)throws InternalException{
        try{
            Country countryGuardado = countryRepository.save(country);
            entityManager.flush();
            entityManager.clear();
            Optional<Country> opt = countryRepository.findById(countryGuardado.getId());
            if(opt.isPresent()){
                return opt.get();
            }
            throw new InternalException("Error al recuperar despues de guardar.");
        }catch(PersistenceException e){
            throw new InternalException("Error de persistencia.");
        }
    }

    @Transactional
    public Country updateCountry(Integer id, CountryDTO countryDTO)throws NoSuchElementException, InternalException{
        Optional<Country> opt1 = countryRepository.findById(id);
        if(!opt1.isPresent()){
            throw new NoSuchElementException("Country no encontrado.");
        }
        Country country = opt1.get();
        country.setName(countryDTO.getName());
        countryRepository.save(country);
        entityManager.flush();
        entityManager.clear();
        Optional<Country> opt2 = countryRepository.findById(id);
        return opt2.orElseThrow(() -> new InternalException("Error al recuperar despues de guardar."));
    }

    public void deleteCountry(Integer id)throws NoSuchElementException, InternalException{
        Optional<Country> opt = countryRepository.findById(id);
        Country country = opt.orElseThrow(() -> new NoSuchElementException("Country no encontrado."));
        List<City> listaCities = country.getCities();
        if(!listaCities.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros City asociados.");
        }
        countryRepository.deleteById(id);
    }
    
}
