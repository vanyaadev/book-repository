package com.vanyaadev.bookrepository.service;

import com.vanyaadev.bookrepository.exception.ResourceAlreadyExistsException;
import com.vanyaadev.bookrepository.exception.ResourceNotFoundException;
import com.vanyaadev.bookrepository.model.Author;
import com.vanyaadev.bookrepository.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getAuthorById(Long id) {
        return returnAuthorIfExistsById(id);
    }

    public Author getAuthorByName(String authorName) {
        return returnAuthorIfExistsByName(authorName);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author createAuthor(Author author) {
        if (authorRepository.findByName(author.getName()).isPresent())
            throw new ResourceAlreadyExistsException("Author already exists!");
        else
            return authorRepository.save(author);
    }

    public Author updateAuthor(Long id, Author author) {
        Author authorToUpdate = returnAuthorIfExistsById(id);
        authorToUpdate.setName(author.getName());
        return authorRepository.save(authorToUpdate);
    }

    public Author deleteAuthorById(Long id) {
        Author authorToDelete = returnAuthorIfExistsById(id);
        authorRepository.deleteById(id);
        return authorToDelete;
    }

    Author returnAuthorIfExistsById(Long id) {
        if (authorRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Author with id: {" + id + "} not found!");
        } else
            return authorRepository.findById(id).get();
    }

    Author returnAuthorIfExistsByName(String authorName) {
        if (authorRepository.findByName(authorName).isEmpty()) {
            throw new ResourceNotFoundException("Author with name: {" + authorName + "} not found!");
        } else
            return authorRepository.findByName(authorName).get();
    }


}




