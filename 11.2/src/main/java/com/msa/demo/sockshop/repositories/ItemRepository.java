package com.msa.demo.sockshop.repositories;

import com.msa.demo.sockshop.entities.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ItemRepository extends MongoRepository<Item, String> {
}
