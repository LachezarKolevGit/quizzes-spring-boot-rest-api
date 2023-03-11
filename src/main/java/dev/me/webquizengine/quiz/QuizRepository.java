package dev.me.webquizengine.quiz;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {

    /** findMaxIdNumber returns List because LIMIT is not supported by JPQL and
     to get only one id the Page interface is used.
     Page requires the returned value to be in List */
    @Query("SELECT q.id FROM Quiz q ORDER BY q.id DESC")
    List<Long> findMaxIdNumber(Pageable pageable);

}
