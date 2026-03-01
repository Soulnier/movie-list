package ru.exmp.movielist.dao;

import ru.exmp.movielist.model.Genre;
import ru.exmp.movielist.model.Movie;
import ru.exmp.movielist.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "select id, name, release_year, watch_status_id, " +
                "user_rating, user_review from movies order by name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Movie m = new Movie();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                m.setReleaseYear(rs.getInt("release_year"));
                m.setWatchStatusId(rs.getInt("watch_status_id"));
                m.setUserRating(rs.getInt("user_rating"));
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
        String sql = "select m.id, m.name, m.release_year, m.watch_status_id, m.user_rating, m.user_review " +
                "from movies m join genre_movie gm on m.id = gm.movie_id " +
                "join genres g on gm.genre_id = g.id " +
                "where g.name = ? " +
                "order by m.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genreName);

            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movie m = new Movie();
                    m.setId(rs.getInt("id"));
                    m.setName(rs.getString("name"));
                    m.setReleaseYear(rs.getInt("release_year"));
                    m.setWatchStatusId(rs.getInt("watch_status_id"));
                    m.setUserRating(rs.getInt("user_rating"));
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

    public boolean deleteMovieById(int id)
    {
        String sql = "delete from movies where id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0)
            {
                System.out.println("Фильм с id " + id + " удалён");
                return true;
            } else {
                System.out.println("Фильм с id " + id + " не найден");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении фильма " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Movie getMovieById(int id){
        String sql = "Select id, name, release_year, watch_status_id, user_rating, user_review "
                + "from movies where id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Movie m = new Movie();
                    m.setId(rs.getInt("id"));
                    m.setName(rs.getString("name"));
                    m.setReleaseYear(rs.getInt("release_year"));
                    m.setWatchStatusId(rs.getInt("watch_status_id"));
                    m.setUserRating(rs.getInt("user_rating"));
                    m.setUserReview(rs.getString("user_review"));
                    return m;
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при поиске фильма " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateMovie(Movie m) {
        String sql = "Update movies set " +
                "name = ?, " +
                "release_year = ?, " +
                "watch_status_id = ?, " +
                "user_rating = ?, " +
                "user_review = ? " +
                "where id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setInt(2, m.getReleaseYear());
            if (m.getWatchStatusId() != null) {
                ps.setInt(3, m.getWatchStatusId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            if (m.getUserRating() != null) {
                ps.setInt(4, m.getUserRating());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            if (m.getUserReview() != null) {
                ps.setString(5, m.getUserReview());
            } else {
                ps.setNull(5, java.sql.Types.VARCHAR);
            }
            ps.setInt(6, m.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Фильм " + m.getName() + " обновлен");
                return true;
            } else {
                System.out.println("Фильм не найден");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении фильма " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveMovie(Movie m) {
        String sql = "insert into movies (name, release_year, watch_status_id, user_rating, user_review) " +
                "values (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setInt(2, m.getReleaseYear());

            if (m.getWatchStatusId() != null) {
                ps.setInt(3, m.getWatchStatusId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            if (m.getUserRating() != null) {
                ps.setInt(4, m.getUserRating());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            if (m.getUserReview() != null) {
                ps.setString(5, m.getUserReview());
            } else {
                ps.setNull(5, java.sql.Types.VARCHAR);
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Фильм " + m.getName() + " добавлен");
                return true;
            } else {
                System.out.println("Не удалось добавить фильм");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении фильма " + e.getMessage());
            e.printStackTrace();
            return false;

        }
    }
}


