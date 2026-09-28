package com.movies.movieservice.services;

import com.movies.movieservice.model.ActorDto;
import com.movies.movieservice.model.CreateActorDto;
import com.movies.movieservice.model.CreateDirectorDto;
import com.movies.movieservice.model.DirectorDto;
import com.movies.movieservice.repository.ActorRepository;
import com.movies.movieservice.repository.DirectorRepository;
import com.movies.movieservice.repository.entity.Actor;
import com.movies.movieservice.repository.entity.Director;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;

    public List<ActorDto> getActors() {
        return actorRepository.findAll().stream()
                .map(actor -> ActorDto.builder()
                        .id(actor.getId())
                        .firstName(actor.getFirstName())
                        .lastName(actor.getLastName())
                        .build())
                .toList();
    }

    public List<DirectorDto> getDirectors() {
        return directorRepository.findAll().stream()
                .map(director -> DirectorDto.builder()
                        .id(director.getId())
                        .firstName(director.getFirstName())
                        .lastName(director.getLastName())
                        .build())
                .toList();
    }

    public DirectorDto createDirector(CreateDirectorDto dto) {
        var director = Director.fromDto(dto);
        directorRepository.save(director);
        return DirectorDto.builder()
                .id(director.getId())
                .firstName(director.getFirstName())
                .lastName(director.getLastName())
                .build();
    }

    public ActorDto createActor(CreateActorDto dto) {
        var actor = Actor.fromDto(dto);
        actorRepository.save(actor);
        return ActorDto.builder()
                .id(actor.getId())
                .firstName(actor.getFirstName())
                .lastName(actor.getLastName())
                .build();
    }

}
