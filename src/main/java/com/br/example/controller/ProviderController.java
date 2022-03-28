package com.br.example.controller;

import com.br.example.dto.ProviderDTO;
import com.br.example.dto.form.ProviderForm;
import com.br.example.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @Autowired
    public ProviderController(ProviderService providerService){
        this.providerService = providerService;
    }

    @GetMapping
    public ResponseEntity<List<ProviderDTO>> findAll(){
        return ResponseEntity.ok(providerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(providerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProviderDTO> save(@RequestBody ProviderForm providerForm){
        return ResponseEntity.ok(providerService.save(providerForm));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderDTO> update(@PathVariable Long id, @RequestBody ProviderForm providerForm){
        return ResponseEntity.ok(providerService.update(id, providerForm));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        providerService.delete(id);
        return ResponseEntity.ok().build();
    }

}
