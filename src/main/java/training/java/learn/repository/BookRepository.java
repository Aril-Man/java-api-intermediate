package training.java.learn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import training.java.learn.entity.Books;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Books, Integer> {
    @Query("select (count(b) > 0) from Books b where b.judul = ?1")
    boolean existByJudul(String judul);

    List<Books> findByAuthorId(@Param("id") Integer id);

    @Query(value = "select * from books order by id desc limit 1", nativeQuery = true)
    Books findLatest();
}
