package training.java.learn.service;

import org.springframework.stereotype.Service;
import training.java.learn.dto.AuthorResponse;
import training.java.learn.dto.CreateAuthorRequest;

@Service
public interface AuthorService {
    AuthorResponse createAuthor(CreateAuthorRequest request);
    AuthorResponse setAuthorBook(Integer id, Integer bookId);
}
