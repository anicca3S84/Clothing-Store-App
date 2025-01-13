package com.dailycode.clothingstore.service.order;

import com.dailycode.clothingstore.dto.OrderDto;
import com.dailycode.clothingstore.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    Order getOrder(Long orderId);
    List<Order> getOrdersByUserId(Long userId);
    OrderDto convertToDto(Order order);
    List<OrderDto> getConvertedOrder(List<Order> orders);
}
