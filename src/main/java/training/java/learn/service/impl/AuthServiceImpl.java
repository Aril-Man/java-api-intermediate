package training.java.learn.service.impl;

import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import training.java.learn.Security.BCrypt;
import training.java.learn.dto.LoginUserRequest;
import training.java.learn.dto.TokenResponse;
import training.java.learn.entity.User;
import training.java.learn.repository.UserRepository;
import training.java.learn.service.AuthService;
import training.java.learn.service.ValidationService;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Override
    public TokenResponse login(LoginUserRequest request) {
        validationService.validation(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password is wrong"));

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password is wrong");
        }

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(next30Days());
        userRepository.save(user);

        return TokenResponse.builder()
                .token(user.getToken())
                .expiredAt(user.getTokenExpiredAt())
                .build();
    }

    public Long next30Days() {
        return System.currentTimeMillis() + (1000 * 16 * 24 * 7);
    }
}
