package com.dafnis.AppSpringMySQL.services;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.models.ActorInfo;
import com.dafnis.AppSpringMySQL.repo.ActorInfoRepository;

@Service
public class ActorInfoService {
    
    private ActorInfoRepository actorInfoRepository;
    private ActorInfoService(ActorInfoRepository actorInfoRepository){
        this.actorInfoRepository = actorInfoRepository;
    }

    public Page<ActorInfo> getAll(int numPagina, int tamañoPagina){
        Pageable pageable = PageRequest.of(numPagina -1, tamañoPagina);
        return actorInfoRepository.findAll(pageable);
    }

    public ActorInfo geActorInfoById(Integer id) throws NoSuchElementException{
        Optional<ActorInfo> opt = actorInfoRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new NoSuchElementException("Actor no encontrado.");
    }
    
}
