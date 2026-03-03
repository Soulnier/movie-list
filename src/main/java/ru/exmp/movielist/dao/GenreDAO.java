package ru.exmp.movielist.dao;

import org.postgresql.util.PSQLException;
import ru.exmp.movielist.model.Genre;
import ru.exmp.movielist.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO {

    private static final GenreDAO INSTANCE = new GenreDAO();

    private GenreDAO() {}

    public static GenreDAO getInstance() {
        return INSTANCE;
    }

    public void printAllGenres() {
    String sql = """
        select id, name 
        from genres 
        order by name
        """;

        try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
            while (rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                System.out.println(id + " " + name);
            }
        } catch (SQLException e) {
        System.out.println("Ошибка запроса" + e.getMessage());
        e.printStackTrace();
        }
    }

    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sql = """
                select id, name 
                from genres 
                order by name
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
            {
                Genre g = new Genre();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                genres.add(g);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения жанров" + e.getMessage());
            e.printStackTrace();
        }
        return genres;
    }

    public Genre saveGenre(Genre g) {
        String sql = """
                insert into genres (name) 
                values (?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, g.getName());

            ps.executeUpdate();

            var generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()) {
                g.setId(generatedKeys.getInt("id"));
            }
            return g;


        } catch (SQLException e){
            System.out.println("Ошибка при добавлении жанра " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public boolean deleteGenreByName(String genreName)
    {
        String sql = """
                delete 
                from genres 
                where name = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, genreName);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Ошибка при удалении жанра " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGenreById(int id)
    {
        String sql = """
                delete 
                from genres 
                where id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Ошибка при удалении жанра " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Genre> getGenresByMovieId(int movieId)
    {
        List<Genre> genres = new ArrayList<>();

        String sql = "SELECT g.id, g.name " +
                "FROM genres g " +
                "JOIN genre_movie gm ON g.id = gm.genre_id " +
                "WHERE gm.movie_id = ? " +
                "ORDER BY g.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Genre g = new Genre();
                    g.setId(rs.getInt("id"));
                    g.setName(rs.getString("name"));
                    genres.add(g);
                }
            }
        } catch (SQLException e)
        {
            System.out.println("Ошибка при получении жанров " + e.getMessage());
            e.printStackTrace();
        }
        return genres;
    }
}
