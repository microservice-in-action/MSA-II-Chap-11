package com.msa.demo.sockshop.cart;

import java.util.function.Supplier;

public interface HasContents<T extends Contents> {
    Supplier<T> contents();
}
