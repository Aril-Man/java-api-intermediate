package training.java.learn.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import training.java.learn.dto.BooksResponse;
import training.java.learn.dto.CreateBookRequest;
import training.java.learn.entity.Books;
import training.java.learn.repository.BookRepository;
import training.java.learn.service.BookService;
import training.java.learn.service.ValidationService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void addBook(CreateBookRequest request) {
        validationService.validation(request);

       if (bookRepository.existByJudul(request.getJudul())) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Judul already exist");
        }

        Books books = new Books(request.getJudul(), request.getPenulis(), request.getPenerbit());
        bookRepository.save(books);
    }

    @Override
    public List<BooksResponse> getListBook() {
        List<Books> books = bookRepository.findAll();
        return toBooksResponse(books);
    }

    @Transactional(readOnly = true)
    public BooksResponse getBookById(Integer id) {
        Optional<Books> book = bookRepository.findById(id);
        return book.map(b -> new BooksResponse(b.getJudul(), b.getPenulis(), b.getPenerbit())).orElse(null);
    }

    private List<BooksResponse> toBooksResponse(List<Books> books) {
        return books.stream().map(b -> new BooksResponse(b.getJudul(), b.getPenulis(), b.getPenerbit())).toList();
    }
}
