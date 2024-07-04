package com.devmau.book.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    private String isbn;

    private String title;

    @ManyToOne(cascade = { CascadeType.PERSIST } )
    @JoinColumn( name = "author_id" )
    private AuthorEntity author;
}
