package com.msa.demo.sockshop.controllers;

import com.msa.demo.sockshop.cart.CartDAO;
import com.msa.demo.sockshop.entities.Cart;
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
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UnitCartsController {

    @Autowired
    private ItemsController itemsController;

    @Autowired
    private CartDAO cartRepository;

    @Autowired
    private CartsController cartsController;


    @Test
    public void shouldGetCart() {
        String customerId = "customerIdGet";
        Cart cart = new Cart(customerId);
        cartRepository.save(cart);
        Cart gotCart = cartsController.get(customerId);
        assertThat(gotCart, is(equalTo(cart)));
        assertThat(cartRepository.findByCustomerId(customerId).get(0), is(equalTo(cart)));
    }

    @Test
    public void shouldDeleteCart() {
        String customerId = "customerIdGet";
        Cart cart = new Cart(customerId);
        cartRepository.save(cart);
        cartsController.delete(customerId);
        assertThat(cartRepository.findByCustomerId(customerId), is(empty()));
    }

    @Test
    public void shouldMergeItemsInCartsTogether() {
        String customerId1 = "customerId1";
        Cart cart1 = new Cart(customerId1);
        Item itemId1 = new Item("itemId1");
        cart1.add(itemId1);
        cartRepository.save(cart1);
        String customerId2 = "customerId2";
        Cart cart2 = new Cart(customerId2);
        Item itemId2 = new Item("itemId2");
        cart2.add(itemId2);
        cartRepository.save(cart2);

        cartsController.mergeCarts(customerId1, customerId2);
        assertThat(cartRepository.findByCustomerId(customerId1).get(0).contents(), is(hasSize(2)));
        assertThat(cartRepository.findByCustomerId(customerId1).get(0).contents(), is(containsInAnyOrder(itemId1, itemId2)));
        assertThat(cartRepository.findByCustomerId(customerId2), is(empty()));
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
