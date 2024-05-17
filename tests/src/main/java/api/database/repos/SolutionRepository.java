package api.database.repos;

import api.database.entities.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Integer> {

    @Query(nativeQuery = true,
    value = "select * from solution " +
            "where task_id = ?1")
    List<Solution> findAllByTaskId(Integer taskId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true,
    value = "delete from solution " +
            "where task_id = ?1")
    public void deleteByTaskId(Integer taskId);
}
