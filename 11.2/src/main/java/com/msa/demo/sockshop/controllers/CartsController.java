package com.msa.demo.sockshop.controllers;

import com.msa.demo.sockshop.cart.CartDAO;
import com.msa.demo.sockshop.cart.CartResource;
import com.msa.demo.sockshop.entities.Cart;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestSchema(schemaId = "carts")
@RequestMapping(path = "/carts", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartsController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CartDAO cartRepository;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Cart get(@PathVariable String customerId) {
        return new CartResource(cartRepository, customerId).value().get();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "/{customerId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String customerId) {
        new CartResource(cartRepository, customerId).destroy().run();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "/{customerId}/merge", method = RequestMethod.GET)
    public void mergeCarts(@PathVariable String customerId, @RequestParam(value = "sessionId") String sessionId) {
        logger.debug("Merge carts request received for ids: " + customerId + " and " + sessionId);
        CartResource sessionCart = new CartResource(cartRepository, sessionId);
        CartResource customerCart = new CartResource(cartRepository, customerId);
        customerCart.merge(sessionCart.value().get()).run();
        delete(sessionId);
    }
}
