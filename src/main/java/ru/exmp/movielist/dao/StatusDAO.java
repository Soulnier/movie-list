package ru.exmp.movielist.dao;

import ru.exmp.movielist.model.Status;
import ru.exmp.movielist.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatusDAO {

    public List<Status> getAllStatuses() {
        List<Status> statuses = new ArrayList<>();
        String sql = "SELECT id, name FROM statuses ORDER BY id";

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

    public Status getStatusById(int id) {
        String sql = "SELECT id, name FROM statuses WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

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

    public Status getStatusByName(String name) {
        String sql = "SELECT id, name FROM statuses WHERE name = ?";

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
}
