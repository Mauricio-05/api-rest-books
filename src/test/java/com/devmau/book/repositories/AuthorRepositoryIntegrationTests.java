package com.devmau.book.repositories;

import com.devmau.book.TestDataUtil;
import com.devmau.book.domain.entities.AuthorEntity;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorRepositoryIntegrationTests {

    private final AuthorRepository underTest;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled(){

        AuthorEntity author = TestDataUtil.createTestAuthorA();

        underTest.save(author);
        Optional<AuthorEntity> result = underTest.findById(author.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(author);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled(){

        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        AuthorEntity authorC = TestDataUtil.createTestAuthorC();

        List<AuthorEntity> authors = new ArrayList<AuthorEntity>(List.of(authorA,authorB,authorC));

        underTest.saveAll(authors);

        Iterable<AuthorEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(authorA, authorB, authorC);
    }

    @Test
    public void testThatAuthorCanBeUpdateAndRecalled(){

        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        underTest.save(authorA);

        authorA.setAge(82);
        underTest.save(authorA);

        Optional<AuthorEntity> result = underTest.findById(authorA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorA);

    }

    @Test
    public void testThatAuthorCanBeDeleted(){
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();

        underTest.save(authorA);
        underTest.delete(authorA);
        Optional<AuthorEntity> result = underTest.findById(authorA.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void testThatAuthorGetAuthorsWithAgeLessThan(){

        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        AuthorEntity authorC = TestDataUtil.createTestAuthorC();


        List<AuthorEntity> authors = new ArrayList<AuthorEntity>(List.of(authorA,authorB,authorC));

        underTest.saveAll(authors);

        Iterable<AuthorEntity> result = underTest.ageLessThan(50);

        assertThat(result).containsExactly(authorA, authorC);

    }

    @Test
    public void testThatAuthorGetAuthorsWithAgeGreaterThan(){
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        AuthorEntity authorC = TestDataUtil.createTestAuthorC();

        List<AuthorEntity> authors = new ArrayList<>(List.of(authorA,authorB,authorC));

        Iterable<AuthorEntity> authorsSaved = underTest.saveAll(authors);

        Iterable<AuthorEntity> result = underTest.findAuthorsWithAgeGreaterThan(50);

        List<AuthorEntity> authorsSavedList = StreamSupport.stream(authorsSaved.spliterator(), false).toList();

        assertThat(result).containsExactly(authorsSavedList.get(1));

    }
}
