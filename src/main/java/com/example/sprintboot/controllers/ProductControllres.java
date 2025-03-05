package com.example.sprintboot.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sprintboot.dtos.ProductRecordDto;
import com.example.sprintboot.models.ProductModel;
import com.example.sprintboot.repositories.ProductRepository;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductControllres {

    @Autowired
    ProductRepository productRepository;
	
    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel); 
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }
    
    // Método GET para obter todos os produtos
    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> products = productRepository.findAll();
        
        if(!products.isEmpty()) {
        	for(ProductModel product : products) {
        		UUID id = product.getIdProduct();
        		 product.add(linkTo(methodOn(ProductControllres.class).getProductById(id)).withSelfRel());
        	}
        }
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    
 // Método GET para obter um produto por ID
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductModel> getProductById(@PathVariable UUID id) {
        Optional<ProductModel> optionalProduct = productRepository.findById(id);
        
        if (optionalProduct.isEmpty()) {
            // Retorna um ResponseEntity com status 404 e um corpo vazio
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        ProductModel product = optionalProduct.get();
        // Adiciona o link HATEOAS ao produto
        product.add(linkTo(methodOn(ProductControllres.class).getProductById(id)).withSelfRel());
        
        return ResponseEntity.ok(product); // Retorna o produto com status 200
    }

    // Método PUT para atualizar um produto
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductModel> updateProduct(@PathVariable UUID id, @RequestBody @Valid ProductRecordDto productRecordDto) {
        Optional<ProductModel> existingProduct = productRepository.findById(id);
        
        if (existingProduct.isPresent()) {
            ProductModel productModel = existingProduct.get();
            productModel.setName(productRecordDto.name());
            productModel.setValue(productRecordDto.value());
            
            ProductModel updatedProduct = productRepository.save(productModel);
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

 // Método DELETE para remover um produto
    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok("Produto deletado com sucesso."); // 200 OK com mensagem
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

}
