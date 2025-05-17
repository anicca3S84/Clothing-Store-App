package com.dailycode.clothingstore.controller;

import com.dailycode.clothingstore.dto.OrderDto;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Order;
import com.dailycode.clothingstore.model.OrderItem;
import com.dailycode.clothingstore.response.ApiResponse;
import com.dailycode.clothingstore.service.order.IOrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/orders/", produces = "application/json")
public class OrderController {
    @Autowired
    private IOrderService iOrderService;

    @PostMapping("order")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam Long userId){
        try {
            Order order = iOrderService.placeOrder(userId);
            OrderDto orderDto = iOrderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Order success", orderDto));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("{orderId}/order")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId){
        try {
            Order order = iOrderService.getOrder(orderId);
            OrderDto orderDto = iOrderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Get success", orderDto));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("by/user/{userId}/order")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getOrdersByUserId(@PathVariable Long userId){
        try {
            List<Order> orders = iOrderService.getOrdersByUserId(userId);
            List<OrderDto> orderDtos = iOrderService.getConvertedOrder(orders);
            return ResponseEntity.ok(new ApiResponse("Get success", orderDtos));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
