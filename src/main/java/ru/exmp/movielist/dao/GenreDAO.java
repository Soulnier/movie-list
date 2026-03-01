package ru.exmp.movielist.dao;

import org.postgresql.util.PSQLException;
import ru.exmp.movielist.util.DatabaseConnection;

import java.sql.*;

public class GenreDAO {

    public void getAllGenres() {
    String sql = "select id, name from genres order by name";

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

    public boolean saveGenre(String genreName) {
        String sql = "insert into genres (name) values (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genreName);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e){
            System.out.println("Ошибка при добавлении жанра " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGenreByName(String genreName)
    {
        String sql = "delete from genres where name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, genreName);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0)
            {
                System.out.println("Жанр " + genreName + " удалён");
                return true;
            } else {
                System.out.println("Жанр " + genreName + " не найден");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении жанра " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGenreById(int id)
    {
        String sql = "delete from genres where id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0)
            {
                System.out.println("Жанр с id " + id + " удалён");
                return true;
            } else {
                System.out.println("Жанр с id " + id + " не найден");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении жанра " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
