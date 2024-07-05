package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController controller;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Test
    public void verifySubmitOrderSuccessfully() {
        User user = MockData.createUser();
        Item item = MockData.createItem();

        Cart cart = user.getCart();

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);

        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("Username")).thenReturn(user);

        ResponseEntity<UserOrder> response = controller.submit("Username");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserOrder retrievedUserOrder = response.getBody();
        assertNotNull(retrievedUserOrder);
        assertNotNull(retrievedUserOrder.getItems());
        assertNotNull(retrievedUserOrder.getTotal());
        assertNotNull(retrievedUserOrder.getUser());
    }

    @Test
    public void verifySubmitOrderWithNullUser() {
        User user = MockData.createUser();
        Item item = MockData.createItem();
        Cart cart = user.getCart();

        cart.setUser(user);
        user.setCart(cart);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);

        when(userRepository.findByUsername("Username")).thenReturn(null);
        ResponseEntity<UserOrder> response = controller.submit("Username");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void verifyGetOrdersForUserSuccessfully() {
        User user = MockData.createUser();
        Item item = MockData.createItem();
        Cart cart = user.getCart();

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);

        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("Username")).thenReturn(user);
        controller.submit("Username");

        ResponseEntity<List<UserOrder>> responseEntity = controller.getOrdersForUser("Username");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<UserOrder> userOrders = responseEntity.getBody();
        assertNotNull(userOrders);
        assertEquals(0, userOrders.size());
    }

    @Test
    public void verifyGetOrdersForUserWithNullUser() {
        User user = MockData.createUser();
        Item item = MockData.createItem();
        Cart cart = user.getCart();

        cart.setUser(user);
        user.setCart(cart);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);

        when(userRepository.findByUsername("Username")).thenReturn(null);

        controller.submit("Username");

        ResponseEntity<List<UserOrder>> responseEntity = controller.getOrdersForUser("Username");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}