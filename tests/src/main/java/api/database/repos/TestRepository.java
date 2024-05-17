package api.database.repos;

import api.database.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true,
    value = "delete from test " +
            "where task_id = ?1")
    void deleteTestsByTaskId(Integer taskId);

    @Query(nativeQuery = true,
    value = "select * from test " +
            "where task_id = ?1")
    List<Test> getTestsByTaskId(Integer taskId);
}
