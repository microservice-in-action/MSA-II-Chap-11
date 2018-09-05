package com.msa.demo.sockshop.orders.values;

import com.msa.demo.sockshop.orders.entities.Address;
import com.msa.demo.sockshop.orders.entities.Card;
import com.msa.demo.sockshop.orders.entities.Customer;

public class PaymentRequest {

    private float amount;

    // For jackson
    public PaymentRequest() {
    }

    public PaymentRequest(Address address, Card card, Customer customer, float amount) {
        this.amount = amount;
    }

    public PaymentRequest(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                '}';
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
