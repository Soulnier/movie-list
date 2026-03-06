package ru.exmp.movielist.service;

import ru.exmp.movielist.dao.GenreDAO;
import ru.exmp.movielist.dao.MovieDAO;
import ru.exmp.movielist.dao.StatusDAO;
import ru.exmp.movielist.dto.MovieDto;
import ru.exmp.movielist.model.Genre;
import ru.exmp.movielist.model.Movie;
import ru.exmp.movielist.model.Status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieService {

    private static final MovieService INSTANCE = new MovieService();

    private final MovieDAO movieDao = MovieDAO.getInstance();
    private final GenreDAO genreDao = GenreDAO.getInstance();
    private final StatusDAO statusDao = StatusDAO.getInstance();

    private MovieService() {}

    public static MovieService getInstance() {
        return INSTANCE;
    }

    public List<MovieDto> findAll() {
        return movieDao.getAllMovies().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<MovieDto> findById(int id) {
        return movieDao.getMovieById(id)
                .map(this::convertToDto);
    }

    public List<Status> getAllStatuses() {
        return statusDao.getAllStatuses();
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    public Movie saveMovie(Movie movie, List<Integer> genreIds) {
        Movie savedMovie = movieDao.saveMovie(movie);
        if (savedMovie.getId() != null && genreIds != null && !genreIds.isEmpty()) {
            movieDao.saveMovieGenres(savedMovie.getId(), genreIds);
        }
        return savedMovie;
    }

    public boolean updateMovie(Movie movie, List<Integer> genreIds) {
        System.out.println("MovieService.updateMovie получил фильм с ID: " + movie.getId());
        boolean updated = movieDao.updateMovie(movie);
        System.out.println("MovieDAO.updateMovie вернул: " + updated);

        if (updated && movie.getId() != null) {
            System.out.println("Сохраняем жанры для фильма ID: " + movie.getId());
            movieDao.saveMovieGenres(movie.getId(), genreIds);
        }
        return updated;
    }

    public boolean deleteMovie(int id) {
        return movieDao.deleteMovieById(id);
    }

    public String getStatusNameById(Integer statusId) {
        if (statusId == null) return "не указан";
        Optional<Status> status = statusDao.getStatusById(statusId);
        return status.map(Status::getName).orElse("не указан");
    }

    private MovieDto convertToDto(Movie movie) {
        MovieDto dto = new MovieDto(
                movie.getId(),
                movie.getName(),
                movie.getReleaseYear(),
                movie.getWatchStatusId(),
                movie.getUserRating(),
                movie.getUserReview()
        );
        List<Genre> genres = genreDao.getGenresByMovieId(movie.getId());
        dto.setGenres(genres);
        return dto;
    }
}