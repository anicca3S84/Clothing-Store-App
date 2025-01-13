package com.dailycode.clothingstore.service.user;

import com.dailycode.clothingstore.dto.UserDto;
import com.dailycode.clothingstore.exceptions.AlreadyExistException;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.User;
import com.dailycode.clothingstore.repository.UserRepository;
import com.dailycode.clothingstore.request.AddUserRequest;
import com.dailycode.clothingstore.request.UserUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{
    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresentOrElse(userRepository::delete, () -> {
            throw new NotFoundException("User not found");
        });
    }

    private final UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User createUser(AddUserRequest request) {
        return Optional.of(request).filter(u -> !userRepository.existsByEmail(request.getEmail()))
                .map(user -> {
                    User newUser = new User();
                    newUser.setFirstName(user.getFirstName());
                    newUser.setLastName(user.getLastName());
                    newUser.setEmail(user.getEmail());
                    newUser.setPassword(user.getPassword());
                    return userRepository.save(newUser);
                }).orElseThrow(() -> new AlreadyExistException("Email " + request.getEmail() + " already exists"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long id) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
