package com.br.example.service;

import com.br.example.config.ModelMapperConf;
import com.br.example.dto.ProductDTO;
import com.br.example.dto.ProviderDTO;
import com.br.example.dto.form.ProviderForm;
import com.br.example.model.Provider;
import com.br.example.repository.ProviderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    private static final String ID_NOT_FOUND = "ID não encontrado";
    private final Logger logger = LoggerFactory.getLogger(ProviderService.class);

    private final ProviderRepository providerRepository;

    private final ModelMapperConf modelMapper;


    public ProviderService(ProviderRepository providerRepository, ModelMapperConf modelMapper){
        this.providerRepository = providerRepository;
        this.modelMapper = modelMapper;
    }

    public List<ProviderDTO> findAll() {
        List<Provider> providerList = providerRepository.findAll();
        return providerList.stream().map(ProviderDTO::from).collect(Collectors.toList());
    }

    public ProviderDTO findById(Long id) {
        return ProviderDTO.from(providerRepository.findById(id).orElseThrow(() -> {
            logger.error("Provider not found {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }));
    }

    public ProviderDTO save(ProviderForm providerForm) {
        if (providerRepository.findByName(providerForm.getName()).isPresent()){
            logger.error("Provider already exists {}", providerForm.getName());
        }

        return ProviderDTO.from(providerRepository.save(Provider.from(providerForm)));
    }


    public ProviderDTO update(Long id, ProviderForm providerForm) {

        var providerFound = providerRepository.findById(id).orElseThrow(() ->{
            logger.error("Provider not found {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ID_NOT_FOUND);
        });

        modelMapper.modelMapper().map(providerForm, providerFound);

        return ProviderDTO.from(providerRepository.save(providerFound));

    }

    public void delete(Long id){
        Provider provider = providerRepository.findById(id).orElseThrow(() -> {
            logger.error("ID not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ID_NOT_FOUND);
        });

        providerRepository.delete(provider);
    }
}

