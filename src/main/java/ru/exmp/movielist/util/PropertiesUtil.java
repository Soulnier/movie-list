package ru.exmp.movielist.util;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtil {

    private final static Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtil() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            System.out.println("Не удалось загрузить application.properties");
            throw new RuntimeException(e);
        }
    }
}
