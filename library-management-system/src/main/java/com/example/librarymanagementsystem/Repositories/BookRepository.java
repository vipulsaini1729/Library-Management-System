package com.example.librarymanagementsystem.Repositories;


import com.example.librarymanagementsystem.Enums.Genre;
import com.example.librarymanagementsystem.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByGenre(Genre genre);
}
