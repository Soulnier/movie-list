package ru.exmp.movielist.servlet;

import ru.exmp.movielist.service.MovieService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet("/movies")
public class MovieServlet extends HttpServlet {

    private final MovieService movieService = MovieService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write("<h1>Список фильмов:</h1>");
            printWriter.write("<ul>");
            movieService.findAll().forEach(movieDto -> {
                printWriter.write("<li> %s, %s, %s ".formatted(movieDto.getId(),
                        movieDto.getName(),
                        movieDto.getReleaseYear())
                );
                printWriter.write("Жанры: %s".formatted(movieDto.getGenresAsString()));
                printWriter.write("<br> %s, %s <br>".formatted(
                        movieDto.getUserRating(),
                        movieDto.getUserReview())
                );
                printWriter.write("</li>");
            });
            printWriter.write("</ul>");
        }
    }
}
