package com.msa.demo.sockshop.item;

import com.msa.demo.sockshop.entities.Item;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;


public class UnitItemResource {
    private ItemStore itemRepository = new ItemStore.Fake();

    @Test
    public void testCreateAndDestroy() {
        Item item = new Item("itemId", "testId", 1, 0F);
        ItemResource itemResource = new ItemResource(itemRepository, () -> item);
        itemResource.create().get();
        assertThat(itemRepository.findOne(item.id()), is(equalTo(item)));
        itemResource.destroy().run();
        assertThat(itemRepository.findOne(item.id()), is(nullValue()));
    }

    @Test
    public void mergedItemShouldHaveNewQuantity() {
        Item item = new Item("itemId", "testId", 1, 0F);
        ItemResource itemResource = new ItemResource(itemRepository, () -> item);
        assertThat(itemResource.value().get(), is(equalTo(item)));
        Item newItem = new Item(item, 10);
        itemResource.merge(newItem).run();
        assertThat(itemRepository.findOne(item.id()).quantity(), is(equalTo(newItem.quantity())));
    }
}
