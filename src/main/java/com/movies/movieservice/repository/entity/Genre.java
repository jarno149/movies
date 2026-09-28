package com.movies.movieservice.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "genre")
@Getter
@NoArgsConstructor
public class Genre {

    @Id
    private String name;

    public Genre(String name) {
        this.name = name;
    }
}
