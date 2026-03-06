package ru.exmp.movielist.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            var inputStream = DatabaseInitializer.class
                    .getClassLoader().getResourceAsStream("init.sql");

            assert inputStream != null;
            String sql = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            for (String statement : sql.split(";")) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement.trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка инициализации БД " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}