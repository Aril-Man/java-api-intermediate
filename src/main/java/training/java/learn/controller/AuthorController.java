package training.java.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import training.java.learn.dto.AuthorResponse;
import training.java.learn.dto.CreateAuthorRequest;
import training.java.learn.service.AuthorService;

@RestController
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping(
            path = "/api/author/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthorResponse> createAuthor(@RequestBody CreateAuthorRequest request) {
        AuthorResponse authorResponse = authorService.createAuthor(request);
        return new ResponseEntity< >(authorResponse, HttpStatus.OK);
    }

    @PostMapping(
            path = "/api/author/set-author",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthorResponse> setAuthorBook(@RequestParam(name = "id") Integer id, @RequestParam(name = "book_id") Integer bookId) {
        AuthorResponse authorResponse = authorService.setAuthorBook(id, bookId);
        return new ResponseEntity< >(authorResponse, HttpStatus.OK);
    }
}
