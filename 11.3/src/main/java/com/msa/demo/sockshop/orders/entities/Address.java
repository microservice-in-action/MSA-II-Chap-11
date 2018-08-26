package com.msa.demo.sockshop.orders.entities;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class Address {

    @Id
    private String id;

    private String address;

    public Address() {
    }

    public Address(String id, String address) {
        this.id = id;
        this.address = address;
    }

    public Address(String address) {
        this(null, address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return Objects.equals(id, address1.id) &&
                Objects.equals(address, address1.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
