package com.movies.movieservice.repository.entity;

import com.movies.movieservice.model.CreateDirectorDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "director")
@Getter
@NoArgsConstructor
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String firstName;

    private String lastName;

    public static Director fromDto(CreateDirectorDto dto) {
        Director director = new Director();
        director.firstName = dto.getFirstName();
        director.lastName = dto.getLastName();
        return director;
    }
}
