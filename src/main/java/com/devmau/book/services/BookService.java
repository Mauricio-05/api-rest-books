package com.devmau.book.services;

import com.devmau.book.domain.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookEntity savedBook(BookEntity book);

    Page<BookEntity> findAllBooks(Pageable pageable);

//    List<BookEntity> findAllBooks();

    Optional<BookEntity> findOneBook(String isbn);

    boolean isExists(String isbn);

    BookEntity partialUpdate(String isbn, BookEntity bookEntity);

    void delete(String isbn);
}
