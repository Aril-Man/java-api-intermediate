package training.java.learn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import training.java.learn.dto.ContactResponse;
import training.java.learn.dto.CreateContactRequest;
import training.java.learn.dto.UpdateContactRequest;
import training.java.learn.entity.Contact;
import training.java.learn.entity.User;
import training.java.learn.repository.ContactRepository;
import training.java.learn.service.ContactService;
import training.java.learn.service.ValidationService;

import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ContactResponse create(User user, CreateContactRequest request) {
        validationService.validation(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        contactRepository.save(contact);

        return ContactResponse.builder()
                .id(contact.getId())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .build();
    }

    @Transactional(readOnly = true)
    public ContactResponse get(User user, String id) {
        Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .build();
    }

    @Transactional
    public ContactResponse update(User user, UpdateContactRequest request) {
        Contact contact = contactRepository.findByUsername(user, user.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

        contact.setFirstName(request.getFirstName().isEmpty() ? contact.getFirstName() : request.getFirstName());
        contact.setLastName(request.getLastName().isEmpty() ? contact.getLastName() : request.getLastName());
        contact.setEmail(request.getEmail().isEmpty() ? contact.getEmail() : request.getEmail());
        contact.setPhone(request.getPhone().isEmpty() ? contact.getPhone() : request.getPhone());

        contactRepository.save(contact);

        return ContactResponse.builder()
                .id(contact.getId())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .firstName(contact.getFirstName())
                .lastName(contact.getPhone())
                .build();
    }
}
