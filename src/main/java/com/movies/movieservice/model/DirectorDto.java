package com.movies.movieservice.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class DirectorDto {

    private UUID id;
    private String firstName;
    private String lastName;


}
