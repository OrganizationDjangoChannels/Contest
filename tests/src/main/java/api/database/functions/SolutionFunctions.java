package api.database.functions;

import api.database.entities.Solution;
import api.database.repos.SolutionRepository;
import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolutionFunctions {

    private final SolutionRepository solutionRepository;

    public List<Solution> getTaskSolutionsByTaskId(Integer taskId) {
        return solutionRepository.findAllByTaskId(taskId);
    }

    @Step("Удаляем solution с task_id = {taskId}")
    public void deleteSolutionByTaskId(Integer taskId) {
        solutionRepository.deleteByTaskId(taskId);
    }
}
