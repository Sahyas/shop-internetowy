package com.example.shop.service;

import com.example.shop.model.Order;
import com.example.shop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;
    
    @InjectMocks
    private OrderService orderService;
    
    private Order testOrder;
    
    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId("order1");
        testOrder.setUserId("user@example.com");
        testOrder.setTotalAmount(299.99);
        testOrder.setStatus("ZŁOŻONE");
    }
    
    @Test
    void createOrder_shouldSaveOrder() {
        // given
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        
        // when
        Order result = orderService.createOrder(testOrder);
        
        // then
        assertNotNull(result);
        assertEquals("order1", result.getId());
        assertEquals(299.99, result.getTotalAmount());
        verify(orderRepository, times(1)).save(testOrder);
    }
    
    @Test
    void findById_whenOrderExists_shouldReturnOrder() {
        // given
        when(orderRepository.findById("order1")).thenReturn(Optional.of(testOrder));
        
        // when
        Optional<Order> result = orderService.findById("order1");
        
        // then
        assertTrue(result.isPresent());
        assertEquals("order1", result.get().getId());
        verify(orderRepository, times(1)).findById("order1");
    }
    
    @Test
    void findByUserId_shouldReturnUserOrders() {
        // given
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByUserId("user@example.com")).thenReturn(orders);
        
        // when
        List<Order> result = orderService.findByUserId("user@example.com");
        
        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user@example.com", result.get(0).getUserId());
        verify(orderRepository, times(1)).findByUserId("user@example.com");
    }
    
    @Test
    void updateOrderStatus_shouldUpdateStatus() {
        // given
        when(orderRepository.findById("order1")).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        
        // when
        Order result = orderService.updateOrderStatus("order1", "WYSŁANE");
        
        // then
        assertNotNull(result);
        assertEquals("WYSŁANE", result.getStatus());
        verify(orderRepository, times(1)).findById("order1");
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    @Test
    void findAll_shouldReturnAllOrders() {
        // given
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);
        
        // when
        List<Order> result = orderService.findAll();
        
        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }
}
