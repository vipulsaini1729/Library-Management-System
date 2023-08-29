package com.example.librarymanagementsystem.Repositories;

import com.example.librarymanagementsystem.Models.LibraryCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<LibraryCard, Integer> {
}
