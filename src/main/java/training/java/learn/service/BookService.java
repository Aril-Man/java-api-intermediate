package training.java.learn.service;

import org.springframework.stereotype.Service;
import training.java.learn.dto.BooksResponse;
import training.java.learn.dto.CreateBookRequest;

import java.util.List;

@Service
public interface BookService {
    void addBook(CreateBookRequest request);
    List<BooksResponse> getListBook();
    BooksResponse getBookById(Integer id);
}
