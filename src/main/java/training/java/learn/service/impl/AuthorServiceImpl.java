package training.java.learn.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import training.java.learn.dto.AuthorResponse;
import training.java.learn.dto.CreateAuthorRequest;
import training.java.learn.entity.Author;
import training.java.learn.entity.Books;
import training.java.learn.repository.AuthorRepository;
import training.java.learn.repository.BookRepository;
import training.java.learn.service.AuthorService;
import training.java.learn.service.ValidationService;

import java.util.List;

@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ValidationService validationService;

    @Override
    public AuthorResponse createAuthor(CreateAuthorRequest request) {

        validationService.validation(request);

        if (authorRepository.findFirstByAuthorName(request.getAuthorName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author name already exist");
        }

        Author author = new Author(request.getAuthorName(), request.getAddress());

        authorRepository.save(author);

        return new AuthorResponse(author.getId(), author.getAuthorName(), author.getAddress(), null);
    }

    @Override
    public AuthorResponse setAuthorBook(Integer id, Integer bookId) {

        if (id.describeConstable().isEmpty() || bookId.describeConstable().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid required request");
        }

        Author author = authorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "author not found"));
        Books books = bookRepository.findById(bookId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));

        books.setAuthor(author);

        bookRepository.save(books);

        List<Books> booksList = bookRepository.findByAuthorId(author.getId());

        return new AuthorResponse(author.getId(), author.getAuthorName(), author.getAddress(), booksList);
    }
}
