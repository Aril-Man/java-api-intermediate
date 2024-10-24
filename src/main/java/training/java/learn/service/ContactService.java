package training.java.learn.service;

import org.springframework.stereotype.Service;
import training.java.learn.dto.ContactResponse;
import training.java.learn.dto.CreateContactRequest;
import training.java.learn.dto.UpdateContactRequest;
import training.java.learn.entity.User;

@Service
public interface ContactService {
    ContactResponse create(User user, CreateContactRequest request);
    ContactResponse get(User user, String id);
    ContactResponse update(User user, UpdateContactRequest request);
}
