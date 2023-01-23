package com.samsara.paladin.model;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", length = 60, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 60, nullable = false)
    private String lastName;

    @Column(name = "username", length = 60, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 300, nullable = false)
    private String password;

    @Column(name = "email", length = 60, nullable = false)
    private String email;

    @Column(name = "about", length = 600, nullable = false)
    private String about;

    @Column(name = "created", nullable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private Date created;

    @Column(name = "secret_question", length = 60, nullable = false)
    private String secretQuestion;

    @Column(name = "secret_answer", length = 60, nullable = false)
    private String secretAnswer;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    private Boolean tokenExpired;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Hero> heroes;

}
