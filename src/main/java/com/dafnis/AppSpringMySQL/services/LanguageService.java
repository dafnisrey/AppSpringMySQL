package com.dafnis.AppSpringMySQL.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.models.Film;
import com.dafnis.AppSpringMySQL.models.Language;
import com.dafnis.AppSpringMySQL.repo.LanguageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class LanguageService {
    
    private LanguageRepository languageRepository;
    private EntityManager entityManager;
    public LanguageService(LanguageRepository languageRepository, EntityManager entityManager){
        this.languageRepository = languageRepository;
        this.entityManager = entityManager;
    }
    public List<Language> getAll(){
        return languageRepository.findAll();
    }

    @Transactional
    public Language saveLanguage(Language language)throws InternalException{
        try{
            Language languageGuardado = languageRepository.save(language);
            entityManager.flush();
            entityManager.clear();
            Optional<Language> opt = languageRepository.findById(languageGuardado.getId());
            if(opt.isPresent()){
                return opt.get();
            }
            throw new InternalException("Error al recuperar despues de guardar.");
        }catch(PersistenceException e){
            throw new InternalException("Error de persistencia.");
        }
    }

    public void deleteLanguage(Integer id)throws NoSuchElementException, InternalException{
        Optional<Language> opt = languageRepository.findById(id);
        Language language = opt.orElseThrow(() -> new NoSuchElementException("Language no encontrado."));
        List<Film> listaFilms = language.getFilms();
        if(!listaFilms.isEmpty()){
            throw new InternalException("Imposible eliminar. Tiene registros Film asociados.");
        }
        languageRepository.deleteById(id);
    }
    
}
