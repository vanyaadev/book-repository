package com.vanyaadev.bookrepository.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Valid
    @ManyToOne
    private Author author;

    @NotBlank(message = "Book title must not be empty")
    private String title;

    @Valid
    @ManyToOne
    private Firm firm;

    @Range(min = 1000, max = 2100, message = "Invalid publication year")
    @NotNull(message = "Invalid publication year")
    private Integer publicationYear;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    public Book(Author author, String title, Firm firm, Integer publicationYear) {
        this.author = author;
        this.title = title;
        this.firm = firm;
        this.publicationYear = publicationYear;
    }
}
