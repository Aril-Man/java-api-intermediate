package training.java.learn.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import training.java.learn.Security.BCrypt;
import training.java.learn.dto.LoginUserRequest;
import training.java.learn.dto.LogoutResponse;
import training.java.learn.dto.TokenResponse;
import training.java.learn.entity.User;
import training.java.learn.repository.UserRepository;
import training.java.learn.service.AuthService;
import training.java.learn.service.ValidationService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
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

        String token = generateToken(request);

        user.setToken(token);
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 16 * 24 * 7));
        userRepository.save(user);

        return TokenResponse.builder()
                .token(user.getToken())
                .expiredAt(next30Days())
                .build();
    }

    @Override
    public LogoutResponse logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);
        userRepository.save(user);

        return LogoutResponse.builder().message("Successfully logout").build();
    }

    public String next30Days() {
        Long expired = System.currentTimeMillis() + (1000 * 16 * 24 * 7);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YY HH:mm:ss");
        Date result = new Date(expired);

        return sdf.format(result);
    }

    public String generateToken(LoginUserRequest request) {
        try {

            Long expired = System.currentTimeMillis() + (1000 * 16 * 24 * 7);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YY HH:mm:ss");
            Date date = new Date(expired);

            Algorithm algorithm = Algorithm.HMAC256("Key");
            String token = JWT.create()
                    .withClaim("username", request.getUsername())
                    .withClaim("password", request.getPassword())
                    .withExpiresAt(date)
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.toString());
        }
    }
}
