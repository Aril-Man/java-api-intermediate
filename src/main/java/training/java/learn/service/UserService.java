package training.java.learn.service;

import training.java.learn.dto.RegisterUserRequest;
import training.java.learn.dto.UserResponse;
import training.java.learn.entity.User;

public interface UserService {
    void register(RegisterUserRequest request);
    UserResponse get(User user);
}
