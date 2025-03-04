package training.java.learn.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import training.java.learn.dto.LoginUserRequest;
import training.java.learn.dto.LogoutResponse;
import training.java.learn.dto.TokenResponse;
import training.java.learn.dto.WebResponse;
import training.java.learn.entity.User;
import training.java.learn.service.AuthService;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/auth/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@Valid @RequestBody LoginUserRequest request) {
        TokenResponse login = authService.login(request);
        return WebResponse.<TokenResponse>builder().data(login).build();
    }

    @PostMapping(
            path = "/auth/logout"
    )
    public WebResponse<LogoutResponse> logout(User user) {
        LogoutResponse result = authService.logout(user);
        return WebResponse.<LogoutResponse>builder().data(result).build();
    }
}
