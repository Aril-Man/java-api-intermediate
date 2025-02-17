package training.java.learn.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import training.java.learn.entity.Books;

import java.util.List;

@Data
public class AuthorResponse {

    private Integer id;

    private String authorName;

    private String address;

    private List<Books> books;

    public AuthorResponse(Integer id, String authorName, String address ,@Nullable List<Books> books) {
        super();
        this.id = id;
        this.authorName = authorName;
        this.address = address;
        this.books = books;
    }
}
