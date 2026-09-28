package com.movies.movieservice.repository;

import com.movies.movieservice.repository.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActorRepository extends JpaRepository<Actor, UUID> {
}
