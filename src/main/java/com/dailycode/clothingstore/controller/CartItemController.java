package com.dailycode.clothingstore.controller;

import com.dailycode.clothingstore.dto.CartDto;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Cart;
import com.dailycode.clothingstore.model.User;
import com.dailycode.clothingstore.response.ApiResponse;
import com.dailycode.clothingstore.service.cart.CartService;
import com.dailycode.clothingstore.service.cart.ICartItemService;
import com.dailycode.clothingstore.service.cart.ICartService;
import com.dailycode.clothingstore.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "api/v1/cartItems/", produces = "application/json")
public class CartItemController {
    private final ICartItemService iCartItemService;
    private final ICartService iCartService;
    @Autowired
    private IUserService iUserService;
    private static final Logger logger = Logger.getLogger(CartItemController.class.getName());

    public CartItemController(ICartItemService iCartItemService, ICartService iCartService) {
        this.iCartItemService = iCartItemService;
        this.iCartService = iCartService;
    }

    @PostMapping("/item/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> addItemToCart(
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            //get the user
            User user = iUserService.getAuthenticatedUser();
            Cart cart = iCartService.initializeNewCart(user);
            iCartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("cart/{cartId}/item/{itemId}/remove")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
                                                          @PathVariable Long itemId){
        try {
            iCartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Remove success", null));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping("cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long itemId,
                                                          @RequestParam int quantity){
        try {
            iCartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Update success", null));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
