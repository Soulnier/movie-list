package ru.exmp.movielist.dao;

import ru.exmp.movielist.model.Status;
import ru.exmp.movielist.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class StatusDAO {

    public static StatusDAO INSTANCE = new StatusDAO();

    private StatusDAO() {}

    public static StatusDAO getInstance() {
        return INSTANCE;
    }

    public List<Status> getAllStatuses() {
        List<Status> statuses = new ArrayList<>();
        String sql = """
                SELECT id, name 
                FROM statuses 
                ORDER BY id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Status s = new Status();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                statuses.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при получении статусов " + e.getMessage());
            e.printStackTrace();
        }
        return statuses;
    }

    public Optional<Status> getStatusById(int id) {
        String sql = """
                SELECT id, name 
                FROM statuses 
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Status s = new Status();
            if (rs.next()) {
                    s.setId(rs.getInt("id"));
                    s.setName(rs.getString("name"));
            }
            return Optional.of(s);

        } catch (SQLException e) {
            System.out.println("Ошибка при поиске статуса " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Status getStatusByName(String name) {
        String sql = """
                SELECT id, name 
                FROM statuses 
                WHERE name = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Status s = new Status();
                    s.setId(rs.getInt("id"));
                    s.setName(rs.getString("name"));
                    return s;
                }
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при поиске статуса " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Status saveStatus(Status s) {
        String sql = """
                INSERT INTO statuses
                (name)
                values (?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getName());
            ps.executeUpdate();

            var generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                s.setId(generatedKeys.getInt("id"));
            }

            return s;

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении статуса " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
