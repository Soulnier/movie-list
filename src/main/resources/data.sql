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