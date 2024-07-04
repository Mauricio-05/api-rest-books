package com.devmau.book.repositories;

import com.devmau.book.TestDataUtil;
import com.devmau.book.domain.entities.AuthorEntity;
import com.devmau.book.domain.entities.BookEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookRepositoryIntegrationTests {

    private final BookRepository underTest;

    @Autowired
    public BookRepositoryIntegrationTests(BookRepository underTest){
        this.underTest = underTest;
    }


    @Test
    public void testThatBookCanBeCreatedAndRecalled(){

        AuthorEntity author = TestDataUtil.createTestAuthorA();
        BookEntity book = TestDataUtil.createTestBookA(author);

        BookEntity bookSaved = underTest.save(book);

        Optional<BookEntity> result = underTest.findById(book.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookSaved);
    }

    @Test
    public void testThatMultiplesBookCanBeCreatedAndRecalled(){

        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        AuthorEntity authorC = TestDataUtil.createTestAuthorC();

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        BookEntity bookB = TestDataUtil.createTestBookB(authorB);
        BookEntity bookC = TestDataUtil.createTestBookC(authorC);

        List<BookEntity> books = new ArrayList<>(List.of(bookA, bookB, bookC));

        Iterable<BookEntity> booksSaved = underTest.saveAll(books);

        Iterable<BookEntity> result = underTest.findAll();

        assertThat(result).hasSize(3).containsExactlyElementsOf(booksSaved);
    }

    @Test
    public void testThatBookCanBeUpdateAndRecalled(){

        AuthorEntity author = TestDataUtil.createTestAuthorA();
        BookEntity book = TestDataUtil.createTestBookA(author);

        underTest.save(book);

        book.setTitle("Update");

        BookEntity bookUpdated = underTest.save(book);

        Optional<BookEntity> result = underTest.findById(book.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookUpdated);
    }

    @Test
    public void  testThatBookCanBeDeleted(){
        AuthorEntity author = TestDataUtil.createTestAuthorA();

        BookEntity book = TestDataUtil.createTestBookA(author);
        underTest.save(book);
        underTest.delete(book);

        Optional<BookEntity> result  = underTest.findById(book.getIsbn());

        assertThat(result).isEmpty();
    }

}
