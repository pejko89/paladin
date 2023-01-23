package com.samsara.paladin.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "heroes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = 60, nullable = false, unique = true)
    private String name;

    @Column(name = "type", length = 60, nullable = false)
    private String type;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "created", nullable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private Date created;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User user;

}
