package com.vanyaadev.bookrepository.service;

import com.vanyaadev.bookrepository.exception.ResourceAlreadyExistsException;
import com.vanyaadev.bookrepository.exception.ResourceNotFoundException;
import com.vanyaadev.bookrepository.model.Author;
import com.vanyaadev.bookrepository.model.Book;
import com.vanyaadev.bookrepository.model.Firm;
import com.vanyaadev.bookrepository.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final FirmService firmService;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorService authorService, FirmService firmService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.firmService = firmService;
    }


    public Book getBookById(Long id) {
        return returnBookIfExistsById(id);
    }

    public Book getBookByAuthorNameAndTitle(String authorName, String title) {
        return returnBookIfExistsByAuthorNameAndTitle(authorName, title);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book createBook(Book book, MultipartFile file) {
        var newBook = new Book();
        Author author = book.getAuthor();
        String authorName = author.getName();

        if (bookRepository
                .findByAuthorNameAndTitle(authorName, book.getTitle())
                .isPresent())
            throw new ResourceAlreadyExistsException("Book already exists!");

        Book preparedBook = prepareBookToProcess(book, newBook, authorName, file);

        return bookRepository.save(preparedBook);
    }

    public Book updateBook(Long id, Book newBook, MultipartFile file) {
        Book oldBook = returnBookIfExistsById(id);

        Author author = newBook.getAuthor();
        String authorName = author.getName();

        newBook = prepareBookToProcess(newBook, oldBook, authorName, file);

        Optional<Book> bookByAuthorNameAndTitle = bookRepository
                .findByAuthorNameAndTitle(
                        newBook.getAuthor().getName(),
                        newBook.getTitle());

        if (bookByAuthorNameAndTitle.isPresent()
                && (!id.equals(bookByAuthorNameAndTitle.get().getId())))
            throw new ResourceAlreadyExistsException("Book already exists!");

        return bookRepository.save(newBook);
    }

    public Book deleteBookById(Long id) {
        Book bookToDelete = returnBookIfExistsById(id);
        bookRepository.deleteById(id);

        return bookToDelete;
    }

    Book returnBookIfExistsById(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Book with id: {" + id + "} not found!");
        } else
            return bookRepository.findById(id).get();
    }

    Book returnBookIfExistsByAuthorNameAndTitle(String authorName, String title) {
        if (bookRepository.findByAuthorNameAndTitle(authorName, title).isEmpty()) {
            throw new ResourceNotFoundException("Book with author: {" + authorName + "} and title:{" + title + "}) not found!");

        } else
            return bookRepository.findByAuthorNameAndTitle(authorName, title).get();
    }


    Book prepareBookToProcess(Book processingBook,
                              Book bookToSave,
                              String authorName,
                              MultipartFile file) {

        setBookAuthorFromDBorCreateNewIfNotExists(bookToSave, authorName);
        setBookFirmFromDBorCreateNewIfNotExists(processingBook, bookToSave);

        bookToSave.setTitle(processingBook.getTitle());
        bookToSave.setPublicationYear(processingBook.getPublicationYear());

        setBookImageIfNotEmpty(bookToSave, file);

        return bookToSave;
    }

    private void setBookAuthorFromDBorCreateNewIfNotExists(Book bookToSave, String authorName) {
        try {
            Author authorByName = authorService.getAuthorByName(authorName);
            bookToSave.setAuthor(authorByName);

        } catch (ResourceNotFoundException e) {
            var newAuthor = new Author(authorName);
            bookToSave.setAuthor(newAuthor);
            authorService.createAuthor(newAuthor);
        }
    }

    private void setBookFirmFromDBorCreateNewIfNotExists(Book processingBook, Book bookToSave) {
        Firm firm = processingBook.getFirm();
        String firmName = firm.getName();
        String firmCity = firm.getCity();

        try {
            Firm firmByNameAndCity = firmService.getFirmByNameAndCity(firmName, firmCity);
            bookToSave.setFirm(firmByNameAndCity);

        } catch (ResourceNotFoundException e) {
            var newFirm = new Firm(firmName, firmCity);
            bookToSave.setFirm(newFirm);
            firmService.createFirm(newFirm);
        }
    }

    private void setBookImageIfNotEmpty(Book bookToSave, MultipartFile file) {
        String fileName = StringUtils
                .cleanPath(file.getOriginalFilename());
        
        if (!file.isEmpty()) {
            try {
                bookToSave.setImage(Base64
                        .getEncoder()
                        .encodeToString(file.getBytes()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}