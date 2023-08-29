package com.example.librarymanagementsystem.Repositories;

import com.example.librarymanagementsystem.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
