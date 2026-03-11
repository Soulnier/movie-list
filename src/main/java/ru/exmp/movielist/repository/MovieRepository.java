package ru.exmp.movielist.repository;

import org.springframework.stereotype.Repository;
import ru.exmp.movielist.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findByStatusId(Integer statusId);
}