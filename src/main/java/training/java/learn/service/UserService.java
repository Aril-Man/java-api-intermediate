package training.java.learn.service;

import training.java.learn.dto.RegisterUserRequest;
import training.java.learn.dto.UpdateUserRequest;
import training.java.learn.dto.UserResponse;
import training.java.learn.entity.User;

import java.util.List;

public interface UserService {
    void register(RegisterUserRequest request);
    UserResponse get(User user);
    UserResponse update(User user, UpdateUserRequest request);
    List<User> getUsers(User user);
}
