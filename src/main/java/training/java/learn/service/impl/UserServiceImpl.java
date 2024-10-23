package training.java.learn.service.impl;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import training.java.learn.Security.BCrypt;
import training.java.learn.dto.RegisterUserRequest;
import training.java.learn.dto.UpdateUserRequest;
import training.java.learn.dto.UserResponse;
import training.java.learn.entity.User;
import training.java.learn.repository.UserRepository;
import training.java.learn.service.UserService;
import training.java.learn.service.ValidationService;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validation(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username alredy register");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }

    @Override
    public UserResponse get(User user) {
        return UserResponse.builder()
                .username(user.getUsername() )
                .name(user.getName())
                .build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validation(request);
        if (!request.getPassword().isEmpty() && !BCrypt.checkpw(request.getOld_password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password not match");
        }

        user.setName(request.getName().isEmpty() ? user.getName() : request.getName());
        user.setPassword(request.getPassword().isEmpty() ? user.getPassword() : BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));

        userRepository.save(user);

        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }
}
