package com.dailycode.clothingstore.service.user;

import com.dailycode.clothingstore.dto.UserDto;
import com.dailycode.clothingstore.model.User;
import com.dailycode.clothingstore.request.AddUserRequest;
import com.dailycode.clothingstore.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long id);
    User createUser(AddUserRequest request);
    User updateUser(UserUpdateRequest request, Long id);
    void deleteUser(Long id);
    UserDto convertUserToDto(User user);
}
