package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.StoreDTO;
import com.dafnis.AppSpringMySQL.models.Address;
import com.dafnis.AppSpringMySQL.models.Customer;
import com.dafnis.AppSpringMySQL.models.Inventory;
import com.dafnis.AppSpringMySQL.models.Staff;
import com.dafnis.AppSpringMySQL.models.Store;
import com.dafnis.AppSpringMySQL.repo.AddressRepository;
import com.dafnis.AppSpringMySQL.repo.StaffRepository;
import com.dafnis.AppSpringMySQL.repo.StoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class StoreService {

    private StoreRepository storeRepository;
    private StaffRepository staffRepository;
    private AddressRepository addressRepository;
    private EntityManager entityManager;
    public StoreService(StoreRepository storeRepository, StaffRepository staffRepository,
                            AddressRepository addressRepository, EntityManager entityManager){
        this.storeRepository = storeRepository;
        this.addressRepository = addressRepository;
        this.staffRepository = staffRepository;
        this.entityManager = entityManager;
    }

    public List<Store> getAll(){
        return storeRepository.findAll();
    }

    public Store getStoreById(Integer id)throws NoSuchElementException{
        Optional<Store> opt = storeRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new NoSuchElementException("Store no encontrada.");
    }

    public List<Staff> getStaffByStoreId(Integer id){
        Optional<Store> opt = storeRepository.findById(id);
        Store store = opt.orElseThrow(() -> new NoSuchElementException("Store no encontrada."));
        return store.getStaffList();
    }

    @Transactional
    public Store saveStore(Store store)throws InternalException, NoSuchElementException, DataIntegrityViolationException{
        Optional<Staff> optStaff = staffRepository.findById(store.getStaffIdIntroducido());
        Staff staff = optStaff.orElseThrow((() -> new NoSuchElementException("Staff no encontrado.")));
        store.setStaff(staff);
        if(storeRepository.findByStaff(staff).isPresent()){
            throw new DataIntegrityViolationException("Ese staff ya esta asociado a otra tienda.");
        }
        Optional<Address> optAddress = addressRepository.findById(store.getAddressIdIntroducido());
        store.setAddress(optAddress.orElseThrow(() -> new NoSuchElementException("Address no encontrada.")));
        try{
            Store storeGuardada = storeRepository.save(store);
            entityManager.flush();
            entityManager.clear();
            Optional<Store> opt = storeRepository.findById(storeGuardada.getId());
            if(opt.isPresent()){
                return opt.get();
            }
            throw new InternalException("Error al recuperar después de guardar.");
        }catch(PersistenceException e){
            throw new InternalException("Error de persistencia.");
        }
    }

    @Transactional
    public Store updateStore(Integer id, StoreDTO storeDTO)throws InternalException, NoSuchElementException, PersistenceException{
        Optional<Store> optStore = storeRepository.findById(id);
        Store store = optStore.orElseThrow(() -> new NoSuchElementException("Store no encontrada."));
        if(storeDTO.getAddress_id() != null){
            store.setAddress(addressRepository.findById(storeDTO.getAddress_id()).orElseThrow(() -> new NoSuchElementException("Address no encontrada")));
        }
        if(storeDTO.getStaff_id() != null){
            store.setStaff(staffRepository.findById(storeDTO.getStaff_id()).orElseThrow(() -> new NoSuchElementException("Staff no encontrado.")));
        }
        try{
            storeRepository.save(store);
        }catch(PersistenceException e){
            throw new InternalException("Error al guardar.");
        }
        entityManager.flush();
        entityManager.clear();
        return storeRepository.findById(id).orElseThrow(() -> new InternalException("Error al recuperar después de guardar."));
    }

    public void deleteStore(Integer id)throws NoSuchElementException, InternalException{
        Optional<Store> opt = storeRepository.findById(id);
        Store store = opt.orElseThrow(() -> new NoSuchElementException("Store no encontrada."));
        List<Customer> listaCustomers = store.getCustomers();
        if(!listaCustomers.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Customer asociados.");
        }
        List<Inventory> listaInventories = store.getInventory();
        if(!listaInventories.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Inventory asociados.");
        }
        storeRepository.deleteById(id);
    }

    


    
}
