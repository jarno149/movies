package com.movies.movieservice.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class MovieDto {

    private UUID id;
    private String name;
    private Integer year;
    private Integer ageLimit;
    private Float rating;
    private String synopsis;
    private List<String> genres;
    private DirectorDto director;
    private List<ActorDto> actors;


}
