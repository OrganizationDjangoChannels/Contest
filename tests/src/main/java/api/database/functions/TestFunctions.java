package api.database.functions;

import api.database.entities.Test;
import api.database.repos.TestRepository;
import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestFunctions {

    private final TestRepository testRepository;

    public Test getTestById(Integer id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Тест с заданным id не существует"));
    }

    @Step("Удаляем тесты задачи с id = {taskId}")
    public void deleteTestsByTaskId(Integer taskId) {
        testRepository.deleteTestsByTaskId(taskId);
    }

    public List<Test> getTestsByTaskId(Integer taskId) {
        return testRepository.getTestsByTaskId(taskId);
    }
}
