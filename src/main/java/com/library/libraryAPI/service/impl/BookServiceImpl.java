package com.library.libraryAPI.service.impl;

import com.library.libraryAPI.exception.BusinessException;
import com.library.libraryAPI.model.Entity.Book;
import com.library.libraryAPI.model.repository.BookRepository;
import com.library.libraryAPI.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())){
            throw new BusinessException("Isbn já cadastrado");
        }
        return repository.save(book);
    }


}
