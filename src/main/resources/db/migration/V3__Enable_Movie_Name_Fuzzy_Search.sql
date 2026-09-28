CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- AI's suggestion for fuzzy-search
CREATE INDEX movie_name_trgm_idx
    ON movie USING gin (name gin_trgm_ops);
