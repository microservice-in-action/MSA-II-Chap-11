package com.msa.demo.sockshop.orders.repositories;

import com.msa.demo.sockshop.orders.entities.CustomerOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import java.util.List;

@RepositoryRestResource(path = "orders", itemResourceRel = "order")
public interface CustomerOrderRepository extends MongoRepository<CustomerOrder, String> {
    @RestResource(path = "customerId")
    List<CustomerOrder> findByCustomerId(@Param("custId") String id);
    
    @RestResource(path = "id")
    CustomerOrder findById(@Param("orderId") String id);
}
