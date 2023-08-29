package com.example.librarymanagementsystem.Services;

import com.example.librarymanagementsystem.CustomExceptions.BookNotAvailableException;
import com.example.librarymanagementsystem.CustomExceptions.BookNotFoundException;
import com.example.librarymanagementsystem.Enums.Genre;
import com.example.librarymanagementsystem.Enums.TransactionStatus;
import com.example.librarymanagementsystem.Enums.TransactionType;
import com.example.librarymanagementsystem.Models.Author;
import com.example.librarymanagementsystem.Models.Book;
import com.example.librarymanagementsystem.Models.LibraryCard;
import com.example.librarymanagementsystem.Models.Transaction;
import com.example.librarymanagementsystem.Repositories.AuthorRepository;
import com.example.librarymanagementsystem.Repositories.BookRepository;
import com.example.librarymanagementsystem.Repositories.CardRepository;
import com.example.librarymanagementsystem.Repositories.TransactionRepository;
import com.example.librarymanagementsystem.RequestDto.AddBookRequestDto;
import com.example.librarymanagementsystem.ResponseDto.BookResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class BookService {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;


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

    public List<BookResponseDto> getBookListByGenre(Genre genre){
        List<Book> bookList = bookRepository.findBooksByGenre(genre);
        List<BookResponseDto> responseList = new ArrayList<>();
        for(Book book: bookList){
            BookResponseDto bookResponseDto = new BookResponseDto(book.getTitle(),book.getIsAvailable(),
                                             book.getGenre(),book.getPublicationDate(),book.getPrice(),
                                             book.getAuthor().getName());
            responseList.add(bookResponseDto);
        }
        return responseList;
    }

    public String returnBook(Integer bookId, Integer cardId){
        Book book = bookRepository.findById(bookId).get();
        LibraryCard card = cardRepository.findById(cardId).get();

        List<Transaction> transactionList = transactionRepository.findTransactionsByBookAndLibraryCardAndTransactionStatusAndTransactionType(book,card, TransactionStatus.SUCCESS, TransactionType.ISSUE);

        Transaction latestTransaction = transactionList.get(transactionList.size()-1);

        Date issueDate = latestTransaction.getCreatedAt();

        long milliSecondTime = Math.abs(System.currentTimeMillis()-issueDate.getTime());

        long no_of_days_issued = TimeUnit.DAYS.convert(milliSecondTime, TimeUnit.MILLISECONDS);

        int fineAmount = 0;

        if(no_of_days_issued > 15){
            fineAmount = (int) ((no_of_days_issued-15)*5);
        }

        book.setIsAvailable(Boolean.TRUE);
        card.setNoOfBooksIssued(card.getNoOfBooksIssued() - 1);

        Transaction transaction = new Transaction(TransactionStatus.SUCCESS,TransactionType.RETURN,fineAmount);

        transaction.setBook(book);
        transaction.setLibraryCard(card);

        Transaction newTransactionWithId = transactionRepository.save(transaction);

        book.getTransactionList().add(newTransactionWithId);
        card.getTransactionList().add(newTransactionWithId);

        //saving the parents

        bookRepository.save(book);
        cardRepository.save(card);

        return "Book has successfully been returned";



    }
}
