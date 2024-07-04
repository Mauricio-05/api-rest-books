package com.devmau.book.mappers.impl;

import com.devmau.book.domain.dto.AuthorDto;
import com.devmau.book.domain.dto.BookDto;
import com.devmau.book.domain.entities.AuthorEntity;
import com.devmau.book.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthorMapperImpl implements Mapper<AuthorEntity, AuthorDto> {

    private final ModelMapper modelMapper;

    public AuthorMapperImpl(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthorDto mapTo(AuthorEntity authorEntity) {
        AuthorDto authorDto = modelMapper.map(authorEntity, AuthorDto.class);

//        if(!authorDto.getBooks().isEmpty()){
//            authorDto.getBooks().forEach(bookDto -> {
//                bookDto.setAuthor(null);
//            });
//        }

        return authorDto;

    }

    @Override
    public AuthorEntity mapFrom(AuthorDto authorDto) {
        return modelMapper.map(authorDto, AuthorEntity.class);
    }
}
