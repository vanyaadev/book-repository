package com.vanyaadev.bookrepository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Author field is obligatory")
    private String name;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private Set<Book> bookSet;

    public Author(String name) {
        this.name = name;
    }


}
