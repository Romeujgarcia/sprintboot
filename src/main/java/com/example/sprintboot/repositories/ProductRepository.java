package com.example.sprintboot.repositories;
import com.example.sprintboot.models.ProductModel;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID>{ 

}
