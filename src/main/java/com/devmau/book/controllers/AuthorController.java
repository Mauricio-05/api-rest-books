package com.devmau.book.controllers;


import com.devmau.book.config.ApiResponseConfig;
import com.devmau.book.domain.dto.AuthorDto;
import com.devmau.book.domain.entities.AuthorEntity;
import com.devmau.book.mappers.Mapper;
import com.devmau.book.services.AuthorService;
import com.devmau.book.utils.ApiResponseSuccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

@RestController
@RequestMapping("${apiPrefix}/authors")
public class AuthorController {

    private final AuthorService authorService;

    private final Mapper<AuthorEntity, AuthorDto> authorMapper;
  
    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;

    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccess<AuthorDto>> createAuthor (@RequestBody AuthorDto author){

        AuthorEntity authorEntity = authorMapper.mapFrom(author);

        AuthorEntity savedAuthorEntity = authorService.saveAuthor(authorEntity);

        return new ResponseEntity<>(
                ApiResponseConfig.success(
                HttpStatus.CREATED.value(),
                authorMapper.mapTo(savedAuthorEntity),
                "Author Created"),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccess<Page<AuthorDto>>> listAuthors (Pageable pageable){

        Page<AuthorEntity> authors = authorService.findAllAuthors(pageable);

        return new ResponseEntity<>(
                ApiResponseConfig.success(
                HttpStatus.OK.value(),
                authors.map(authorMapper::mapTo),
                "Authors lists"),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccess<AuthorDto>> getAuthor (@PathVariable("id") Long id){

        Optional<AuthorEntity> author = authorService.findOneAuthor(id);

        return author.map(authorEntity -> {
           AuthorDto authorDto = authorMapper.mapTo(authorEntity);

           return ResponseEntity.status(HttpStatus.OK).body(
                   ApiResponseConfig.success(
                   HttpStatus.OK.value(),
                   authorDto,
                   "Author list")
           );

        }).orElseThrow(() -> new ResourceAccessException("Author not found by id " + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccess<AuthorDto>> fullUpdateAuthor (@PathVariable("id") Long id, @RequestBody AuthorDto author) {

        if(!authorService.isExists(id)){
            throw new ResourceAccessException("Author not found by id" + id);
        }

        author.setId(id);

        AuthorEntity authorEntity = authorMapper.mapFrom(author);

        AuthorEntity updateAuthor = authorService.saveAuthor(authorEntity);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseConfig.success(
                HttpStatus.OK.value(),
                authorMapper.mapTo(updateAuthor),
                "Author fullUpdate")
        );
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseSuccess<AuthorDto>> partialUpdate (@PathVariable("id") Long id, @RequestBody AuthorDto authorDto) {

        if(!authorService.isExists(id)){
            throw new ResourceAccessException("Author not found by id " + id);
        }

        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);

        AuthorEntity updateAuthor = authorService.partialUpdate(id, authorEntity);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseConfig.success(
                HttpStatus.OK.value(),
                authorMapper.mapTo(updateAuthor),
                "Author partial update")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccess<Void>> deleteAuthor (@PathVariable("id") Long id) {

        if(!authorService.isExists(id)){
            throw new ResourceAccessException("Author not found by id " + id);
        }

        authorService.delete(id);

        return  ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseConfig.success(
                HttpStatus.OK.value(),
                null,
                "Author delete")
        );
    }
}
