package com.msa.demo.sockshop.configuration;

import com.msa.demo.sockshop.cart.CartDAO;
import com.msa.demo.sockshop.entities.Cart;
import com.msa.demo.sockshop.entities.Item;
import com.msa.demo.sockshop.item.ItemStore;
import com.msa.demo.sockshop.repositories.CartRepository;
import com.msa.demo.sockshop.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BeanConfiguration {
    @Bean
    @Autowired
    public CartDAO getCartDao(CartRepository cartRepository) {
        return new CartDAO() {
            @Override
            public void delete(Cart cart) {
                cartRepository.delete(cart);
            }

            @Override
            public Cart save(Cart cart) {
                return cartRepository.save(cart);
            }

            @Override
            public List<Cart> findByCustomerId(String customerId) {
                return cartRepository.findByCustomerId(customerId);
            }
        };
    }

    @Bean
    @Autowired
    public ItemStore getItemDao(ItemRepository itemRepository) {
        return new ItemStore() {
            @Override
            public Item save(Item item) {
                return itemRepository.save(item);
            }

            @Override
            public void destroy(Item item) {
                itemRepository.delete(item);
            }

            @Override
            public Item findOne(String id) {
                return itemRepository.findOne(id);
            }
        };
    }
}
