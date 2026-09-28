CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE director (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    UNIQUE (first_name, last_name)
);

CREATE TABLE movie (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    year INTEGER NOT NULL,
    age_limit INTEGER NOT NULL,
    rating DECIMAL NOT NULL,
    synopsis TEXT NOT NULL,
    director_id UUID REFERENCES director(id) ON DELETE SET NULL
);

CREATE TABLE genre (
    name TEXT PRIMARY KEY
);

CREATE TABLE movie__genre (
    movie_id UUID REFERENCES movie(id) ON DELETE CASCADE,
    genre_id TEXT REFERENCES genre(name) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, genre_id)
);

CREATE TABLE actor (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    UNIQUE (first_name, last_name)
);

CREATE TABLE movie__actor (
    movie_id UUID REFERENCES movie(id) ON DELETE CASCADE,
    actor_id UUID REFERENCES actor(id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, actor_id)
);



-- Delete orphan actors when a movie__actor entry is deleted.
--
-- This also handles the case where a movie is deleted:
-- movie
--   -> CASCADE deletes movie__actor entries
--   -> this trigger checks whether the actor is still referenced
--   -> if not, the actor is deleted
CREATE OR REPLACE FUNCTION delete_orphan_actor()
RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM actor
    WHERE id = OLD.actor_id
      AND NOT EXISTS (
          SELECT 1
          FROM movie__actor
          WHERE actor_id = OLD.actor_id
      );

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER movie_actor_delete_orphan
    AFTER DELETE ON movie__actor
    FOR EACH ROW
    EXECUTE FUNCTION delete_orphan_actor();


-- Delete orphan directors when a movie is deleted or its director changes.
--
-- If the deleted/previously assigned director is no longer referenced
-- by any movie, the director is deleted.
CREATE OR REPLACE FUNCTION delete_orphan_director()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.director_id IS NOT NULL THEN
        DELETE FROM director
        WHERE id = OLD.director_id
          AND NOT EXISTS (
              SELECT 1
              FROM movie
              WHERE director_id = OLD.director_id
          );
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER movie_delete_orphan_director
    AFTER DELETE OR UPDATE OF director_id ON movie
    FOR EACH ROW
    EXECUTE FUNCTION delete_orphan_director();
