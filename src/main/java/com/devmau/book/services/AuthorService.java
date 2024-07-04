package com.devmau.book.services;

import com.devmau.book.domain.entities.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorEntity saveAuthor(AuthorEntity author);

    Page<AuthorEntity> findAllAuthors(Pageable pageable);

    // List<AuthorEntity> findAllAuthors();

    Optional<AuthorEntity> findOneAuthor(Long id);

    boolean isExists(Long id);

    AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity);

    void delete(Long id);
}
