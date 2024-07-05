package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @InjectMocks
    private ItemController controller;

    @Mock
    private ItemRepository itemRepository;

    @Test
    public void verifyGetItemByIdSuccessfully(){
        when(itemRepository.findById(1L)).thenReturn(Optional.of(MockData.createItem()));

        ResponseEntity<Item> response = controller.getItemById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        Item item = response.getBody();
        assertNotNull(item);
    }

    @Test
    public void verifyGetItemsSuccessfully(){
        ResponseEntity<List<Item>> response = controller.getItems();

        List<Item> itemList = response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(itemList);
    }

    @Test
    public void verifyGetItemByNameSuccessfully(){
        List<Item> items = new ArrayList<>();

        items.add(MockData.createItem());
        when(this.itemRepository.findByName("Created Item")).thenReturn(items);
        ResponseEntity<List<Item>> response = controller.getItemsByName("Created Item");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(items, response.getBody());
    }

}