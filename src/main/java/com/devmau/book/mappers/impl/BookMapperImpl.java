package com.devmau.book.mappers.impl;

import com.devmau.book.domain.dto.AuthorDto;
import com.devmau.book.domain.dto.BookDto;
import com.devmau.book.domain.entities.BookEntity;
import com.devmau.book.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BookMapperImpl implements Mapper<BookEntity, BookDto> {

    private final ModelMapper modelMapper;

    public BookMapperImpl(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDto mapTo(BookEntity bookEntity) {
        BookDto bookDto = modelMapper.map(bookEntity, BookDto.class);
//        if(bookDto.getAuthor() != null){
//            bookDto.getAuthor().setBooks(null);
//        }
        return  bookDto;
    }

    @Override
    public BookEntity mapFrom(BookDto bookDto) {
        return modelMapper.map(bookDto, BookEntity.class);
    }
}
