package ru.exmp.movielist.dto;

import ru.exmp.movielist.model.Genre;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MovieDto {

    private final Integer id;
    private final String name;
    private final Integer releaseYear;
    private final Integer userRating;
    private final String userReview;
    private List<Genre> genres;

    public MovieDto(Integer id, String name, Integer releaseYear, Integer userRating, String userReview) {
        this.id = id;
        this.name = name;
        this.releaseYear = releaseYear;
        this.userRating = userRating;
        this.userReview = userReview;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public Integer getUserRating() {
        return userRating;
    }

    public String getUserReview() {
        return userReview;
    }

    public String getGenresAsString() {
        if (genres == null || genres.isEmpty()) return "нет жанров";
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovieDto movieDto = (MovieDto) o;
        return Objects.equals(id, movieDto.id) && Objects.equals(name, movieDto.name) && Objects.equals(releaseYear, movieDto.releaseYear) && Objects.equals(userRating, movieDto.userRating) && Objects.equals(userReview, movieDto.userReview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, releaseYear, userRating, userReview);
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseYear=" + releaseYear +
                ", userRating=" + userRating +
                ", userReview='" + userReview + '\'' +
                '}';
    }
}
