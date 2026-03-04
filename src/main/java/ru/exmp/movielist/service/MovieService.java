package ru.exmp.movielist.service;

import ru.exmp.movielist.dao.GenreDAO;
import ru.exmp.movielist.dao.MovieDAO;
import ru.exmp.movielist.dto.MovieDto;
import ru.exmp.movielist.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

public class MovieService {

    private static final MovieService INSTANCE = new MovieService();

    private final MovieDAO movieDao = MovieDAO.getInstance();

    public MovieService() {
    }

    public List<MovieDto> findAll() {
        return movieDao.getAllMovies().stream()
                .map(movie -> {
                    MovieDto dto = new MovieDto(
                            movie.getId(),
                            movie.getName(),
                            movie.getReleaseYear(),
                            movie.getUserRating(),
                            movie.getUserReview()
                    );
                    List<Genre> genres = GenreDAO.getInstance().getGenresByMovieId(movie.getId());
                    dto.setGenres(genres);
                    return dto;
                })
                .collect(Collectors.toList());

    }

    public static MovieService getInstance() {
        return INSTANCE;
    }
}
