package com.msa.demo.sockshop.cart;

import com.msa.demo.sockshop.entities.Item;

import java.util.List;
import java.util.function.Supplier;

public interface Contents<T> {
    Supplier<List<T>> contents();

    Runnable add(Supplier<Item> item);

    Runnable delete(Supplier<Item> item);
}
