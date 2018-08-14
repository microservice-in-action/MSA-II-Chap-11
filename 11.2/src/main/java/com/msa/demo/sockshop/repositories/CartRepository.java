package com.msa.demo.sockshop.repositories;

import com.msa.demo.sockshop.entities.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface CartRepository extends MongoRepository<Cart, String> {
    List<Cart> findByCustomerId(@Param("custId") String id);
}
