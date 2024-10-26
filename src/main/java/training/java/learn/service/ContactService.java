package training.java.learn.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import training.java.learn.dto.*;
import training.java.learn.entity.User;

@Service
public interface ContactService {
    ContactResponse create(User user, CreateContactRequest request);
    ContactResponse get(User user, String id);
    ContactResponse update(User user, UpdateContactRequest request);
    WebResponse<String> removeContact(User user, String id);
    Page<ContactResponse> seach(User user, SearchContactRequest request);
}
