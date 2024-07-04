package com.devmau.book.services.impl;

import com.devmau.book.domain.entities.BookEntity;
import com.devmau.book.repositories.BookRepository;
import com.devmau.book.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity savedBook(BookEntity book) {
        return this.bookRepository.save(book);
    }

    @Override
    public Page<BookEntity> findAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

//    @Override
//    public List<BookEntity> findAllBooks() {
//        return  StreamSupport.stream(bookRepository
//                        .findAll()
//                        .spliterator(), false)
//                .collect(Collectors.toList());
//    }

    @Override
    public Optional<BookEntity> findOneBook(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public boolean isExists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdate(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepository.findById(isbn).map(existingBook -> {
            Optional.ofNullable(bookEntity.getTitle()).ifPresent(existingBook::setTitle);
            return bookRepository.save(existingBook);
        }).orElseThrow(() -> new RuntimeException("Book does not exist"));
    }

    @Override
    public void delete(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
