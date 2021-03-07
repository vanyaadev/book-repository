package com.vanyaadev.bookrepository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "firms")
@Getter
@Setter
@NoArgsConstructor
public class Firm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Firm name field is obligatory")
    private String name;

    @NotBlank(message = "Firm city field is obligatory")
    private String city;

    @OneToMany(mappedBy = "firm")
    @JsonIgnore
    private Set<Book> bookSet;

    public Firm(String name, String city) {
        this.name = name;
        this.city = city;
    }

}
