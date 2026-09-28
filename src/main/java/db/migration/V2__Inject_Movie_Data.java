package db.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

@Slf4j
public class V2__Inject_Movie_Data extends BaseJavaMigration {

    private static final String MOVIES_RESOURCE = "/movies-compact.json";

    @Override
    public void migrate(Context context) throws SQLException {
        log.info("Starting V2 migration: injecting movie data");

        JsonNode movies = readMovies();
        Connection connection = context.getConnection();
        for (JsonNode movie : movies) {
            UUID directorId = insertDirector(connection, movie.get("director"));
            UUID movieId = insertMovie(connection, movie, directorId);

            insertGenres(connection, movie.get("genres"), movieId);
            insertActors(connection, movie.get("actors"), movieId);
        }

        log.info("V2 migration completed successfully");
    }

    private JsonNode readMovies() throws SQLException {
        try (var source = V2__Inject_Movie_Data.class.getResourceAsStream(MOVIES_RESOURCE)) {
            if (source == null) {
                throw new SQLException("Resource not found: " + MOVIES_RESOURCE);
            }
            JsonNode movies = new ObjectMapper().readTree(source);
            if (movies == null || !movies.isArray()) {
                throw new SQLException("Movie resource must contain a JSON array");
            }
            return movies;
        } catch (IOException e) {
            throw new SQLException("Failed to read " + MOVIES_RESOURCE, e);
        }
    }

    private UUID insertDirector(Connection connection, JsonNode director) throws SQLException {
        try (var statement = connection.prepareStatement("""
                INSERT INTO director (first_name, last_name)
                VALUES (?, ?)
                ON CONFLICT (first_name, last_name) DO UPDATE
                    SET first_name = EXCLUDED.first_name
                RETURNING id
                """)) {
            setPersonParameters(statement, director);
            try (var result = statement.executeQuery()) {
                result.next();
                return result.getObject("id", UUID.class);
            }
        }
    }

    private UUID insertActor(Connection connection, JsonNode actor) throws SQLException {
        try (var statement = connection.prepareStatement("""
                INSERT INTO actor (first_name, last_name)
                VALUES (?, ?)
                ON CONFLICT (first_name, last_name) DO UPDATE
                    SET first_name = EXCLUDED.first_name
                RETURNING id
                """)) {
            setPersonParameters(statement, actor);
            try (var result = statement.executeQuery()) {
                result.next();
                return result.getObject("id", UUID.class);
            }
        }
    }

    private void setPersonParameters(java.sql.PreparedStatement statement, JsonNode person)
            throws SQLException {
        statement.setString(1, person.get("firstName").asString().trim());
        statement.setString(2, person.get("lastName").asString().trim());
    }

    private UUID insertMovie(Connection connection, JsonNode movie, UUID directorId)
            throws SQLException {
        try (var statement = connection.prepareStatement("""
                INSERT INTO movie (name, year, age_limit, rating, synopsis, director_id)
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id
                """)) {
            statement.setString(1, movie.get("name").asString());
            statement.setInt(2, movie.get("year").asInt());
            statement.setInt(3, movie.get("ageLimit").asInt());
            statement.setBigDecimal(4, BigDecimal.valueOf(movie.get("rating").asDouble()));
            statement.setString(5, movie.get("synopsis").asString());
            statement.setObject(6, directorId);

            try (var result = statement.executeQuery()) {
                result.next();
                return result.getObject("id", UUID.class);
            }
        }
    }

    private void insertGenres(Connection connection, JsonNode genres, UUID movieId)
            throws SQLException {
        try (var genreStatement = connection.prepareStatement("""
                INSERT INTO genre (name)
                VALUES (?)
                ON CONFLICT (name) DO NOTHING
                """);
             var relationStatement = connection.prepareStatement("""
                INSERT INTO movie__genre (movie_id, genre_id)
                VALUES (?, ?)
                """)) {
            for (JsonNode genre : genres) {
                String genreName = genre.asString().trim();
                genreStatement.setString(1, genreName);
                genreStatement.addBatch();

                relationStatement.setObject(1, movieId);
                relationStatement.setString(2, genreName);
                relationStatement.addBatch();
            }
            genreStatement.executeBatch();
            relationStatement.executeBatch();
        }
    }

    private void insertActors(Connection connection, JsonNode actors, UUID movieId)
            throws SQLException {
        try (var relationStatement = connection.prepareStatement("""
                INSERT INTO movie__actor (movie_id, actor_id)
                VALUES (?, ?)
                """)) {
            for (JsonNode actor : actors) {
                UUID actorId = insertActor(connection, actor);
                relationStatement.setObject(1, movieId);
                relationStatement.setObject(2, actorId);
                relationStatement.addBatch();
            }
            relationStatement.executeBatch();
        }
    }
}
