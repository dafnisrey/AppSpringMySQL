package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.InventoryDTO;
import com.dafnis.AppSpringMySQL.models.Film;
import com.dafnis.AppSpringMySQL.models.Inventory;
import com.dafnis.AppSpringMySQL.models.Rental;
import com.dafnis.AppSpringMySQL.models.Store;
import com.dafnis.AppSpringMySQL.repo.FilmRepository;
import com.dafnis.AppSpringMySQL.repo.InventoryRepository;
import com.dafnis.AppSpringMySQL.repo.StoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class InventoryService {

    private InventoryRepository inventoryRepository;
    private StoreRepository storeRepository;
    private FilmRepository filmRepository;
    private EntityManager entityManager;
    public InventoryService(InventoryRepository inventoryRepository, StoreRepository storeRepository,
                                FilmRepository filmRepository, EntityManager entityManager){
        this.inventoryRepository = inventoryRepository;
        this.filmRepository = filmRepository;
        this.entityManager = entityManager;
        this.storeRepository = storeRepository;
    }

    public Page<Inventory> getAll(int numPagina, int tamañoPagina){
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return inventoryRepository.findAll(pageable);
    }

    public List<Inventory> getInventoryByFilmId(Integer filmId){
        return inventoryRepository.findByFilmId(filmId);
    }

    public Page<Inventory> getInventoriesByStoreId(Integer id, int numPagina, int tamañoPagina)throws NoSuchElementException{
        if (!storeRepository.existsById(id)) {
            throw new NoSuchElementException("Ese Store no existe.");
        }
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return inventoryRepository.findByStoreId(id, pageable);
    }

    @Transactional
    public Inventory saveInventory(Inventory inventory)throws InternalException{
        Optional<Film> optFilm = filmRepository.findById(inventory.getFilmIdIntroducido());
        if(optFilm.isPresent()){
            inventory.setFilm(optFilm.get());
        }else{
            throw new InternalException("Film no encontrada.");
        }
        Optional<Store> optStore = storeRepository.findById(inventory.getStoreIdIntroducido());
        if(optStore.isPresent()){
            inventory.setStore(optStore.get());
        }else{
            throw new InternalException("Store no encontrada.");
        }
        try{
            Inventory inventoryGuardado = inventoryRepository.save(inventory);
            entityManager.flush();
            entityManager.clear();
            Optional<Inventory> opt = inventoryRepository.findById(inventoryGuardado.getId());
            if(opt.isPresent()){
                return opt.get();
            }
            throw new InternalException("Error al recuperar después de guardar.");
        }catch(PersistenceException e){
            throw new InternalException("Error de persistencia.");
        }
    }

    @Transactional
    public Inventory updateInventory(Integer id, InventoryDTO inventoryDTO)throws NoSuchElementException, InternalException{
        Optional<Inventory> optInventory = inventoryRepository.findById(id);
        if(!optInventory.isPresent()){
            throw new NoSuchElementException("Inventory no encontrado.");
        }
        Inventory inventory = optInventory.get();
        if(inventoryDTO.getFilm_id() != null){
            Optional<Film> optFilm = filmRepository.findById(inventoryDTO.getFilm_id());
            if(optFilm.isPresent()){
                inventory.setFilm(optFilm.get());
            }else{
                throw new NoSuchElementException("Film no encontrada.");
            }
        }
        if(inventoryDTO.getStore_id() != null){
            Optional<Store> optStore = storeRepository.findById(inventoryDTO.getStore_id());
            if(optStore.isPresent()){
                inventory.setStore(optStore.get());
            }else{
                throw new NoSuchElementException("Store no encontrada.");
            }
        }
        inventoryRepository.save(inventory);
        entityManager.flush();
        entityManager.clear();
        return inventoryRepository.findById(id).orElseThrow(() -> new InternalException("Error al recuperar después de guardar."));
    }

    public void deleteInventory(Integer id)throws NoSuchElementException, InternalException{
        Optional<Inventory> opt = inventoryRepository.findById(id);
        Inventory inventory = opt.orElseThrow(() -> new NoSuchElementException("Inventory no encontrado."));
        List<Rental> listaRentals = inventory.getRentals();
        if(!listaRentals.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Rental asociados.");
        }
        inventoryRepository.deleteById(id);
    }

}
