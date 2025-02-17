package training.java.learn.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import training.java.learn.dto.AuthorResponse;
import training.java.learn.dto.CreateAuthorRequest;
import training.java.learn.entity.Author;
import training.java.learn.entity.Books;
import training.java.learn.repository.AuthorRepository;
import training.java.learn.repository.BookRepository;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    Faker faker = new Faker();

    @Test
    void createAuthor() throws Exception {

        CreateAuthorRequest request = new CreateAuthorRequest(faker.name().firstName(), faker.address().fullAddress());

        mockMvc.perform(
                post("/api/author/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        );
    }

    @Test
    void serAuthorBook() throws Exception {

        Author author = authorRepository.findLatest();
        Books books = bookRepository.findLatest();

        mockMvc.perform(
                post("/api/author/set-author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", author.getId().toString())
                        .param("book_id", books.getId().toString())
        ).andExpectAll(
            status().isOk()
        );
    }
}