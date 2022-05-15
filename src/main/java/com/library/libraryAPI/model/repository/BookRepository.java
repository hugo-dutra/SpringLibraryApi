package com.library.libraryAPI.model.repository;

import com.library.libraryAPI.model.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
    boolean existsByIsbn(String isbn);
}
