package com.example.librarymanagementsystem.Services;

import com.example.librarymanagementsystem.Models.Author;
import com.example.librarymanagementsystem.Repositories.AuthorRepository;
import com.example.librarymanagementsystem.RequestDto.UpdateNameAndPenNameRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public String addAuthor(Author author) throws Exception {
        if(author.getAuthorId()!=null){
            throw new Exception("Author Id should not be sent as the parameter");
        }
        authorRepository.save(author);
        return "Author has been successfully added to the db";
    }

    public String updateNameAndPenName (UpdateNameAndPenNameRequestDto request) throws Exception {
        Optional<Author> authorOptional = authorRepository.findById(request.getAuthorId());
        if(!authorOptional.isPresent()){
            throw new Exception("Author id is Invalid");
        }
        Author author = authorOptional.get();
        author.setName(request.getNewName());
        author.setPenName(request.getNewName());

        authorRepository.save(author);

        return "Author Name and PenName has been updated";
    }

    public Author getAuthor(Integer authorId){
        Author author = authorRepository.findById(authorId).get();
        return author;
    }
}
