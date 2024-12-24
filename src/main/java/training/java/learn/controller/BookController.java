package training.java.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import training.java.learn.dto.BooksResponse;
import training.java.learn.dto.CreateBookRequest;
import training.java.learn.dto.WebResponse;
import training.java.learn.service.BookService;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping(
            path = "/api/book/add",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> addBook(@RequestBody CreateBookRequest request) {
        bookService.addBook(request);

        return WebResponse.<String>builder().data("Successfully add book").build();
    }

    @GetMapping(
            path = "/api/books",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<BooksResponse>> getListBook() {
        List<BooksResponse> books = bookService.getListBook();
        return WebResponse.<List<BooksResponse>>builder().data(books).build();
    }

    @GetMapping(
            path = "/api/book/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<BooksResponse> getBookById(@PathVariable(name = "id") Integer id) {
        BooksResponse book = bookService.getBookById(id);
        return WebResponse.<BooksResponse>builder().data(book).build();
    }
}
