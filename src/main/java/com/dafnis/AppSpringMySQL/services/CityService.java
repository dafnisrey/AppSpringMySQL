package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.CityDTO;
import com.dafnis.AppSpringMySQL.models.Address;
import com.dafnis.AppSpringMySQL.models.City;
import com.dafnis.AppSpringMySQL.models.Country;
import com.dafnis.AppSpringMySQL.repo.CityRepository;
import com.dafnis.AppSpringMySQL.repo.CountryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class CityService {

    private CityRepository cityRepository;
    private EntityManager entityManager;
    private CountryRepository countryRepository;
    public CityService(CityRepository cityRepository, EntityManager entityManager, CountryRepository countryRepository){
        this.cityRepository = cityRepository;
        this.entityManager = entityManager;
        this.countryRepository = countryRepository;
    }

    public Page<City> getAll(int numPagina, int tamañoPagina){
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return cityRepository.findAll(pageable);
    }

    public City getCityById(Integer id)throws NoSuchElementException{
        Optional<City> opt = cityRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new NoSuchElementException("Ciudad no encontrada.");
    }

    public City getCityByName(String name)throws NoSuchElementException{
        Optional<City> opt = cityRepository.findByName(name);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new NoSuchElementException("Ciudad no encontrada.");
    }

    public List<Address> getAddressesByCityId(Integer id){
        Optional<City> opt = cityRepository.findById(id);
        if(opt.isPresent()){
            City city = opt.get();
            return city.getAddresses();
        }
        throw new NoSuchElementException("Ciudad no encontrada.");
    }

    @Transactional
    public City saveCity(City city)throws IllegalArgumentException, InternalException{
        try{
            Optional<Country> optCountry = countryRepository.findById(city.getCountryIdIntroducido());
            if(optCountry.isPresent()){
                city.setCountry(optCountry.get());
                City ciudadGuardada = cityRepository.save(city);
                entityManager.flush();
                entityManager.clear();
                Optional<City> optCity = cityRepository.findById(ciudadGuardada.getId());
                if(optCity.isPresent()){
                    return optCity.get();
                }
                throw new InternalException("Error al recuperar tras guardar.");
            }
            throw new IllegalArgumentException("Ciudad no encontrada.");
        }catch(PersistenceException e){
            throw new InternalException("Error al guardar. Problema de persistencia.");
        }
    }

    @Transactional
    public City updateCity(Integer id, CityDTO cityDTO)throws NoSuchElementException, InternalException{
        Optional<City> opt1 = cityRepository.findById(id);
        if(!opt1.isPresent()){
            throw new NoSuchElementException("Ciudad no encontrada.");
        }
        City city = opt1.get();
        if(cityDTO.getName() != null){
            city.setName(cityDTO.getName());
        }
        if(cityDTO.getCountry_id() != null){
            Optional<Country> opt2 = countryRepository.findById(cityDTO.getCountry_id());
            if(opt2.isPresent()){
                city.setCountry(opt2.get());
            }else{
                throw new NoSuchElementException("Country no encontrado.");
            }
        }
        cityRepository.save(city);
        entityManager.flush();
        entityManager.clear();
        return cityRepository.findById(id).orElseThrow(() -> new InternalException("Error al recuperar despues de guardar."));
    }

    public void deleteCity(Integer id)throws NoSuchElementException, InternalException{
        Optional<City> opt = cityRepository.findById(id);
        City city = opt.orElseThrow(() -> new NoSuchElementException("City no encontrada."));
        List<Address> listaAddresses = city.getAddresses();
        if(!listaAddresses.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Address asociados.");
        }
        cityRepository.deleteById(id);
    }
    
}
