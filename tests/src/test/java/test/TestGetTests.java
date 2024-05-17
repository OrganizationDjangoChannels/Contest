package test;

import api.database.ContestDatabaseAutoConfiguration;
import api.database.functions.TaskFunctions;
import api.database.functions.TestFunctions;
import api.methods.TaskApi;
import api.methods.TestApi;
import api.requests.test.TestRequest;
import api.responses.task.TaskResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import api.responses.test.TestResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static api.TestUtils.generateTaskRequestBody;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = { ContestDatabaseAutoConfiguration.class, TestApi.class, TaskApi.class })
@ActiveProfiles(profiles = "test")
@Epic("Test API")
@Feature("Test GET")
@DisplayName("Тесты на получение тестов у задач")
public class TestGetTests {

    @Autowired
    private TestFunctions testFunctions;

    @Autowired
    private TestApi testApi;

    @Autowired
    private TaskApi taskApi;

    @Autowired
    private TaskFunctions taskFunctions;

    private Integer newTaskId;

    @BeforeEach
    public void createTaskAndTests() {
        newTaskId = taskApi.createNewTask(generateTaskRequestBody(
                1,
                RandomStringUtils.random(50, true, false),
                RandomStringUtils.random(15, true, true),
                "C++|Java",
                new Random().nextInt(1, 3),
                null
        ), "dmitry", "12345").getBody().as(TaskResponse.class).getId();

        testApi.createTests(new TestRequest()
                .taskId(newTaskId)
                .tests(List.of("1", "2", "3", "4")), "dmitry", "12345");
    }

    @Test
    @DisplayName("Получаем созданные тесты у задачи")
    @Description("GET /api/v1/test/?task_id={taskId}")
    public void getTaskTests() {

        var createdTests = step("GIVEN: Получены тесты у созданной задачи",
                ()  -> testApi.getTaskTests(newTaskId).getBody().as(TestResponse[].class));

        var createdDatabaseTests = step("WHEN: Получены тесты к созданной задаче из таблицы test",
                () -> testFunctions.getTestsByTaskId(newTaskId));

        step("THEN: Параметры полученных тестов соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Количество полученных тестов совпадает с количеством тестов в базе",
                                () -> assertEquals(createdTests.length, createdDatabaseTests.size())),

                        () -> step("Id полученных тестов совпадает с полученными в базе",
                                () -> assertEquals(Arrays.stream(createdTests).map(TestResponse::getId).sorted().toList(),
                                        createdDatabaseTests.stream().map(api.database.entities.Test::getId).sorted().toList())),

                        () -> step("Input полученных тестов совпадает с полученными в базе",
                                () -> assertEquals(Arrays.stream(createdTests).map(TestResponse::getInput).sorted().toList(),
                                        createdDatabaseTests.stream().map(api.database.entities.Test::getInput).sorted().toList())),

                        () -> step("Output полученных тестов совпадает с полученными в базе",
                                () -> assertEquals(Arrays.stream(createdTests).map(TestResponse::getOutput).sorted().toList(),
                                        createdDatabaseTests.stream().map(api.database.entities.Test::getOutput).sorted().toList())),

                        () -> step("test_number полученных тестов совпадает с полученными в базе",
                                () -> assertEquals(Arrays.stream(createdTests).map(TestResponse::getTestNumber).sorted().toList(),
                                        createdDatabaseTests.stream().map(api.database.entities.Test::getTestNumber).sorted().toList())),

                        () -> step("Id задачи, к которой относятся тесты совпадает с полученными в базе",
                                () -> assertEquals(Arrays.stream(createdTests).map(a -> a.getTask().getId()).sorted().toList(),
                                        createdDatabaseTests.stream().map(api.database.entities.Test::getTaskId).sorted().toList()))
                ));
    }

    @Test
    @DisplayName("Попытка получения тестов у несуществующей задачи")
    @Description("GET /api/v1/test/?task_id={taskId}")
    public void getTestsToNonExistingTask() {

        var createdTests = step("WHEN: Попытка получить тесты у несуществующей задачи",
                () -> testApi.getTaskTests(newTaskId + 10));

        step("THEN: Параметры полученной ошибки соответствуют ожидаемым",
                () -> assertAll(
                        () -> step("Код ошибки равен полученному коду",
                                () -> assertEquals(createdTests.getStatusCode(), 404)),

                        () -> step("Сообщение об ошибке соответствует ожидаемому",
                                () -> assertTrue(createdTests.getBody().asPrettyString()
                                        .contains("There are no tests with provided task_id")))
                ));
    }

    @Test
    @DisplayName("Попытка получить несуществующие тесты")
    @Description("GET /api/v1/test/?task_id={taskId}")
    public void getNonExistingTests() {

        step("GIVEN: Тесты у соозданной задачи удалены",
                () -> testFunctions.deleteTestsByTaskId(newTaskId));

        var createdTests = step("WHEN: Попытка получения удаленных тестов",
                () -> testApi.getTaskTests(newTaskId));

        step("THEN: Параметры полученной ошибки соответствуют ожидаемым",
                () -> assertAll(
                        () -> step("Код ошибки равен полученному коду",
                                () -> assertEquals(createdTests.getStatusCode(), 404)),

                        () -> step("Сообщение об ошибке соответствует ожидаемому",
                                () -> assertTrue(createdTests.getBody().asPrettyString()
                                        .contains("There are no tests with provided task_id")))
                ));
    }

    @AfterEach
    public void deleteTaskAndTests() {
        testFunctions.deleteTestsByTaskId(newTaskId);
        taskFunctions.deleteTaskById(newTaskId);
    }
}
