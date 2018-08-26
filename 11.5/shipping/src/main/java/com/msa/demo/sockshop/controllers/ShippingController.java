package com.msa.demo.sockshop.controllers;

import com.msa.demo.sockshop.entities.HealthCheck;
import com.msa.demo.sockshop.entities.Shipment;
import com.rabbitmq.client.Channel;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestSchema(schemaId = "shipping")
@RequestMapping(path = "/shipping", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShippingController {
	
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public String getShipping() {
        return "GET ALL Shipping Resource.";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getShippingById(@PathVariable String id) {
        return "GET Shipping Resource with id: " + id;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/{username}", method = RequestMethod.POST)
    public @ResponseBody
    Shipment postShipping(@RequestBody Shipment shipment, @PathVariable("username") String username) {
        System.out.println("Adding shipment to queue..."+username);
        try {
            rabbitTemplate.convertAndSend("shipping-task", shipment);
        } catch (Exception e) {
            System.out
                    .println("Unable to add to queue (the queue is probably down). Accepting anyway. Don't do this " +
                            "for real!");
        }

        return shipment;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, path = "/health")
    public @ResponseBody
    Map<String, List<HealthCheck>> getHealth() {
        Map<String, List<HealthCheck>> map = new HashMap<String, List<HealthCheck>>();
        List<HealthCheck> healthChecks = new ArrayList<HealthCheck>();
        Date dateNow = Calendar.getInstance().getTime();

        HealthCheck rabbitmq = new HealthCheck("shipping-rabbitmq", "OK", dateNow);
        HealthCheck app = new HealthCheck("shipping", "OK", dateNow);

        try {
            this.rabbitTemplate.execute(new ChannelCallback<String>() {
                @Override
                public String doInRabbit(Channel channel) throws Exception {
                    Map<String, Object> serverProperties = channel.getConnection().getServerProperties();
                    return serverProperties.get("version").toString();
                }
            });
        } catch (AmqpException e) {
            rabbitmq.setStatus("err");
        }

        healthChecks.add(rabbitmq);
        healthChecks.add(app);

        map.put("health", healthChecks);
        return map;
    }
}
