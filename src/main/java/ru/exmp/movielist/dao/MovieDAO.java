package ru.exmp.movielist.dao;

import ru.exmp.movielist.model.Movie;
import ru.exmp.movielist.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAO {

    public static final MovieDAO INSTANCE = new MovieDAO();

    private MovieDAO() {}

    public static MovieDAO getInstance() {
        return INSTANCE;
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = """
                SELECT id, name, release_year, watch_status_id, user_rating, user_review 
                FROM movies 
                ORDER BY name
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Movie m = new Movie();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                m.setReleaseYear(rs.getInt("release_year"));

                int statusId = rs.getInt("watch_status_id");
                if (!rs.wasNull()) {
                    m.setWatchStatusId(statusId);
                }

                int rating = rs.getInt("user_rating");
                if (!rs.wasNull()) {
                    m.setUserRating(rating);
                }

                m.setUserReview(rs.getString("user_review"));
                movies.add(m);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при получении всех фильмов " + e.getMessage());
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> getMoviesByGenreName(String genreName) {
        List<Movie> movies = new ArrayList<>();
        String sql = """
                SELECT m.id, m.name, m.release_year, m.watch_status_id, m.user_rating, m.user_review
                FROM movies m 
                JOIN genre_movie gm ON m.id = gm.movie_id
                JOIN genres g ON gm.genre_id = g.id
                WHERE g.name = ?
                ORDER BY m.name
                 """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genreName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movie m = new Movie();
                    m.setId(rs.getInt("id"));
                    m.setName(rs.getString("name"));
                    m.setReleaseYear(rs.getInt("release_year"));

                    int statusId = rs.getInt("watch_status_id");
                    if (!rs.wasNull()) {
                        m.setWatchStatusId(statusId);
                    }

                    int rating = rs.getInt("user_rating");
                    if (!rs.wasNull()) {
                        m.setUserRating(rating);
                    }

                    m.setUserReview(rs.getString("user_review"));
                    movies.add(m);
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении фильмов по жанру " + e.getMessage());
            e.printStackTrace();
        }
        return movies;
    }

    public boolean deleteMovieById(int id) {
        String sql = """
                DELETE 
                FROM movies 
                WHERE id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении фильма " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Movie> getMovieById(int id) {
        String sql = """
                SELECT id, name, release_year, watch_status_id, user_rating, user_review
                FROM movies 
                WHERE id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Movie m = new Movie();
                    m.setId(rs.getInt("id"));
                    m.setName(rs.getString("name"));
                    m.setReleaseYear(rs.getInt("release_year"));

                    int statusId = rs.getInt("watch_status_id");
                    if (!rs.wasNull()) {
                        m.setWatchStatusId(statusId);
                    }

                    int rating = rs.getInt("user_rating");
                    if (!rs.wasNull()) {
                        m.setUserRating(rating);
                    }

                    m.setUserReview(rs.getString("user_review"));
                    return Optional.of(m);
                }
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.out.println("Ошибка при поиске фильма " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean updateMovie(Movie m) {
        System.out.println("MovieDAO.updateMovie получил ID: " + m.getId());

        String sql = """
            UPDATE movies SET 
            name = ?, 
            release_year = ?, 
            watch_status_id = ?, 
            user_rating = ?, 
            user_review = ? 
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getName());
            ps.setInt(2, m.getReleaseYear());

            if (m.getWatchStatusId() != null) {
                ps.setInt(3, m.getWatchStatusId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (m.getUserRating() != null) {
                ps.setInt(4, m.getUserRating());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setString(5, m.getUserReview());
            ps.setInt(6, m.getId());

            int affectedRows = ps.executeUpdate();
            System.out.println("Затронуто строк: " + affectedRows);
            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении фильма " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Movie saveMovie(Movie m) {
        String sql = """
                INSERT INTO movies 
                (name, release_year, watch_status_id, user_rating, user_review) 
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, m.getName());
            ps.setInt(2, m.getReleaseYear());

            if (m.getWatchStatusId() != null) {
                ps.setInt(3, m.getWatchStatusId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (m.getUserRating() != null) {
                ps.setInt(4, m.getUserRating());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setString(5, m.getUserReview());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    m.setId(generatedKeys.getInt(1));
                }
            }
            return m;

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении фильма " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при сохранении фильма", e);
        }
    }

    public void saveMovieGenres(int movieId, List<Integer> genreIds) {
        String deleteSql = "DELETE FROM genre_movie WHERE movie_id = ?";
        String insertSql = "INSERT INTO genre_movie (movie_id, genre_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
                deletePs.setInt(1, movieId);
                deletePs.executeUpdate();
            }

            if (genreIds != null && !genreIds.isEmpty()) {
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    for (Integer genreId : genreIds) {
                        insertPs.setInt(1, movieId);
                        insertPs.setInt(2, genreId);
                        insertPs.executeUpdate();
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении жанров фильма " + e.getMessage());
            e.printStackTrace();
        }
    }
}