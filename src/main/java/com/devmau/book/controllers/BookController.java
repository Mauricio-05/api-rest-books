package com.devmau.book.controllers;

import com.devmau.book.config.ApiResponseConfig;

import com.devmau.book.domain.dto.BookDto;

import com.devmau.book.domain.entities.BookEntity;
import com.devmau.book.mappers.Mapper;
import com.devmau.book.services.BookService;
import com.devmau.book.utils.ApiResponseSuccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${apiPrefix}/books")
public class BookController {

    private final BookService bookService;

    private final Mapper<BookEntity, BookDto> bookMapper;

    public BookController(Mapper<BookEntity, BookDto> bookMapper, BookService bookService){
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccess<BookDto>> createBook (@RequestBody() BookDto bookDto){

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);

        BookEntity savedBookEntity = bookService.savedBook(bookEntity);

        return new ResponseEntity<>(ApiResponseConfig.success(
                HttpStatus.CREATED.value(),
                bookMapper.mapTo(savedBookEntity),
                "Book Created"),
                HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccess<Page<BookDto>>> listBooks (Pageable pageable){

        Page<BookEntity> books = bookService.findAllBooks(pageable);

        return new ResponseEntity<>(ApiResponseConfig.success(
                HttpStatus.OK.value(),
                books.map(bookMapper::mapTo),
                "Books lists"),
                HttpStatus.OK);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<ApiResponseSuccess<BookDto>> getBook (@PathVariable("isbn") String isbn){

        Optional<BookEntity> book = bookService.findOneBook(isbn);

        return book.map(bookEntity -> {

            BookDto bookDto = bookMapper.mapTo(bookEntity);

            return new ResponseEntity<>(ApiResponseConfig.success(
                    HttpStatus.OK.value(),
                    bookDto,
                    "Book list"),
                    HttpStatus.OK);

        }).orElse(new ResponseEntity<>(ApiResponseConfig.success(
                HttpStatus.NOT_FOUND.value(),
                null,
                "Not found"),
                HttpStatus.NOT_FOUND));

    }

    @PutMapping("/{isbn}")
    public ResponseEntity<ApiResponseSuccess<BookDto>> fullUpdateBook (@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {

        if(!bookService.isExists(isbn)){
            return new ResponseEntity<>(ApiResponseConfig.success(
                    HttpStatus.NOT_FOUND.value(),
                    null,
                    "Not Found"),
                    HttpStatus.NOT_FOUND);
        }

        bookDto.setIsbn(isbn);

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);

        BookEntity updateBook = bookService.savedBook(bookEntity);

        return new ResponseEntity<>(ApiResponseConfig.success(
                HttpStatus.OK.value(),
                bookMapper.mapTo(updateBook),
                "Book fullUpdate"),
                HttpStatus.OK);
    }


    @PatchMapping("/{isbn}")
    public ResponseEntity<ApiResponseSuccess<BookDto>> partialUpdate (@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {

        if(!bookService.isExists(isbn)){
            return new ResponseEntity<>(ApiResponseConfig.success(
                    HttpStatus.NOT_FOUND.value(),
                    null,
                    "Not Found"),
                    HttpStatus.NOT_FOUND);
        }

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);

        BookEntity updateBook = bookService.partialUpdate(isbn, bookEntity);

        return new ResponseEntity<>(ApiResponseConfig.success(
                HttpStatus.OK.value(),
                bookMapper.mapTo(updateBook),
                "Book PartialUpdate"),
                HttpStatus.OK);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<ApiResponseSuccess<Void>> deleteBook (@PathVariable("isbn") String isbn) {

        if(!bookService.isExists(isbn)){
            throw new ResourceAccessException("Book not found by isbn " + isbn);
        }

        bookService.delete(isbn);

        return  ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseConfig.success(
                        HttpStatus.OK.value(),
                        null,
                        "Book delete")
        );
    }

}
