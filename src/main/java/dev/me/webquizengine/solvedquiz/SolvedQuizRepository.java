package dev.me.webquizengine.solvedquiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedQuizRepository extends PagingAndSortingRepository<SolvedQuiz, Long> {

    @Query("SELECT s FROM SolvedQuiz s WHERE s.user.id = (SELECT u.id FROM User u WHERE u.email = :username)")
    Page<SolvedQuiz> findAllByUser(@Param("username") String username, Pageable pageable);
}
