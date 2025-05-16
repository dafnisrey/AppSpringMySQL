package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.AddressDTO;
import com.dafnis.AppSpringMySQL.models.Address;
import com.dafnis.AppSpringMySQL.models.City;
import com.dafnis.AppSpringMySQL.models.Customer;
import com.dafnis.AppSpringMySQL.models.Store;
import com.dafnis.AppSpringMySQL.repo.AddressRepository;
import com.dafnis.AppSpringMySQL.repo.CityRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class AddressService {
    
    private AddressRepository addressRepository;
    private CityRepository cityRepository;
    private GeometryFactory geometryFactory;
    private EntityManager entityManager;
    public AddressService(EntityManager entityManager, AddressRepository addressRepository, CityRepository cityRepository, GeometryFactory geometryFactory){
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.geometryFactory = geometryFactory;
        this.entityManager = entityManager;
    }

    public Page<Address> getAll(int numPagina, int tamañoPagina){
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return addressRepository.findAll(pageable);
    }

    public Address getAddressById(Integer id)throws NoSuchElementException{
        Optional<Address> opt = addressRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new NoSuchElementException("Address no encontrada.");
    }

    public List<Store> getStoresByAddressId(Integer id)throws NoSuchElementException{
        Optional<Address> opt = addressRepository.findById(id);
        if(opt.isPresent()){
            Address address = opt.get();
            return address.getStores();
        }
        throw new NoSuchElementException("Address no encontrada.");
    }

    public List<Customer> getCustomersByAddressId(Integer id)throws NoSuchElementException{
        Optional<Address> opt = addressRepository.findById(id);
        if(opt.isPresent()){
            Address address = opt.get();
            return address.getCustomers();
        }
        throw new NoSuchElementException("Address no encontrada.");
    }

    @Transactional
    public Address saveAddress(Address address)throws IllegalArgumentException{
        boolean ciudadExiste = cityRepository.existsById(address.getCityIdIntroducido());
        if(ciudadExiste){
            Optional<City> opt = cityRepository.findById(address.getCityIdIntroducido());
            address.setCity(opt.get());
            if(address.getLatitud() != null && address.getLongitud() != null){
                Point location = geometryFactory.createPoint(new Coordinate(address.getLongitud(), address.getLatitud()));
                location.setSRID(0);
                address.setLocation(location);
                Address addressGuardada = addressRepository.save(address);
                entityManager.flush();
                entityManager.clear();
                return addressRepository.findById(addressGuardada.getId()).orElse(null);
            }
            throw new IllegalArgumentException("Debes proporcionar latitud y longitud.");
        }
        throw new IllegalArgumentException("Ciudad no encontrada.");
    }

    @Transactional
    public Address updateAddress(Integer id, AddressDTO addressDTO)throws InternalException, NoSuchElementException{
        Optional<Address> opt1 = addressRepository.findById(id);
        if(!opt1.isPresent()){
            throw new NoSuchElementException("Address no encontrada.");
        }
        Address address = opt1.get();
        if(addressDTO.getAddress() != null){
            address.setAddress(addressDTO.getAddress());
        }
        if(addressDTO.getAddress2() != null){
            address.setAddress2(addressDTO.getAddress2());
        }
        if(addressDTO.getCity_id() != null){
            Optional<City> opt2 = cityRepository.findById(addressDTO.getCity_id());
            if(opt2.isPresent()){
                address.setCity(opt2.get());
            }else{
                throw new NoSuchElementException("Ciudad no encontrada.");
            }
        }
        if(addressDTO.getDistrict() != null){
            address.setDistrict(addressDTO.getDistrict());
        }
        if(addressDTO.getPostal_code() != null){
            address.setPostal_code(addressDTO.getPostal_code());
        }
        if(addressDTO.getPhone() != null){
            address.setPhone(addressDTO.getPhone());
        }
        addressRepository.save(address);
        entityManager.flush();
        entityManager.clear();
        Optional<Address> opt3 = addressRepository.findById(id);
        return opt3.orElseThrow(() -> new InternalException("Error al recuperar despues de guardar."));
    }

    public void deleteAddress(Integer id)throws NoSuchElementException, InternalException{
        Optional<Address> opt = addressRepository.findById(id);
        Address address = opt.orElseThrow(() -> new NoSuchElementException("Address no encontrada."));
        List<Customer> listaCustomers = address.getCustomers();
        if(!listaCustomers.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Customer asociados.");
        }
        addressRepository.deleteById(id);
    }
    
}
