package api.database.repos;

import api.database.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(nativeQuery = true,
    value = "select * from task " +
            "order by created_at desc " +
            "limit 8"
    )
    List<Task> findLastCreatedTasks();
}
