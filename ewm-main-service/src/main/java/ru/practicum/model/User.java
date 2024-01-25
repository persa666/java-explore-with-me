package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "permission", nullable = false)
    private Boolean permission = true;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User(Long id) {
        this.id = id;
    }
}
