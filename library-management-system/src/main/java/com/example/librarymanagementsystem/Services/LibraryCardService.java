package com.example.librarymanagementsystem.Services;

import com.example.librarymanagementsystem.Models.LibraryCard;
import com.example.librarymanagementsystem.Models.Student;
import com.example.librarymanagementsystem.Repositories.CardRepository;
import com.example.librarymanagementsystem.Repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibraryCardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private StudentRepository studentRepository;
    public String addCard(LibraryCard libraryCard){
        cardRepository.save(libraryCard);
        return "Card has been successfully added";

    }

    public String associateToStudent(Integer cardNo,Integer rollNo) throws Exception{

        // Student should exist or not
        if(!studentRepository.existsById(rollNo)){
            throw new Exception("Student Id is invalid");
        }

        // card should also exist
        if(!cardRepository.existsById(cardNo)){
            throw new Exception("Card Id is Invalid");
        }

        // now i need to set those FK variables

        Optional<Student> optional = studentRepository.findById(rollNo);
        Student studentObj = optional.get();

        Optional<LibraryCard> optionalLibraryCard = cardRepository.findById(cardNo);
        LibraryCard libraryCard = optionalLibraryCard.get();

        //set the Student object in card object
        libraryCard.setStudent(studentObj);

        //since it's a bidirectional mapping
        //we need to set the library card object in student object

        studentObj.setLibraryCard(libraryCard);

        //any object that has been updated should be saved

        //save both of them
        studentRepository.save(studentObj);
//      cardRepository.save(libraryCard);
        //card repository saving can be skipped because student will automatically triggered for card repository save function

        return "student and card saved successfully";
    }
}
