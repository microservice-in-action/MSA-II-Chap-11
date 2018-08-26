package com.msa.demo.sockshop;

import org.springframework.stereotype.Component;
import com.msa.demo.sockshop.entities.Shipment;

@Component
public class ShippingTaskHandler {
    public void handleMessage(Shipment shipment) {
        System.out.println("Received shipment task: " + shipment.getName());
    }
}
