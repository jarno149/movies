package com.movies.movieservice.repository;

import com.movies.movieservice.repository.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, String> {
}
