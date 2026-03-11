package ru.exmp.movielist.controller;

import ru.exmp.movielist.model.Movie;
import ru.exmp.movielist.model.Genre;
import ru.exmp.movielist.model.Status;
import ru.exmp.movielist.repository.MovieRepository;
import ru.exmp.movielist.repository.GenreRepository;
import ru.exmp.movielist.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private StatusRepository statusRepository;

    @GetMapping
    public String listMovies(Model model) {
        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("movies", movies);
        return "movies/list";
    }

    @GetMapping("/new")
    public String newMovie(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("allGenres", genreRepository.findAll());
        model.addAttribute("allStatuses", statusRepository.findAll());
        return "movies/form";
    }

    @PostMapping("/save")
    public String saveMovie(@ModelAttribute Movie movie,
                            @RequestParam(value = "genreIds", required = false) List<Integer> genreIds) {

        if (movie.getStatus() != null && movie.getStatus().getId() != null) {
            Status status = statusRepository.findById(movie.getStatus().getId()).orElse(null);
            movie.setStatus(status);
        }

        if (genreIds != null && !genreIds.isEmpty()) {
            movie.setGenres(genreRepository.findAllById(genreIds));
        }

        movieRepository.save(movie);
        return "redirect:/movies";
    }

    @GetMapping("/edit/{id}")
    public String editMovie(@PathVariable("id") Integer id, Model model) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null) {
            return "redirect:/movies";
        }
        model.addAttribute("movie", movie);
        model.addAttribute("allGenres", genreRepository.findAll());
        model.addAttribute("allStatuses", statusRepository.findAll());
        return "movies/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable("id") Integer id) {
        movieRepository.deleteById(id);
        return "redirect:/movies";
    }
}