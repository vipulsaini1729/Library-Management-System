package com.example.librarymanagementsystem.Controllers;

import com.example.librarymanagementsystem.Enums.Genre;
import com.example.librarymanagementsystem.Models.Book;
import com.example.librarymanagementsystem.RequestDto.AddBookRequestDto;
import com.example.librarymanagementsystem.ResponseDto.BookResponseDto;
import com.example.librarymanagementsystem.Services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;
    @PostMapping("/add")
    public ResponseEntity addBook(@RequestBody AddBookRequestDto addBookRequestDto){
    try{
        String result = bookService.addBook(addBookRequestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    catch (Exception e){
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
     }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteBook(@RequestParam("bookId") Integer bookId){
        try {
            String result = bookService.deleteBook(bookId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getByGenre")
    public ResponseEntity getBookListByGenre(@RequestParam("genre") Genre genre){
        List<BookResponseDto> responseDtoList = bookService.getBookListByGenre(genre);
        return new ResponseEntity<>(responseDtoList,HttpStatus.OK);
    }
}
