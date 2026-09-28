package com.movies.movieservice.repository.entity;

import com.movies.movieservice.model.CreateMovieDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "movie")
@Getter
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String name;

    private Integer year;

    @Column(name = "age_limit")
    private Integer ageLimit;

    private Float rating;

    private String synopsis;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    @ManyToMany
    @JoinTable(
            name = "movie__genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "movie__actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors = new ArrayList<>();

    public static Movie fromDto(
            CreateMovieDto dto,
            Director director,
            List<Genre> genres,
            List<Actor> actors
    ) {
        Movie movie = new Movie();
        movie.name = dto.getName();
        movie.year = dto.getYear();
        movie.ageLimit = dto.getAgeLimit();
        movie.rating = dto.getRating();
        movie.synopsis = dto.getSynopsis();
        movie.director = director;
        movie.genres = genres;
        movie.actors = actors;
        return movie;
    }
}
