package com.example.librarymanagementsystem.Services;

import com.example.librarymanagementsystem.CustomExceptions.BookNotAvailableException;
import com.example.librarymanagementsystem.CustomExceptions.BookNotFoundException;
import com.example.librarymanagementsystem.Models.Author;
import com.example.librarymanagementsystem.Models.Book;
import com.example.librarymanagementsystem.Repositories.AuthorRepository;
import com.example.librarymanagementsystem.Repositories.BookRepository;
import com.example.librarymanagementsystem.RequestDto.AddBookRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;


    public String addBook(AddBookRequestDto request) throws Exception {
        // validation
        //author id should be valid
        Optional<Author> optionalAuthor = authorRepository.findById(request.getAuthorId());
        if(!optionalAuthor.isPresent()){
            throw new Exception("Author id entered is incorrect");
        }

        Author author = optionalAuthor.get();

        Book book = new Book(request.getTitle(), request.getIsAvailable(), request.getGenre(), request.getPublicationDate(), request.getPrice());

        // Entities will go in the database and entities will only come out from the database.

        // Got the book object

        // need to set the FK variables

        // since it's a bidirectional mapping then need to set both in child and parent class.
        // set the parent entity in the child class
        book.setAuthor(author);
        // setting in parent
        List<Book> list = author.getBookList();
        list.add(book);
        author.setBookList(list);

        //I need to save them --->>>>>
        //save only the parent, child will automatically get saved
        authorRepository.save(author);

        return "Book has been successfully added and updated";

    }

    public String deleteBook(Integer bookId) throws Exception {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(!optionalBook.isPresent()){
            throw new Exception("Book is not available");
        }
        Book book = optionalBook.get();
        bookRepository.delete(book);
        return "Book has been deleted successfully";
    }
}
