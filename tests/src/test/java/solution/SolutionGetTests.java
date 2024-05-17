package solution;

import api.database.ContestDatabaseAutoConfiguration;
import api.database.entities.Solution;
import api.database.functions.SolutionFunctions;
import api.database.functions.TaskFunctions;
import api.database.functions.TestFunctions;
import api.methods.SolutionApi;
import api.methods.TaskApi;
import api.methods.TestApi;
import api.requests.solution.SolutionRequest;
import api.requests.test.TestRequest;
import api.responses.solution.SolutionResponse;
import api.responses.task.TaskResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.commons.lang3.RandomStringUtils;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static api.TestUtils.generateTaskRequestBody;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { ContestDatabaseAutoConfiguration.class, SolutionApi.class, TaskApi.class, TestApi.class })
@ActiveProfiles(profiles = "test")
@Epic("Solution API")
@Feature("Solution GET")
@DisplayName("Тесты на просмотр отправленных решений к задаче")
public class SolutionGetTests {

    @Autowired
    private SolutionFunctions solutionFunctions;

    @Autowired
    private TestFunctions testFunctions;

    @Autowired
    private TaskFunctions taskFunctions;

    @Autowired
    private SolutionApi solutionApi;

    @Autowired
    private TaskApi taskApi;

    @Autowired
    private TestApi testApi;

    private Integer newTaskId;

    @BeforeEach
    public void createTaskAndSendSolution() {
        newTaskId = taskApi.createNewTask(generateTaskRequestBody(
                1,
                "Вывести сумму двух переданных чисел",
                "Сумма чисел",
                "Java",
                1,
                null
        ), "dmitry", "12345").getBody().as(TaskResponse.class).getId();

        testApi.createTests(new TestRequest()
                .taskId(newTaskId)
                .tests(List.of("2 4", "6", "3 8", "11")), "dmitry", "12345");

        solutionApi.sendTaskSolution(new File("src/test/resources/Solution.java"),
                "Java", newTaskId, "dmitry", "12345");
    }

    @Test
    @DisplayName("Получаем все отправленные решения к данной задаче")
    @Description("GET /api/v1/solution/?task_id={taskId}")
    public void getTaskSolutions() {

        var givenSolutions = step("GIVEN: Получены отправленные решения к задаче",
                () -> solutionApi.getTaskSolutions(newTaskId).getBody().as(SolutionResponse[].class));

        var givenDatabaseSolutions = step("WHEN: Получены решения к данной задаче из таблицы solution",
                () -> solutionFunctions.getTaskSolutionsByTaskId(newTaskId));

        step("THEN: Параметры полученных решений соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Количество полученных решений совпадает с количеством из базы",
                                () -> assertEquals(givenSolutions.length, givenDatabaseSolutions.size())),

                        () -> step("file совпадает с полученными из базы значениями",
                                () -> assertEquals(Arrays.stream(givenSolutions).map(a -> a.getFile().substring(7)).sorted().toList(),
                                        givenDatabaseSolutions.stream().map(Solution::getFile).sorted().toList())),

                        () -> step("lang совпадает с полученными из базы значениями",
                                () -> assertEquals(Arrays.stream(givenSolutions).map(SolutionResponse::getLang).sorted().toList(),
                                        givenDatabaseSolutions.stream().map(Solution::getLang).sorted().toList())),

                        () -> step("points совпадает с полученными из базы значениями",
                                () -> assertEquals(Arrays.stream(givenSolutions).map(SolutionResponse::getPoints).sorted().toList(),
                                        givenDatabaseSolutions.stream().map(Solution::getPoints).sorted().toList())),

                        () -> step("status совпадает с полученными из базы значениями",
                                () -> assertEquals(Arrays.stream(givenSolutions).map(SolutionResponse::getStatus).sorted().toList(),
                                        givenDatabaseSolutions.stream().map(Solution::getStatus).sorted().toList())),

                        () -> step("passed_tests совпадает с полученными из базы значениями",
                                () -> assertEquals(Arrays.stream(givenSolutions).map(SolutionResponse::getPassedTests).sorted().toList(),
                                        givenDatabaseSolutions.stream().map(Solution::getPassedTests).sorted().toList()))
                ));

    }

    @Test
    @DisplayName("Получаем несуществующие решения задачи")
    @Description("GET /api/v1/solution/?task_id={taskId}")
    public void getTaskNonExistingSolutions() {

        step("GIVEN: Предварительно отправленное решение удалено",
                () -> solutionFunctions.deleteSolutionByTaskId(newTaskId));

        var givenSolutions = step("AND: Получаем все отправленные решения к задаче",
                () -> solutionApi.getTaskSolutions(newTaskId)
                        .getBody().as(SolutionResponse[].class));

        var givenDatabaseSolutions = step("WHEN: Получаем все существующие решения из таблицы solution",
                () -> solutionFunctions.getTaskSolutionsByTaskId(newTaskId));

        step("THEN: Параметры полученных решений соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Количество полученных решений равно нулю",
                                () -> assertEquals(givenSolutions.length, givenDatabaseSolutions.size()))
                ));
    }

    @Test
    @DisplayName("Получаем решения несуществующей задачи")
    @Description("GET /api/v1/solution/?task_id={taskId}")
    public void getNonExistingTaskSolutions() {

        var givenSolutions = step("WHEN: Получаем все отправленные решения к несуществующей задаче",
                () -> solutionApi.getTaskSolutions(newTaskId + 10));

        step("THEN: Полученные параметры соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Код ответа соответствует полученному коду",
                                () -> assertEquals(givenSolutions.getStatusCode(), 200)),

                        () -> step("Тело ответа соответствует ожидаемому",
                                () -> assertEquals(givenSolutions.getBody().asString(), "[]"))
                ));

    }

    @AfterEach
    public void removeInfoAboutTask() {
        solutionFunctions.deleteSolutionByTaskId(newTaskId);
        testFunctions.deleteTestsByTaskId(newTaskId);
        taskFunctions.deleteTaskById(newTaskId);
    }

}
