package ru.exmp.movielist.servlet;

import ru.exmp.movielist.dto.MovieDto;
import ru.exmp.movielist.model.Genre;
import ru.exmp.movielist.model.Movie;
import ru.exmp.movielist.model.Status;
import ru.exmp.movielist.service.MovieService;
import ru.exmp.movielist.util.DatabaseInitializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/movies")
public class MovieServlet extends HttpServlet {

    private final MovieService movieService = MovieService.getInstance();

    @Override
    public void init() throws ServletException {
        DatabaseInitializer.initialize();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        if ("edit".equals(action) && idParam != null) {
            showEditForm(req, resp, Integer.parseInt(idParam));
        } else if ("add".equals(action)) {
            showAddForm(req, resp);
        } else {
            showAllMovies(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            doDelete(req, resp);
        } else if ("update".equals(action)) {
            updateMovie(req, resp);
        } else {
            saveMovie(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            int id = Integer.parseInt(idParam);
            movieService.deleteMovie(id);
        }
        resp.sendRedirect(req.getContextPath() + "/movies");
    }

    private void showAllMovies(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        List<MovieDto> movies = movieService.findAll();
        List<Status> allStatuses = movieService.getAllStatuses();

        try (PrintWriter out = resp.getWriter()) {
            out.write("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Мои фильмы</title>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial; margin: 20px; background: #f5f5f5; }
                        h1 { color: #333; text-align: center; }
                        .movie-list { list-style: none; padding: 0; max-width: 800px; margin: 0 auto; }
                        .movie-item { 
                            background: white; 
                            margin: 15px 0; 
                            padding: 20px; 
                            border-radius: 8px;
                            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                            border-left: 5px solid #4CAF50;
                            position: relative;
                        }
                        .movie-title { font-size: 1.3em; font-weight: bold; color: #333; }
                        .movie-detail { margin: 8px 0; color: #666; }
                        .label { font-weight: bold; color: #4CAF50; display: inline-block; width: 80px; }
                        .genres { color: #ff9800; font-style: italic; }
                        .rating { color: #9c27b0; font-weight: bold; }
                        .status { color: #2196f3; font-weight: bold; }
                        .actions { margin-top: 15px; }
                        .btn { 
                            display: inline-block; 
                            padding: 8px 15px; 
                            margin-right: 10px;
                            text-decoration: none; 
                            border-radius: 4px;
                            border: none;
                            cursor: pointer;
                            font-size: 14px;
                        }
                        .btn-edit { background: #ffc107; color: #333; }
                        .btn-delete { background: #dc3545; color: white; }
                        .btn-add { 
                            background: #28a745; 
                            color: white; 
                            padding: 10px 20px;
                            margin: 20px auto;
                            display: inline-block;
                            text-decoration: none;
                            border-radius: 4px;
                        }
                        .stats {
                            background: #e8f5e9;
                            padding: 15px;
                            border-radius: 8px;
                            margin-bottom: 20px;
                            max-width: 800px;
                            margin-left: auto;
                            margin-right: auto;
                            text-align: center;
                        }
                    </style>
                </head>
                <body>
                    <h1>🎬 Моя коллекция фильмов</h1>
                    
                    <div class="stats">
                        <p>📊 Всего фильмов: <strong>%d</strong></p>
                    </div>
                    
                    <div style="text-align: center;">
                        <a href="?action=add" class="btn-add">➕ Добавить фильм</a>
                    </div>
                    
                    <ul class="movie-list">
                """.formatted(movies.size()));

            for (MovieDto movie : movies) {
                String statusName = movieService.getStatusNameById(movie.getWatchStatusId());
                String statusEmoji = switch (statusName) {
                    case "Запланировано" -> "📅";
                    case "Просматривается" -> "▶️";
                    case "Просмотрено" -> "✅";
                    case "Брошено" -> "⛔";
                    default -> "❓";
                };

                out.write("<li class='movie-item'>");
                out.write("<div class='movie-title'>📽️ " + movie.getName() +
                        " (" + movie.getReleaseYear() + ")</div>");

                out.write("<div class='movie-detail'><span class='label'>ID:</span> " +
                        movie.getId() + "</div>");

                out.write("<div class='movie-detail'><span class='label'>Жанры:</span> " +
                        "<span class='genres'>" + movie.getGenresAsString() + "</span></div>");

                out.write("<div class='movie-detail'><span class='label'>Статус:</span> " +
                        "<span class='status'>" + statusEmoji + " " + statusName + "</span></div>");

                String rating = movie.getUserRating() != null ?
                        movie.getUserRating() + "/10" : "нет оценки";
                out.write("<div class='movie-detail'><span class='label'>Рейтинг:</span> " +
                        "<span class='rating'>" + rating + "</span></div>");

                String review = movie.getUserReview() != null && !movie.getUserReview().isEmpty() ?
                        movie.getUserReview() : "нет отзыва";
                out.write("<div class='movie-detail'><span class='label'>Отзыв:</span> " +
                        review + "</div>");

                out.write("<div class='actions'>");
                out.write("<a href='?action=edit&id=" + movie.getId() +
                        "' class='btn btn-edit'>✏️ Редактировать</a>");

                out.write("<form style='display:inline;' method='post' " +
                        "onsubmit='return confirm(\"Удалить фильм?\")'>");
                out.write("<input type='hidden' name='action' value='delete'>");
                out.write("<input type='hidden' name='id' value='" + movie.getId() + "'>");
                out.write("<button type='submit' class='btn btn-delete'>🗑️ Удалить</button>");
                out.write("</form>");
                out.write("</div>");

                out.write("</li>");
            }

            out.write("""
                    </ul>
                </body>
                </html>
                """);
        }
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        List<Status> allStatuses = movieService.getAllStatuses();
        List<Genre> allGenres = movieService.getAllGenres();

        try (PrintWriter out = resp.getWriter()) {
            out.write("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Добавить фильм</title>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial; margin: 20px; background: #f5f5f5; }
                        .form-container {
                            background: white;
                            padding: 30px;
                            border-radius: 8px;
                            max-width: 500px;
                            margin: 0 auto;
                            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                        }
                        h2 { color: #333; text-align: center; }
                        label { 
                            display: block; 
                            margin: 15px 0 5px; 
                            color: #4CAF50;
                            font-weight: bold;
                        }
                        input, select, textarea {
                            width: 100%;
                            padding: 10px;
                            border: 1px solid #ddd;
                            border-radius: 4px;
                            font-size: 14px;
                        }
                        select[multiple] {
                            height: 120px;
                        }
                        button {
                            background: #28a745;
                            color: white;
                            padding: 12px 20px;
                            border: none;
                            border-radius: 4px;
                            cursor: pointer;
                            font-size: 16px;
                            width: 100%;
                            margin-top: 20px;
                        }
                        button:hover { background: #218838; }
                        .small { font-size: 12px; color: #666; margin-top: 5px; }
                        .back-link {
                            display: block;
                            text-align: center;
                            margin-top: 20px;
                            color: #666;
                        }
                    </style>
                </head>
                <body>
                    <div class="form-container">
                        <h2>➕ Добавить новый фильм</h2>
                        <form method="post">
                            <label>Название:</label>
                            <input type="text" name="name" required>
                            
                            <label>Год выпуска:</label>
                            <input type="number" name="releaseYear" required>
                            
                            <label>Статус:</label>
                            <select name="statusId">
                                <option value="">Без статуса</option>
                """);

            for (Status s : allStatuses) {
                out.write("<option value='" + s.getId() + "'>" + s.getName() + "</option>");
            }

            out.write("""
                            </select>
                            
                            <label>Жанры (можно выбрать несколько):</label>
                            <select name="genreIds" multiple>
                """);

            for (Genre g : allGenres) {
                out.write("<option value='" + g.getId() + "'>" + g.getName() + "</option>");
            }

            out.write("""
                            </select>
                            <div class="small">Зажмите Ctrl для выбора нескольких</div>
                            
                            <label>Рейтинг (0-10):</label>
                            <input type="number" name="userRating" min="0" max="10" step="1">
                            
                            <label>Отзыв:</label>
                            <textarea name="userReview" rows="3"></textarea>
                            
                            <button type="submit">💾 Сохранить</button>
                        </form>
                        <a href="/movielist/movies" class="back-link">← Вернуться к списку</a>
                    </div>
                </body>
                </html>
                """);
        }
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, int id)
            throws IOException {

        Optional<MovieDto> movieOpt = movieService.findById(id);
        if (movieOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/movies");
            return;
        }

        MovieDto movie = movieOpt.get();
        List<Status> allStatuses = movieService.getAllStatuses();
        List<Genre> allGenres = movieService.getAllGenres();
        List<Integer> currentGenreIds = movie.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (PrintWriter out = resp.getWriter()) {
            out.write("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Редактировать фильм</title>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial; margin: 20px; background: #f5f5f5; }
                        .form-container {
                            background: white;
                            padding: 30px;
                            border-radius: 8px;
                            max-width: 500px;
                            margin: 0 auto;
                            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                        }
                        h2 { color: #333; text-align: center; }
                        label { 
                            display: block; 
                            margin: 15px 0 5px; 
                            color: #4CAF50;
                            font-weight: bold;
                        }
                        input, select, textarea {
                            width: 100%;
                            padding: 10px;
                            border: 1px solid #ddd;
                            border-radius: 4px;
                            font-size: 14px;
                        }
                        select[multiple] {
                            height: 120px;
                        }
                        button {
                            background: #ffc107;
                            color: #333;
                            padding: 12px 20px;
                            border: none;
                            border-radius: 4px;
                            cursor: pointer;
                            font-size: 16px;
                            width: 100%;
                            margin-top: 20px;
                        }
                        button:hover { background: #e0a800; }
                        .small { font-size: 12px; color: #666; margin-top: 5px; }
                        .back-link {
                            display: block;
                            text-align: center;
                            margin-top: 20px;
                            color: #666;
                        }
                    </style>
                </head>
                <body>
                    <div class="form-container">
                        <h2>✏️ Редактировать фильм</h2>
                    <form action="/movielist/movies" method="post">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="id" value=\"""" + id + "\">" + """
                            
                            <label>Название:</label>
                            <input type="text" name="name" value=\"""" + movie.getName() + "\" required>" + """
                            
                            <label>Год выпуска:</label>
                            <input type="number" name="releaseYear" value=\"""" + movie.getReleaseYear() + "\" required>" + """
                            
                            <label>Статус:</label>
                            <select name="statusId">
                                <option value="">Без статуса</option>
                """);

            Integer currentStatusId = movie.getWatchStatusId();
            for (Status s : allStatuses) {
                String selected = s.getId().equals(currentStatusId) ? "selected" : "";
                out.write("<option value='" + s.getId() + "' " + selected + ">" +
                        s.getName() + "</option>");
            }

            out.write("""
                            </select>
                            
                            <label>Жанры (можно выбрать несколько):</label>
                            <select name="genreIds" multiple>
                """);

            for (Genre g : allGenres) {
                String selected = currentGenreIds.contains(g.getId()) ? "selected" : "";
                out.write("<option value='" + g.getId() + "' " + selected + ">" +
                        g.getName() + "</option>");
            }

            out.write("""
                            </select>
                            <div class="small">Зажмите Ctrl для выбора нескольких</div>
                            
                            <label>Рейтинг (0-10):</label>
                            <input type="number" name="userRating" min="0" max="10" step="1" 
                                   value=\"""" + (movie.getUserRating() != null ? movie.getUserRating() : "") + "\">" + """
                            
                            <label>Отзыв:</label>
                            <textarea name="userReview" rows="3">""" +
                    (movie.getUserReview() != null ? movie.getUserReview() : "") +
                    """
                    </textarea>
                    
                    <button type="submit">💾 Обновить</button>
                </form>
                <a href="/movielist/movies" class="back-link">← Вернуться к списку</a>
            </div>
        </body>
        </html>
        """);
        }
    }

    private void saveMovie(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String name = req.getParameter("name");
        int releaseYear = Integer.parseInt(req.getParameter("releaseYear"));
        Integer statusId = parseIntOrNull(req.getParameter("statusId"));
        Integer userRating = parseIntOrNull(req.getParameter("userRating"));
        String userReview = req.getParameter("userReview");

        String[] genreIdParams = req.getParameterValues("genreIds");
        List<Integer> genreIds = genreIdParams != null ?
                Arrays.stream(genreIdParams).map(Integer::parseInt).collect(Collectors.toList()) :
                List.of();

        Movie movie = new Movie();
        movie.setName(name);
        movie.setReleaseYear(releaseYear);
        movie.setWatchStatusId(statusId);
        movie.setUserRating(userRating);
        movie.setUserReview(userReview);

        movieService.saveMovie(movie, genreIds);

        resp.sendRedirect(req.getContextPath() + "/movies");
    }

    private void updateMovie(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idParam = req.getParameter("id");

        int id = Integer.parseInt(idParam);
        String name = req.getParameter("name");
        int releaseYear = Integer.parseInt(req.getParameter("releaseYear"));
        Integer statusId = parseIntOrNull(req.getParameter("statusId"));
        Integer userRating = parseIntOrNull(req.getParameter("userRating"));
        String userReview = req.getParameter("userReview");

        String[] genreIdParams = req.getParameterValues("genreIds");
        List<Integer> genreIds = genreIdParams != null ?
                Arrays.stream(genreIdParams).map(Integer::parseInt).collect(Collectors.toList()) :
                List.of();

        Movie movie = new Movie();
        movie.setId(id);
        movie.setName(name);
        movie.setReleaseYear(releaseYear);
        movie.setWatchStatusId(statusId);
        movie.setUserRating(userRating);
        movie.setUserReview(userReview);


        boolean updated = movieService.updateMovie(movie, genreIds);

        resp.sendRedirect(req.getContextPath() + "/movies");
    }

    private Integer parseIntOrNull(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}