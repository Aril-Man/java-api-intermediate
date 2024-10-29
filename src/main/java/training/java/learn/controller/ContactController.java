package training.java.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import training.java.learn.dto.*;
import training.java.learn.entity.User;
import training.java.learn.service.ContactService;

import java.util.List;

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

    @PatchMapping(
            path = "/api/contact/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> update(User user, @RequestBody UpdateContactRequest request) {
        ContactResponse result = contactService.update(user, request);
        return WebResponse.<ContactResponse>builder().data(result).build();
    }

    @DeleteMapping(
            path = "/api/contact/delete/{id}"
    )
    public WebResponse<String> removeContact(User user, @PathVariable String id) {
        return contactService.removeContact(user, id);
    }

    @GetMapping(
            path = "/api/contacts",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ContactResponse>> search(User user, @RequestParam(name = "name", required = false) String name,
                                                     @RequestParam(name = "email", required = false) String email,
                                                     @RequestParam(name = "phone", required = false) String phone,
                                                     @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                                     @RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
        SearchContactRequest request = SearchContactRequest.builder()
                .page(page)
                .size(size)
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        Page<ContactResponse> contactResult = contactService.seach(user, request);

        return WebResponse.<List<ContactResponse>>builder()
                .data(contactResult.getContent())
                .paging(PaginationResponse.builder()
                        .currentPage(contactResult.getNumber())
                        .totalPage(contactResult.getTotalPages())
                        .size(contactResult.getSize())
                        .build())
                .build();
    }
}
