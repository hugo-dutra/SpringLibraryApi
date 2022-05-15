package com.library.libraryAPI.service;

import com.library.libraryAPI.api.DTO.BookDTO;
import com.library.libraryAPI.model.Entity.Book;

public interface BookService {
    Book save(Book book);
}
