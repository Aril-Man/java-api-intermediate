package training.java.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import training.java.learn.dto.ContactResponse;
import training.java.learn.dto.CreateContactRequest;
import training.java.learn.dto.WebResponse;
import training.java.learn.entity.User;
import training.java.learn.service.ContactService;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(
            path = "/api/create/contact",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
        ContactResponse result = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder().data(result).build();
    }

    @GetMapping(
            path = "/api/contact/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> get(User user, @PathVariable String id) {
        ContactResponse result = contactService.get(user, id);

        return WebResponse.<ContactResponse>builder()
                .data(result)
                .build();
    }
}
