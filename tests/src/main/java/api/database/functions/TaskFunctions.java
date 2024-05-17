package api.database.functions;

import api.database.repos.TaskRepository;
import api.database.entities.Task;
import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskFunctions {

    private final TaskRepository taskRepository;


    public Task getTaskByTaskId(Integer taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new IllegalArgumentException("Задача с заданным id не существует"));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findLastCreatedTasks();
    }

    public void deleteTaskById(Integer id) {
        taskRepository.deleteById(id);
    }

}
