package com.dailycode.clothingstore.controller;

import com.dailycode.clothingstore.dto.UserDto;
import com.dailycode.clothingstore.exceptions.AlreadyExistException;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.User;
import com.dailycode.clothingstore.request.AddUserRequest;
import com.dailycode.clothingstore.request.UserUpdateRequest;
import com.dailycode.clothingstore.response.ApiResponse;
import com.dailycode.clothingstore.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users/")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @GetMapping("{id}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id){
        try {
            User user = iUserService.getUserById(id);
            UserDto userDto = iUserService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Get success", userDto));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("user/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody AddUserRequest request){
        try {
            User user = iUserService.createUser(request);
            UserDto userDto = iUserService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Create success", userDto));
        }catch (AlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("user/{id}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id){
        try {
            iUserService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse("Delete Success", null));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PutMapping("user/{id}/update")
    public ResponseEntity<ApiResponse> updateUser( @RequestBody UserUpdateRequest request, @PathVariable Long id){
        try {
            User user = iUserService.updateUser(request, id);
            UserDto userDto = iUserService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Update success", userDto));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


}
