package com.movies.movieservice.repository;

import com.movies.movieservice.repository.entity.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    @Query(value = """
            SELECT *
            FROM movie
            WHERE name ILIKE CONCAT('%', :search, '%')
               OR similarity(name, :search) >= :threshold
            ORDER BY similarity(name, :search) DESC, name
            """, nativeQuery = true)
    List<Movie> findByNameFuzzy(
            @Param("search") String search,
            @Param("threshold") double threshold
    );
}
