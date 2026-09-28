package com.movies.movieservice.services;

import com.movies.movieservice.model.ActorDto;
import com.movies.movieservice.model.CreateMovieDto;
import com.movies.movieservice.model.DirectorDto;
import com.movies.movieservice.model.MovieDto;
import com.movies.movieservice.repository.MovieRepository;
import com.movies.movieservice.repository.ActorRepository;
import com.movies.movieservice.repository.DirectorRepository;
import com.movies.movieservice.repository.GenreRepository;
import com.movies.movieservice.repository.entity.Actor;
import com.movies.movieservice.repository.entity.Director;
import com.movies.movieservice.repository.entity.Genre;
import com.movies.movieservice.repository.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService {

    private static final double FUZZY_SEARCH_THRESHOLD = 0.2;

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovieDto> searchMovies(String search) {
        String normalizedSearch = search.trim();
        if (normalizedSearch.isEmpty()) {
            throw new IllegalArgumentException("Search string cannot be null or empty");
        }

        return movieRepository.findByNameFuzzy(normalizedSearch, FUZZY_SEARCH_THRESHOLD).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public MovieDto createMovie(CreateMovieDto dto) {
        Director director = dto.getDirectorId() == null
                ? null
                : directorRepository.getReferenceById(dto.getDirectorId());
        List<Actor> actors = distinctIds(dto.getActorIds()).stream()
                .map(actorRepository::getReferenceById)
                .toList();
        List<Genre> genres = distinctValues(dto.getGenres()).stream()
                .map(genreRepository::getReferenceById)
                .toList();

        Movie movie = Movie.fromDto(dto, director, genres, actors);
        return toDto(movieRepository.save(movie));
    }

    private List<UUID> distinctIds(List<UUID> ids) {
        return ids == null ? Collections.emptyList() : ids.stream().distinct().toList();
    }

    private List<String> distinctValues(List<String> values) {
        return values == null ? Collections.emptyList() : values.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(String::trim)
                .distinct()
                .toList();
    }

    private MovieDto toDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .name(movie.getName())
                .year(movie.getYear())
                .ageLimit(movie.getAgeLimit())
                .rating(movie.getRating())
                .synopsis(movie.getSynopsis())
                .genres(movie.getGenres().stream()
                        .map(Genre::getName)
                        .toList())
                .director(toDirectorDto(movie.getDirector()))
                .actors(movie.getActors().stream()
                        .map(this::toActorDto)
                        .toList())
                .build();
    }

    private DirectorDto toDirectorDto(Director director) {
        if (director == null) {
            return null;
        }

        return DirectorDto.builder()
                .id(director.getId())
                .firstName(director.getFirstName())
                .lastName(director.getLastName())
                .build();
    }

    private ActorDto toActorDto(Actor actor) {
        return ActorDto.builder()
                .id(actor.getId())
                .firstName(actor.getFirstName())
                .lastName(actor.getLastName())
                .build();
    }
}
