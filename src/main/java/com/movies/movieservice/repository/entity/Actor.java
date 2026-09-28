package com.movies.movieservice.repository.entity;

import com.movies.movieservice.model.CreateActorDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "actor")
@Getter
@NoArgsConstructor
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String firstName;

    private String lastName;

    public static Actor fromDto(CreateActorDto dto) {
        Actor actor = new Actor();
        actor.firstName = dto.getFirstName();
        actor.lastName = dto.getLastName();
        return actor;
    }
}
