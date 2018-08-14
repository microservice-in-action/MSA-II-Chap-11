package com.msa.demo.sockshop.controllers;

import com.msa.demo.sockshop.cart.CartDAO;
import com.msa.demo.sockshop.cart.CartResource;
import com.msa.demo.sockshop.entities.Item;
import com.msa.demo.sockshop.item.FoundItem;
import com.msa.demo.sockshop.item.ItemStore;
import com.msa.demo.sockshop.item.ItemResource;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

import static org.slf4j.LoggerFactory.getLogger;

@RestSchema(schemaId = "items")
@RequestMapping(value = "/carts")
public class ItemsController {
    private final Logger LOG = getLogger(getClass());

    @Autowired
    private ItemStore itemRepository;

    @Autowired
    private CartsController cartsController;

    @Autowired
    private CartDAO cartRepository;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "/{customerId:.*}/items/{itemId:.*}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public Item get(@PathVariable String customerId, @PathVariable String itemId) {
        return new FoundItem(() -> getItems(customerId), () -> new Item(itemId)).get();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "/{customerId:.*}/items/",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public List<Item> getItems(@PathVariable String customerId) {
        return cartsController.get(customerId).contents();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(
            value = "/{customerId:.*}/items/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public Item addToCart(@PathVariable String customerId, @RequestBody Item item) {
        // If the item does not exist in the cart, create new one in the
        // repository.
        FoundItem foundItem = new FoundItem(() -> cartsController.get(customerId).contents(), () -> item);
        if (!foundItem.hasItem()) {
            Supplier<Item> newItem = new ItemResource(itemRepository, () -> item).create();
            LOG.debug("Did not find item. Creating item for user: " + customerId + ", " + newItem.get());
            new CartResource(cartRepository, customerId).contents().get().add(newItem).run();
            return item;
        } else {
            Item newItem = new Item(foundItem.get(), foundItem.get().quantity() + 1);
            LOG.debug("Found item in cart. Incrementing for user: " + customerId + ", " + newItem);
            updateItem(customerId, newItem);
            return newItem;
        }
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{customerId:.*}/items/{itemId:.*}", method = RequestMethod.DELETE)
    public void removeItem(@PathVariable String customerId, @PathVariable String itemId) {
        FoundItem foundItem = new FoundItem(() -> getItems(customerId), () -> new Item(itemId));
        Item item = foundItem.get();

        LOG.debug("Removing item from cart: " + item);
        new CartResource(cartRepository, customerId).contents().get().delete(() -> item).run();

        LOG.debug("Removing item from repository: " + item);
        new ItemResource(itemRepository, () -> item).destroy().run();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(
            value = "/{customerId:.*}/items/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PATCH)
    public void updateItem(@PathVariable String customerId, @RequestBody Item item) {
        ItemResource itemResource = new ItemResource(itemRepository, () -> get(customerId, item.itemId()));
        LOG.debug("Merging item in cart for user: " + customerId + ", " + item);
        itemResource.merge(item).run();
    }
}
