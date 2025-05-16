package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.RentalDTO;
import com.dafnis.AppSpringMySQL.models.Customer;
import com.dafnis.AppSpringMySQL.models.Inventory;
import com.dafnis.AppSpringMySQL.models.Payment;
import com.dafnis.AppSpringMySQL.models.Rental;
import com.dafnis.AppSpringMySQL.models.Staff;
import com.dafnis.AppSpringMySQL.repo.CustomerRepository;
import com.dafnis.AppSpringMySQL.repo.InventoryRepository;
import com.dafnis.AppSpringMySQL.repo.RentalRepository;
import com.dafnis.AppSpringMySQL.repo.StaffRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class RentalService {

    private RentalRepository rentalRepository;
    private CustomerRepository customerRepository;
    private StaffRepository staffRepository;
    private InventoryRepository inventoryRepository;
    private EntityManager entityManager;
    public RentalService(RentalRepository rentalRepository, CustomerRepository customerRepository, 
                            StaffRepository staffRepository, InventoryRepository inventoryRepository, EntityManager entityManager){
        this.rentalRepository = rentalRepository;
        this.inventoryRepository = inventoryRepository;
        this.entityManager = entityManager;
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }
    
    public Page<Rental> getAll(int numPagina, int tamañoPagina){
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return rentalRepository.findAll(pageable);
    }

    public Rental getRentalById(Integer id){
        Optional<Rental> opt = rentalRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new NoSuchElementException("Rental no encontrado.");
    }

    public Page<Rental> getRentalsByCustomerId(Integer id, int numPagina, int tamañoPagina)throws NoSuchElementException{
        if(!customerRepository.existsById(id)){
            throw new NoSuchElementException("Customer no encontrado.");
        }
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return rentalRepository.findByCustomerId(id, pageable);
    }

    public Page<Rental> getRentalsByStaffId(Integer id, int numPagina, int tamañoPagina)throws NoSuchElementException{
        if(!staffRepository.existsById(id)){
            throw new NoSuchElementException("Staff no encontrado.");
        }
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return rentalRepository.findByStaffId(id, pageable);
    }
    
    public List<Payment> getPaymentslByRentalId(Integer id)throws NoSuchElementException{
        Optional<Rental> opt = rentalRepository.findById(id);
        if(opt.isPresent()){
            Rental rental = opt.get();
            return rental.getPayments();
        }
        throw new NoSuchElementException("Rental no encontrado.");
    }

    @Transactional
    public Rental saveRental(Rental rental)throws InternalException{
        Optional<Inventory> optInventory = inventoryRepository.findById(rental.getInventoryIdIntroducido());
        if(optInventory.isPresent()){
            rental.setInventory(optInventory.get());
        }else{
            throw new InternalException("Inventory no encontrado.");
        }
        Optional<Customer> optCustomer = customerRepository.findById(rental.getCustomerIdIntroducido());
        if(optCustomer.isPresent()){
            rental.setCustomer(optCustomer.get());
        }else{
            throw new InternalException("Customer no encontrado.");
        }
        Optional<Staff> optStaff = staffRepository.findById(rental.getStaffIdIntroducido());
        if(optStaff.isPresent()){
            rental.setStaff(optStaff.get());
        }else{
            throw new InternalException("Staff no encontrado.");
        }
        try{
            Rental rentalGuardado = rentalRepository.save(rental);
            entityManager.flush();
            entityManager.clear();
            Optional<Rental> opt = rentalRepository.findById(rentalGuardado.getId());
            if(opt.isPresent()){
                return opt.get();
            }
            throw new InternalException("Error al recuperar despues de guardar.");
        }catch(PersistenceException e){
            throw new InternalException("Error de persistencia.");
        }
    }

    @Transactional
    public Rental updateRental(Integer id, RentalDTO rentalDTO)throws NoSuchElementException, InternalException{
        Optional<Rental> optRental = rentalRepository.findById(id);
        if(!optRental.isPresent()){
            throw new NoSuchElementException("Rental no encontrado.");
        }
        Rental rental = optRental.get();
        if(rentalDTO.getCustomer_id() != null){
            rental.setCustomer(customerRepository.findById(rentalDTO.getCustomer_id()).orElseThrow(() -> new NoSuchElementException("Customer no encontrado.")));   
        }
        if(rentalDTO.getInventory_id() != null){
            rental.setInventory(inventoryRepository.findById(rentalDTO.getInventory_id()).orElseThrow(() -> new NoSuchElementException("Inventory no encontrado.")));
            
        }
        if(rentalDTO.getStaff_id() != null){
            rental.setStaff(staffRepository.findById(rentalDTO.getStaff_id()).orElseThrow(() -> new NoSuchElementException("Staff no encontrado.")));
        }
        if(rentalDTO.getReturn_date() != null){
            rental.setRental_date(rentalDTO.getReturn_date());
        }
        rentalRepository.save(rental);
        entityManager.flush();
        entityManager.clear();
        return rentalRepository.findById(id).orElseThrow(() -> new InternalException("Error al recuperar después de guardar."));
    }

    public void deleteRental(Integer id)throws NoSuchElementException, InternalException{
        Optional<Rental> opt = rentalRepository.findById(id);
        Rental rental = opt.orElseThrow(() -> new NoSuchElementException("Rental no encontrado."));
        List<Payment> listaPayments = rental.getPayments();
        if(!listaPayments.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Payment asociados.");
        }
        rentalRepository.deleteById(id);
    }

    
}
