package training.java.learn.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import training.java.learn.dto.BooksResponse;
import training.java.learn.dto.CreateBookRequest;
import training.java.learn.dto.WebResponse;
import training.java.learn.entity.Books;
import training.java.learn.repository.BookRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    Faker faker = new Faker();

    @AfterEach
    void setUp() {
        if (bookRepository.count() != 0) {
            bookRepository.deleteAll();
        }
    }

    @Test
    void createBookSuccess() throws Exception {
        CreateBookRequest request = new CreateBookRequest();
        request.setJudul(faker.book().title());
        request.setPenerbit(faker.book().publisher());
        request.setPenulis(faker.book().author());

        mockMvc.perform(
                post("/api/book/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

            assertNull(response.getErrors());
        });
    }

    @Test
    void createBookAlreadyExist() throws Exception {

        Books books = new Books();
        books.setJudul("Test 1");
        books.setPenulis("Aril");
        books.setPenerbit("Pelangi");
        bookRepository.save(books);

        CreateBookRequest request = new CreateBookRequest();
        request.setJudul("Test 1");
        request.setPenerbit("Pelangi");
        request.setPenulis("Aril");

        mockMvc.perform(
                post("/api/book/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getAllBookSuccess() throws Exception {

        mockMvc.perform(
                get("/api/books")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<BooksResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<BooksResponse>>>() {});

            assertNull(response.getErrors());
        });
    }
}