package training.java.learn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import training.java.learn.entity.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Optional<Author> findFirstByAuthorName(String authorName);

    @Query( value = "select * from author order by id desc limit 1", nativeQuery = true)
    Author findLatest();
}
