package com.movies.movieservice.model;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class CreateMovieDto {

    private String name;
    private Integer year;
    private Integer ageLimit;
    private Float rating;
    private String synopsis;
    private UUID directorId;
    private List<String> genres;
    private List<UUID> actorIds;

}
