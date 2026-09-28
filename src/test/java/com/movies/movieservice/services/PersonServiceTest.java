package com.movies.movieservice.services;

import com.movies.movieservice.model.CreateActorDto;
import com.movies.movieservice.model.CreateDirectorDto;
import com.movies.movieservice.repository.ActorRepository;
import com.movies.movieservice.repository.DirectorRepository;
import com.movies.movieservice.repository.entity.Actor;
import com.movies.movieservice.repository.entity.Director;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private DirectorRepository directorRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void getActorsReturnsActorDtos() {
        UUID actorId = UUID.randomUUID();
        Actor actor = mock(Actor.class);
        when(actor.getId()).thenReturn(actorId);
        when(actor.getFirstName()).thenReturn("Leonardo");
        when(actor.getLastName()).thenReturn("DiCaprio");
        when(actorRepository.findAll()).thenReturn(List.of(actor));

        var result = personService.getActors();

        assertThat(result).singleElement().satisfies(dto -> {
            assertThat(dto.getId()).isEqualTo(actorId);
            assertThat(dto.getFirstName()).isEqualTo("Leonardo");
            assertThat(dto.getLastName()).isEqualTo("DiCaprio");
        });
        verify(actorRepository).findAll();
    }

    @Test
    void getDirectorsReturnsDirectorDtos() {
        UUID directorId = UUID.randomUUID();
        Director director = mock(Director.class);
        when(director.getId()).thenReturn(directorId);
        when(director.getFirstName()).thenReturn("Christopher");
        when(director.getLastName()).thenReturn("Nolan");
        when(directorRepository.findAll()).thenReturn(List.of(director));

        var result = personService.getDirectors();

        assertThat(result).singleElement().satisfies(dto -> {
            assertThat(dto.getId()).isEqualTo(directorId);
            assertThat(dto.getFirstName()).isEqualTo("Christopher");
            assertThat(dto.getLastName()).isEqualTo("Nolan");
        });
        verify(directorRepository).findAll();
    }

    @Test
    void createActorMapsDtoAndSavesActor() {
        CreateActorDto dto = mock(CreateActorDto.class);
        when(dto.getFirstName()).thenReturn("Leonardo");
        when(dto.getLastName()).thenReturn("DiCaprio");
        ArgumentCaptor<Actor> captor = ArgumentCaptor.forClass(Actor.class);
        when(actorRepository.save(any(Actor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var result = personService.createActor(dto);

        verify(actorRepository).save(captor.capture());
        Actor saved = captor.getValue();
        assertThat(saved.getFirstName()).isEqualTo("Leonardo");
        assertThat(saved.getLastName()).isEqualTo("DiCaprio");
    }

    @Test
    void createDirectorMapsDtoAndSavesDirector() {
        CreateDirectorDto dto = mock(CreateDirectorDto.class);
        when(dto.getFirstName()).thenReturn("Christopher");
        when(dto.getLastName()).thenReturn("Nolan");
        ArgumentCaptor<Director> captor = ArgumentCaptor.forClass(Director.class);
        when(directorRepository.save(any(Director.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var result = personService.createDirector(dto);

        verify(directorRepository).save(captor.capture());
        Director saved = captor.getValue();
        assertThat(saved.getFirstName()).isEqualTo("Christopher");
        assertThat(saved.getLastName()).isEqualTo("Nolan");
        assertThat(result.getId()).isEqualTo(saved.getId());
    }
}
