package ru.exmp.movielist.repository;

import org.springframework.stereotype.Repository;
import ru.exmp.movielist.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
}