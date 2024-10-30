package training.java.learn.service.impl;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import training.java.learn.dto.*;
import training.java.learn.entity.Contact;
import training.java.learn.entity.User;
import training.java.learn.repository.ContactRepository;
import training.java.learn.service.ContactService;
import training.java.learn.service.ValidationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Contact contact = contactRepository.findById(request.getId())
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

    @Override
    public WebResponse<String> removeContact(User user, String id) {
        contactRepository.deleteById(id);
        return WebResponse.<String>builder().data("Successfully remove contact").build();

    }

    @Transactional(readOnly = true)
    public Page<ContactResponse> seach(User user, SearchContactRequest request) {
        Specification<Contact> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));
            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.or(
                        builder.like(root.get("firstName"), "%"+ request.getName() +"%"),
                        builder.like(root.get("lastName"), "%"+ request.getName() +"%")
                ));
            }
            if (Objects.nonNull(request.getEmail())) {
                predicates.add(builder.or(
                        builder.like(root.get("email"), "%" + request.getEmail() + "%")
                ));
            }
            if (Objects.nonNull(request.getPhone())) {
                predicates.add(builder.or(
                        builder.like(root.get("phone"), "%" + request.getPhone() + "%")
                ));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getGroupRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);
        List<ContactResponse> contactResponses = contacts.getContent().stream()
                .map(this::toContactResponse)
                .toList();

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }

    private ContactResponse toContactResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }
}
