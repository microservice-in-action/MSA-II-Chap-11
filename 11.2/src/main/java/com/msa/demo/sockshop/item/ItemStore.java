package com.msa.demo.sockshop.item;

import com.msa.demo.sockshop.entities.Item;

import java.util.HashMap;
import java.util.Map;

public interface ItemStore {
    Item save(Item item);

    void destroy(Item item);

    Item findOne(String id);

    class Fake implements ItemStore {
        private Map<String, Item> store = new HashMap<>();

        @Override
        public Item save(Item item) {
            return store.put(item.itemId(), item);
        }

        @Override
        public void destroy(Item item) {
            store.remove(item.itemId());

        }

        @Override
        public Item findOne(String id) {
            return store.entrySet()
                    .stream()
                    .filter(i -> i.getValue().id().equals(id))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse(null);
        }
    }
}
