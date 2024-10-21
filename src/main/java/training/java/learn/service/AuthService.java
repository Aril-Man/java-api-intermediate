package training.java.learn.service;

import org.springframework.stereotype.Service;
import training.java.learn.dto.LoginUserRequest;
import training.java.learn.dto.TokenResponse;

@Service
public interface AuthService {
    TokenResponse login(LoginUserRequest request);
}
