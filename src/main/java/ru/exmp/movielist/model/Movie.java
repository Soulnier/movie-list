package ru.exmp.movielist.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Movie {
    private Integer id;
    private String name;
    private Integer releaseYear;
    private Integer watchStatusId;
    private Integer userRating;
    private String userReview;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String statusName;
    private String genresNames;

    public Movie(String name, int releaseYear) {
        this.name = name;
        this.releaseYear = releaseYear;
    }
}
