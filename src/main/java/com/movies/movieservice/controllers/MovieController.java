package com.movies.movieservice.controllers;

import com.movies.movieservice.model.*;
import com.movies.movieservice.services.PersonService;
import com.movies.movieservice.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies")
@Tag(name = "Movies", description = "Movie, actor and director endpoints")
public class MovieController {

    private final MovieService movieService;
    private final PersonService personService;

    @GetMapping()
    @Operation(
            summary = "List movies",
            description = "Returns all movies or performs fuzzy name search when search is provided."
    )
    @ApiResponse(responseCode = "200", description = "Movies returned successfully")
    public List<MovieDto> getMovies(@RequestParam(required = false) String search) {
        if(search != null && !search.isEmpty()) {
            return movieService.searchMovies(search);
        }
        return movieService.getAllMovies();
    }

    @PostMapping
    @Operation(summary = "Create movie", description = "Creates a movie and its relations to existing people and genres.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or unknown related ID")
    })
    public MovieDto createMovie(@RequestBody CreateMovieDto dto) {
        return movieService.createMovie(dto);
    }

    @GetMapping("/actors")
    @Operation(summary = "List actors")
    @ApiResponse(responseCode = "200", description = "Actors returned successfully")
    public List<ActorDto> getActors() {
        return personService.getActors();
    }

    @GetMapping("/directors")
    @Operation(summary = "List directors")
    @ApiResponse(responseCode = "200", description = "Directors returned successfully")
    public List<DirectorDto> getDirectors() {
        return personService.getDirectors();
    }

    @PostMapping("/actor")
    @Operation(summary = "Create actor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ActorDto createActor(@RequestBody CreateActorDto dto) {
        return personService.createActor(dto);
    }

    @PostMapping("/director")
    @Operation(summary = "Create director")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Director created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public DirectorDto createDirector(@RequestBody CreateDirectorDto dto) {
        return personService.createDirector(dto);
    }

}
