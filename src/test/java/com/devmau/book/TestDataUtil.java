package com.devmau.book;

import com.devmau.book.domain.entities.AuthorEntity;
import com.devmau.book.domain.entities.BookEntity;

public final class TestDataUtil {

    private TestDataUtil(){}

    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder().name("Abigail Rose").age(30).build();
    }


    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder().name("Thomas Rose").age(80).build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder().name("Jesse Rose").age(40).build();
    }

    public static BookEntity createTestBookA(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-0")
                .title("The shadow in the Attic")
                .author(author)
                .build();
    }

    public static BookEntity createTestBookB(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("978-1668-0377-13")
                .title("You Like It Darker: Stories")
                .author(author)
                .build();
    }

    public static BookEntity createTestBookC(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn(" 978-1668-0161-38")
                .title("Holly")
                .author(author)
                .build();
    }
}
