package ru.exmp.movielist.dao;

import ru.exmp.movielist.model.Genre;
import ru.exmp.movielist.model.Movie;
import ru.exmp.movielist.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAO {

    public static MovieDAO INSTANCE = new MovieDAO();

    private MovieDAO() {}

    public static MovieDAO getInstance() {
        return INSTANCE;
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = """
                select id, name, release_year, watch_status_id, user_rating, user_review 
                from movies 
                order by name
                """;

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
        String sql = """
                select m.id, m.name, m.release_year, m.watch_status_id, m.user_rating, m.user_review
                from movies m join genre_movie gm on m.id = gm.movie_id
                join genres g on gm.genre_id = g.id
                where g.name = ?
                order by m.name
                 """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genreName);

            ResultSet rs = ps.executeQuery();
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
            System.out.println("Ошибка при получении фильмов по жанру " + e.getMessage());
            e.printStackTrace();
        }
        return movies;
    }

    public boolean deleteMovieById(int id)
    {
        String sql = """
                delete 
                from movies 
                where id = ?
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

    public Optional<Movie> getMovieById(int id){
        String sql = """
                Select id, name, release_year, watch_status_id, user_rating, user_review
                from movies 
                where id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            var rs = ps.executeQuery();

            Movie m = new Movie();
                if (rs.next()) {
                    m.setId(rs.getInt("id"));
                    m.setName(rs.getString("name"));
                    m.setReleaseYear(rs.getInt("release_year"));
                    m.setWatchStatusId(rs.getInt("watch_status_id"));
                    m.setUserRating(rs.getInt("user_rating"));
                    m.setUserReview(rs.getString("user_review"));
                }
                return Optional.of(m);
        } catch (SQLException e) {
            System.out.println("Ошибка при поиске фильма " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public boolean updateMovie(Movie m) {
        String sql = """
                Update movies set 
                "name = ?, 
                "release_year = ?, 
                "watch_status_id = ?, 
                "user_rating = ?, 
                "user_review = ? 
                "where id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getName());
            ps.setObject(2, m.getReleaseYear());
            ps.setObject(3, m.getWatchStatusId());
            ps.setObject(4, m.getUserRating());
            ps.setObject(5, m.getUserReview());
            ps.setInt(6, m.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении фильма " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Movie saveMovie(Movie m) {
        String sql = """
                insert into movies 
                (name, release_year, watch_status_id, user_rating, user_review) 
                values (?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, m.getName());
            ps.setObject(2, m.getReleaseYear());
            ps.setObject(3, m.getWatchStatusId());
            ps.setObject(4, m.getUserRating());
            ps.setObject(5, m.getUserReview());

            ps.executeUpdate();

            var generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                m.setId(generatedKeys.getInt("id"));
            }
            return m;

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении фильма " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}


