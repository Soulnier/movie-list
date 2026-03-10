CREATE TABLE IF NOT EXISTS genres (
id SERIAL PRIMARY KEY,
name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS statuses (
id SERIAL PRIMARY KEY,
name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS movies (
id SERIAL PRIMARY KEY,
name TEXT NOT NULL,
release_year INTEGER,
watch_status_id INTEGER REFERENCES statuses(id) ON DELETE SET NULL,
user_rating INTEGER CHECK (user_rating >= 0 AND user_rating <= 10),
user_review TEXT,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS genre_movie (
movie_id INTEGER REFERENCES movies(id) ON DELETE CASCADE,
genre_id INTEGER REFERENCES genres(id) ON DELETE CASCADE,
PRIMARY KEY (movie_id, genre_id)
);

INSERT INTO genres (name) VALUES
('Боевик'),
('Драма'),
('Фантастика'),
('Ужасы'),
('Комедия'),
('Триллер'),
('Мелодрама'),
('Детектив'),
('Приключения'),
('Фэнтези')
ON CONFLICT (name) DO NOTHING;

INSERT INTO statuses (name) VALUES
('Запланировано'),
('Просматривается'),
('Просмотрено'),
('Брошено')
ON CONFLICT (name) DO NOTHING;
