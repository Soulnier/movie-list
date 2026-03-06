package ru.exmp.movielist.dto;

import lombok.*;
import ru.exmp.movielist.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
public class MovieDto {

    private final Integer id;
    private final String name;
    private final Integer releaseYear;
    private final Integer watchStatusId;
    private final Integer userRating;
    private final String userReview;
    @Setter
    private List<Genre> genres;

    public MovieDto(Integer id, String name, Integer releaseYear,
                    Integer watchStatusId, Integer userRating, String userReview) {
        this.id = id;
        this.name = name;
        this.releaseYear = releaseYear;
        this.watchStatusId = watchStatusId;
        this.userRating = userRating;
        this.userReview = userReview;
    }

    public String getGenresAsString() {
        if (genres == null || genres.isEmpty()) return "нет жанров";
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.joining(", "));
    }
}