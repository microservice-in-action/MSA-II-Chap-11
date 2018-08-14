package com.msa.demo.sockshop.controllers;

import com.msa.demo.sockshop.cart.CartDAO;
import com.msa.demo.sockshop.entities.Item;
import com.msa.demo.sockshop.item.ItemStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UnitItemsController {

    @Autowired
    private ItemsController itemsController;

    @Autowired
    private ItemStore itemRepository;

    @Autowired
    private CartsController cartsController;

    @Test
    public void whenNewItemAdd() {
        Item item = new Item("id", "itemId", 1, 0F);
        String customerId = "customerIdAdd";
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId), is(hasSize(1)));
        assertThat(itemsController.getItems(customerId), is(org.hamcrest.CoreMatchers.hasItem(item)));
    }

    @Test
    public void whenExistIncrementQuantity() {
        Item item = new Item("id", "itemId", 1, 0F);
        String customerId = "customerIdIncrement";
        itemsController.addToCart(customerId, item);
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId), is(hasSize(1)));
        assertThat(itemsController.getItems(customerId), is(org.hamcrest.CoreMatchers.hasItem(item)));
        assertThat(itemRepository.findOne(item.id()).quantity(), is(equalTo(2)));
    }

    @Test
    public void shouldRemoveItemFromCart() {
        Item item = new Item("id", "itemId", 1, 0F);
        String customerId = "customerIdRemove";
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId), is(hasSize(1)));
        itemsController.removeItem(customerId, item.itemId());
        assertThat(itemsController.getItems(customerId), is(hasSize(0)));
    }

    @Test
    public void shouldSetQuantity() {
        Item item = new Item("id", "itemId", 1, 0F);
        String customerId = "customerIdQuantity";
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId).get(0).quantity(), is(equalTo(item.quantity())));
        Item anotherItem = new Item(item, 15);
        itemsController.updateItem(customerId, anotherItem);
        assertThat(itemRepository.findOne(item.id()).quantity(), is(equalTo(anotherItem.quantity())));
    }

    @Configuration
    static class ItemsControllerTestConfiguration {
        @Bean
        public ItemsController itemsController() {
            return new ItemsController();
        }

        @Bean
        public CartsController cartsController() {
            return new CartsController();
        }

        @Bean
        public ItemStore itemDAO() {
            return new ItemStore.Fake();
        }

        @Bean
        public CartDAO cartDAO() {
            return new CartDAO.Fake();
        }
    }
}
