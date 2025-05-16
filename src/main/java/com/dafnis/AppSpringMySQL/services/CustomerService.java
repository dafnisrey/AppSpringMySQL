package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.CustomerDTO;
import com.dafnis.AppSpringMySQL.models.Address;
import com.dafnis.AppSpringMySQL.models.Customer;
import com.dafnis.AppSpringMySQL.models.Payment;
import com.dafnis.AppSpringMySQL.models.Rental;
import com.dafnis.AppSpringMySQL.models.Store;
import com.dafnis.AppSpringMySQL.repo.AddressRepository;
import com.dafnis.AppSpringMySQL.repo.CustomerRepository;
import com.dafnis.AppSpringMySQL.repo.StoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
    private StoreRepository storeRepository;
    private EntityManager entityManager;
    private AddressRepository addressRepository;
    public CustomerService(CustomerRepository customerRepository, 
                            StoreRepository storeRepository, 
                            EntityManager entityManager,
                            AddressRepository addressRepository){
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.storeRepository = storeRepository;
        this.entityManager = entityManager;
    }

    public Page<Customer> getAll(int numPagina, int tamañoPagina){
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return customerRepository.findAll(pageable);
    }

    public Customer getCustomerById(Integer id)throws NoSuchElementException{
        Optional<Customer> opt = customerRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new NoSuchElementException("Customer no encontrado.");
    }

    public Page<Customer> getCustomersByStoreId(Integer id, int numPagina, int tamañoPagina)throws NoSuchElementException{
        if(!storeRepository.existsById(id)){
            throw new NoSuchElementException("Store no encontrada.");
        }
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return customerRepository.findByStoreId(id, pageable);
    }

    @Transactional
    public Customer saveCustomer(Customer customer)throws InternalException{
        Optional<Store> optStore = storeRepository.findById(customer.getStoreIdIntroducido());
        if(optStore.isPresent()){
            customer.setStore(optStore.get());
        }else{
            throw new InternalException("Store no encontrada.");
        }
        Optional<Address> optAddress = addressRepository.findById(customer.getAddressIdIntroducido());
        if(optAddress.isPresent()){
            customer.setAddress(optAddress.get());
        }else{
            throw new InternalException("Address no encontrada.");
        }
        try{   
            Customer customerGuardado = customerRepository.save(customer);
            entityManager.flush();
            entityManager.clear();
            Optional<Customer> optCustomer = customerRepository.findById(customerGuardado.getId());
            if(optCustomer.isPresent()){
                return optCustomer.get();
            }
            throw new InternalException("Error al recuperar después de guardar.");
        }catch(PersistenceException e){
            throw new InternalException("Error de persistencia.");
        }
    }

    @Transactional
    public Customer updateCustomer(Integer id, CustomerDTO customerDTO)throws NoSuchElementException, InternalException{
        Optional<Customer> opt1 = customerRepository.findById(id);
        if(!opt1.isPresent()){
            throw new NoSuchElementException("Customer no encontrado.");
        }
        Customer customer = opt1.get();
        if(customerDTO.getFirst_name() != null){
            customer.setFirst_name(customerDTO.getFirst_name());
        }
        if(customerDTO.getLast_name() != null){
            customer.setLast_name(customerDTO.getLast_name());
        }
        if(customerDTO.getActive() != null){
            customer.setActive(customerDTO.getActive());
        }
        if(customerDTO.getEmail() != null){
            customer.setEmail(customerDTO.getEmail());
        }
        if(customerDTO.getAddress_id() != null){
            Optional<Address> opt2 = addressRepository.findById(customerDTO.getAddress_id());
            if(opt2.isPresent()){
                customer.setAddress(opt2.get());
            }else{
                throw new NoSuchElementException("Address no encontrada.");
            }
        }
        if(customerDTO.getStore_id() != null){
            Optional<Store> opt3 = storeRepository.findById(customerDTO.getStore_id());
            if(opt3.isPresent()){
                customer.setStore(opt3.get());
            }else{
                throw new NoSuchElementException("Store no encontrada.");
            }
        }
        customerRepository.save(customer);
        entityManager.flush();
        entityManager.clear();
        Optional<Customer> opt4 = customerRepository.findById(id);
        return opt4.orElseThrow(() -> new InternalException("Error al recuperar después de guardar."));
    }

    public void deleteCustomer(Integer id)throws NoSuchElementException, InternalException{
        Optional<Customer> opt = customerRepository.findById(id);
        Customer customer = opt.orElseThrow(() -> new NoSuchElementException("Customer no encontrado."));
        List<Rental> listaRentals = customer.getRentals();
        if(!listaRentals.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Rental asociados.");
        }
        List<Payment> listaPayments = customer.getPayments();
        if(!listaPayments.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Payment asociados.");
        }
        customerRepository.deleteById(id);
    }
    
}
