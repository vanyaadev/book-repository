package com.vanyaadev.bookrepository.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Username field is obligatory")
    private String username;

    @NotBlank(message = "Password field is obligatory")
    private String password;

    @Transient
    @NotBlank(message = "Confirm your password")
    private String confirmedPassword;

    public User(String username, String password, String confirmedPassword) {
        this.username = username;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
    }


}
