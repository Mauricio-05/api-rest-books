package com.devmau.book.controllers;

import com.devmau.book.TestDataUtil;
import com.devmau.book.domain.entities.AuthorEntity;
import com.devmau.book.services.AuthorService;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthorService authorService;

    private final String apiPrefix;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, Environment env, AuthorService authorService){
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.apiPrefix = env.getProperty("apiPrefix");
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception{
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(apiPrefix +"/authors").contentType(MediaType.APPLICATION_JSON).content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.success").isBoolean()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data").isMap()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.name").value(authorA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.age").value(authorA.getAge())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").isString()
        );
    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPrefix +"/authors").contentType(MediaType.APPLICATION_JSON)
                ).andExpect(
                        MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsReturnsListOAuthors() throws Exception {

        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        authorEntityA.setBooks(new ArrayList<>());

        authorService.saveAuthor(authorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPrefix +"/authors").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.success").isBoolean()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data").isMap()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.content[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.content[0].name").value(authorEntityA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.content[0].age").value(authorEntityA.getAge())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.content[0].books").isArray()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").isString()
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExist() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();

        AuthorEntity authorSaved = authorService.saveAuthor(authorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPrefix +"/authors/" + authorSaved.getId()).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorReturnsAuthorWhenAuthorExist() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        authorEntityA.setBooks(new ArrayList<>());

        AuthorEntity authorSaved = authorService.saveAuthor(authorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPrefix +"/authors/" + authorSaved.getId()).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.success").isBoolean()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data").isMap()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.id").value(authorSaved.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.name").value(authorEntityA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.age").value(authorEntityA.getAge())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.books").isArray()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").isString()
        ).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus404WhenNoAuthorExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPrefix +"/authors/16767454").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus404WhenNoAuthorExist() throws Exception {

        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();

        String authorDtoJson = objectMapper.writeValueAsString(authorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPrefix +"/authors/5675674574")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {

        AuthorEntity authorA = TestDataUtil.createTestAuthorA();

        AuthorEntity savedAuthor = authorService.saveAuthor(authorA);

        String authorDtoJson = objectMapper.writeValueAsString(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPrefix +"/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }


    @Test
    public void testThatFullUpdateUpdatesExistingAuthor() throws Exception {

        AuthorEntity authorA = TestDataUtil.createTestAuthorA();

        AuthorEntity savedAuthor = authorService.saveAuthor(authorA);

        AuthorEntity authorDto = TestDataUtil.createTestAuthorB();


        String authorDtoJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPrefix +"/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.success").isBoolean()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data").isMap()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.name").value(authorDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.age").value(authorDto.getAge())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").isString()
        );
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsHttpStatus200k() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.saveAuthor(authorA);


        authorA.setName("UPDATED");
        String authorDtoJson = objectMapper.writeValueAsString(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPrefix +"/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.saveAuthor(authorA);


        authorA.setName("UPDATED");
        String authorDtoJson = objectMapper.writeValueAsString(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPrefix +"/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.success").isBoolean()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data").isMap()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.name").value(authorA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.age").value(authorA.getAge())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").isString()
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus404WhenNoAuthorExist() throws Exception {

        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        authorService.saveAuthor(authorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(apiPrefix +"/authors/5675674574")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus200() throws Exception {

        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        AuthorEntity authorSaved  = authorService.saveAuthor(authorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(apiPrefix +"/authors/" + authorSaved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

}
