package com.br.example.service;

import com.br.example.config.ModelMapperConf;
import com.br.example.dto.ProductDTO;
import com.br.example.dto.form.ProductForm;
import com.br.example.model.Product;
import com.br.example.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final String ID_NOT_FOUND = "ID não encontrado";
    private static final String ID_CONFLICT = "ID doesn´t match";
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ModelMapperConf modelMapper;


    public  ProductService(ProductRepository productRepository, ModelMapperConf modelMapper){
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public List<ProductDTO> findAll() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(ProductDTO::from).collect(Collectors.toList());
    }

    public ProductDTO findById(Long id){
        return ProductDTO.from(productRepository.findById(id).orElseThrow(() -> {
            logger.error("Product not found {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ID_NOT_FOUND);
        }));
    }

    public ProductDTO save(ProductForm productForm){
        if(productRepository.findByNameContaining(productForm.getName()).isPresent()){
            logger.error("Product already exists", productForm.getName());
        }

        Product productSave = Product.from(productForm);

        return ProductDTO.from(productRepository.save(productSave));
    }

    public ProductDTO update(ProductForm productForm, Long id){

        var productFound = productRepository.findById(id).orElseThrow(() ->{
            logger.error("Product not found {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ID_NOT_FOUND);
        });

        modelMapper.modelMapper().map(productForm, productFound);

        return ProductDTO.from(productRepository.save(productFound));

    }

    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> {
            logger.error("ID not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ID_NOT_FOUND);
        });

        productRepository.delete(product);
    }

}
