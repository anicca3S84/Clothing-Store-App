package com.dailycode.clothingstore.service.order;

import com.dailycode.clothingstore.dto.OrderDto;
import com.dailycode.clothingstore.enums.OrderStatus;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Cart;
import com.dailycode.clothingstore.model.Order;
import com.dailycode.clothingstore.model.OrderItem;
import com.dailycode.clothingstore.model.Product;
import com.dailycode.clothingstore.repository.OrderRepository;
import com.dailycode.clothingstore.repository.ProductRepository;
import com.dailycode.clothingstore.service.cart.CartService;
import com.dailycode.clothingstore.service.cart.ICartService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderService implements IOrderService {


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ICartService iCartService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        //get cart
        //get order
        //get orderItems
        //set orderItems of order
        //set totalPrice
        //clear the cart
        //save

        Cart cart = iCartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setItems(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(orderItems));
        Order savedOrder = orderRepository.save(order);

        iCartService.clearCart(cart.getId());
        return savedOrder;
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        //for each orderItem in cart
        //reset product inventory
        //create new orderItem with cartItem info
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(cartItem.getProduct().getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems){
        return orderItems
                .stream()
                .map(orderItem -> orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public OrderDto convertToDto(Order order){
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> getConvertedOrder(List<Order> orders) {
        return orders.stream().map(this::convertToDto).toList();
    }
}
