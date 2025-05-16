package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.StaffDTO;
import com.dafnis.AppSpringMySQL.models.Address;
import com.dafnis.AppSpringMySQL.models.Payment;
import com.dafnis.AppSpringMySQL.models.Rental;
import com.dafnis.AppSpringMySQL.models.Staff;
import com.dafnis.AppSpringMySQL.models.Store;
import com.dafnis.AppSpringMySQL.repo.AddressRepository;
import com.dafnis.AppSpringMySQL.repo.StaffRepository;
import com.dafnis.AppSpringMySQL.repo.StoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class StaffService {
    
    private StaffRepository staffRepository;
    private AddressRepository addressRepository;
    private StoreRepository storeRepository;
    private EntityManager entityManager;
    public StaffService(StaffRepository staffRepository, AddressRepository addressRepository,
                            StoreRepository storeRepository, EntityManager entityManager){
        this.staffRepository = staffRepository;
        this.addressRepository = addressRepository;
        this.storeRepository = storeRepository;
        this.entityManager = entityManager;
    }

    public List<Staff> getAll(){
        return staffRepository.findAll();
    }

    public Staff getStaffById(Integer id)throws NoSuchElementException{
        Optional<Staff> opt = staffRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new NoSuchElementException("Staff no encontrado.");
    }

    public List<Store> getStoresByStaffId(Integer id)throws NoSuchElementException{
        Optional<Staff> opt = staffRepository.findById(id);
        if(opt.isPresent()){
            Staff staff = opt.get();
            return staff.getStores();
        }
        throw new NoSuchElementException("Staff no encontrado.");
    }

    @Transactional
    public Staff saveStaff(Staff staff)throws InternalException, NoSuchElementException{
        Optional<Address> optAddress = addressRepository.findById(staff.getAddressIdIntroducido());
        staff.setAddress(optAddress.orElseThrow(() -> new NoSuchElementException("Address no encontrada.")));
        Optional<Store> optStore = storeRepository.findById(staff.getStoreIdIntroducido());
        staff.setStore(optStore.orElseThrow(() -> new NoSuchElementException("Store no encontrada.")));
        try{
            Staff staffGuardado = staffRepository.save(staff);
            entityManager.flush();
            entityManager.clear();
            Optional<Staff> opt = staffRepository.findById(staffGuardado.getId());
            if(opt.isPresent()){
                return opt.get();
            }
            throw new InternalException("Error al recuperar después de guardar.");
        }catch(PersistenceException e){
            throw new InternalException("Error de persistencia.");
        }
    }

    @Transactional
    public Staff updateStaff(Integer id, StaffDTO staffDTO)throws NoSuchElementException, InternalException, PersistenceException{
        Optional<Staff> optStaff = staffRepository.findById(id);
        if(!optStaff.isPresent()){
            throw new NoSuchElementException("Staff no encontrado.");
        }
        Staff staff = optStaff.get();
        if(staffDTO.getAddress_id() != null){
            staff.setAddress(addressRepository.findById(staffDTO.getAddress_id()).orElseThrow(() -> new NoSuchElementException("Address no encontrada.")));
        }
        if(staffDTO.getStore_id() != null){
            staff.setStore(storeRepository.findById(staffDTO.getStore_id()).orElseThrow(() -> new NoSuchElementException("Store no encontrada.")));
        }
        if(staffDTO.getActive() != null){
            staff.setActive(staffDTO.getActive());
        }
        if(staffDTO.getEmail() != null){
            staff.setEmail(staffDTO.getEmail());
        }
        if(staffDTO.getFirst_name() != null){
            staff.setFirst_name(staffDTO.getFirst_name());
        }
        if(staffDTO.getLast_name() != null){
            staff.setLast_name(staffDTO.getLast_name());
        }
        if(staffDTO.getUsername() != null){
            staff.setUsername(staffDTO.getUsername());
        }
        if(staffDTO.getPassword() != null){
            staff.setPassword(staffDTO.getPassword());
        }
        try{
            staffRepository.save(staff);
        }catch(PersistenceException e){
            throw new InternalException("Error al guardar.");
        }
        entityManager.flush();
        entityManager.clear();
        return staffRepository.findById(id).orElseThrow(() -> new InternalException("Error al recuperar después de guardar."));
    }

    public void deleteStaff(Integer id)throws NoSuchElementException, InternalException{
        Optional<Staff> opt = staffRepository.findById(id);
        Staff staff = opt.orElseThrow(() -> new NoSuchElementException("Staff no encontrado."));
        List<Store> listaStores = staff.getStores();
        if(!listaStores.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Store asociados.");
        }
        List<Rental> listaRentals = staff.getRentals();
        if(!listaRentals.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Rental asociados.");
        }
        List<Payment> listaPayments = staff.getPayments();
        if(!listaPayments.isEmpty()){
            throw new InternalException(("Imposible eliminar. Tiene registros Payment asociados."));
        }
        staffRepository.deleteById(id);
    }
    
}
