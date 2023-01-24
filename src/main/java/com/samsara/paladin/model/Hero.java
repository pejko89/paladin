package com.samsara.paladin.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    public static final int STANDARD_COLUMN_LENGTH = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = STANDARD_COLUMN_LENGTH, nullable = false, unique = true)
    private String name;

    @Column(name = "type", length = STANDARD_COLUMN_LENGTH, nullable = false)
    private String type;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "created", nullable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private Date created;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
