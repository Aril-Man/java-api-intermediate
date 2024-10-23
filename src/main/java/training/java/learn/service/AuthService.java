package training.java.learn.service;

import org.springframework.stereotype.Service;
import training.java.learn.dto.LoginUserRequest;
import training.java.learn.dto.LogoutResponse;
import training.java.learn.dto.TokenResponse;
import training.java.learn.entity.User;

@Service
public interface AuthService {
    TokenResponse login(LoginUserRequest request);
    LogoutResponse logout(User user);
}
