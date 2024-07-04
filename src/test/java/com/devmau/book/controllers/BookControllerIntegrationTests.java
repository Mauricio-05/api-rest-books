package com.devmau.book.controllers;

import com.devmau.book.TestDataUtil;
import com.devmau.book.domain.entities.AuthorEntity;
import com.devmau.book.domain.entities.BookEntity;
import com.devmau.book.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BookService bookService;


    private final String apiPrefix;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, Environment env, BookService bookService){
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.apiPrefix = env.getProperty("apiPrefix");
        this.bookService = bookService;
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsHttp201Created() throws Exception {
        BookEntity bookA = TestDataUtil.createTestBookA(null);

        String bookAJson = objectMapper.writeValueAsString(bookA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(apiPrefix +"/books").contentType(MediaType.APPLICATION_JSON).content(bookAJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.success").isBoolean()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data").isMap()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.isbn").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.title").value(bookA.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").isString()
        );
    }

    @Test
    public void testThatListBooksReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPrefix +"/books").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListBooksReturnsListBooks() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);

        bookService.savedBook(bookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPrefix +"/books").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.success").isBoolean()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data").isMap()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.content[0].isbn").value(bookEntityA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.content[0].title").value(bookEntityA.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").isString()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus200WhenBookExist() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);

        bookService.savedBook(bookEntityA);


        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPrefix +"/books/978-1-2345-6789-0").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBookReturnsBookWhenBookExist() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);

        bookService.savedBook(bookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPrefix +"/books/978-1-2345-6789-0").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.success").isBoolean()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data").isMap()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.isbn").value(bookEntityA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.title").value(bookEntityA.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").isString()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus404WhenNoBookExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPrefix +"/books/454544534").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }


    @Test
    public void testThatFullUpdateBookReturnsHttpStatus404WhenNoBookExist() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);

        String bookDtoJson = objectMapper.writeValueAsString(bookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPrefix +"/books/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateBookReturnsHttpStatus200WhenBookExists() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);

        BookEntity savedBook = bookService.savedBook(bookEntityA);

        String bookDtoJson = objectMapper.writeValueAsString(bookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPrefix +"/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }


    @Test
    public void testThatFullUpdateUpdatesExistingBook() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);

        BookEntity savedBook = bookService.savedBook(bookEntityA);

        BookEntity bookDto = TestDataUtil.createTestBookB(null);
        String bookDtoJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPrefix +"/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.success").isBoolean()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data").isMap()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.isbn").value(savedBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.title").value(bookDto.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").isString()
        );
    }

    @Test
    public void testThatPartialUpdatesExistingBookReturnsHttpStatus200Ok() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);

        BookEntity savedBook = bookService.savedBook(bookEntityA);

        bookEntityA.setTitle("UPDATED");
        String bookDtoJson = objectMapper.writeValueAsString(bookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPrefix +"/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdatesExistingBookReturnsUpdatedBook() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);

        BookEntity savedBook = bookService.savedBook(bookEntityA);

        bookEntityA.setTitle("UPDATED");
        String bookDtoJson = objectMapper.writeValueAsString(bookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPrefix +"/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.success").isBoolean()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data").isMap()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.isbn").value(savedBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.title").value(bookEntityA.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").isString()
        );
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus404WhenNoBookExist() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);
        bookService.savedBook(bookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(apiPrefix +"/books/5675674574")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus200() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);
        bookService.savedBook(bookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(apiPrefix +"/books/" + bookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }


}
