package ru.exmp.movielist.model;

import java.time.LocalDateTime;

public class Movie {
    private int id;
    private String name;
    private int releaseYear;
    private int watchStatusId;
    private int userRating;
    private String userReview;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String statusName;
    private String genresNames;

    public Movie() {
    }

    public Movie(String name, int releaseYear) {
        this.name = name;
        this.releaseYear = releaseYear;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getWatchStatusId() {
        return watchStatusId;
    }

    public int getUserRating() {
        return userRating;
    }

    public String getUserReview() {
        return userReview;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getGenresNames() {
        return genresNames;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setWatchStatusId(int watchStatusId) {
        this.watchStatusId = watchStatusId;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setGenresNames(String genresNames) {
        this.genresNames = genresNames;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "releaseYear=" + releaseYear +
                ", name='" + name + '\'' +
                '}';
    }
}
