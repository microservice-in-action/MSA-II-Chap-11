package com.msa.demo.sockshop.orders.resources;

import java.net.URI;
import java.util.List;

import com.msa.demo.sockshop.orders.entities.Item;
import org.hibernate.validator.constraints.URL;

public class NewOrderResource {
    @URL
    public URI customer;

    @URL
    public URI address;

    @URL
    public URI card;

    public List<Item> items;

    public String customerId;
}
