package test;

import api.database.ContestDatabaseAutoConfiguration;
import api.database.functions.TaskFunctions;
import api.database.functions.TestFunctions;
import api.methods.TaskApi;
import api.methods.TestApi;
import api.requests.test.NewTest;
import api.requests.test.TestRequest;
import api.responses.task.TaskResponse;
import api.responses.test.CreatedTests;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Random;

import static api.TestUtils.generateTaskRequestBody;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { ContestDatabaseAutoConfiguration.class, TestApi.class, TaskApi.class })
@ActiveProfiles(profiles = "test")
@Epic("Test API")
@Feature("Test POST")
@DisplayName("Тесты на создание тестов к задачам")
public class TestPostTests {

    @Autowired
    private TestFunctions testFunctions;

    @Autowired
    private TaskFunctions taskFunctions;

    @Autowired
    private TestApi testApi;

    @Autowired
    private TaskApi taskApi;

    private Integer newTaskId;

    @BeforeEach
    public void createTestingTask() {
        newTaskId = taskApi.createNewTask(generateTaskRequestBody(
                1,
                RandomStringUtils.random(50, true, false),
                RandomStringUtils.random(15, true, true),
                "C++|Java",
                new Random().nextInt(1, 3),
                null
        ), "dmitry", "12345").getBody().as(TaskResponse.class).getId();
    }

    @Test
    @DisplayName("Создаем тесты к существующей задаче")
    @Description("POST /api/v1/test")
    public void createTestsToTask() {

        var createdTests = step("GIVEN: Сформировано тело запроса с созданными тестами",
                () -> new TestRequest()
                        .taskId(newTaskId)
                        .tests(List.of("1", "2", "3", "4"))
        );

        var testCreatedCount = step("WHEN: Получено созданное количество тестов к задаче",
                () -> testApi.createTests(createdTests, "dmitry", "12345")
                        .getBody().as(CreatedTests.class));

        var databaseTests = step("AND: Получены созданные тесты из таблицы test",
                () -> testFunctions.getTestsByTaskId(newTaskId));

        step("THEN: Параметры полученных тестов соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Количество созданных тестов совпадает с количеством в базе",
                                () -> assertEquals(testCreatedCount.getTestsCreated(), databaseTests.size())),

                        () -> step("Input созданных тестов соотвествуют полученным из базы тестам",
                                () -> assertEquals(databaseTests.stream().map(api.database.entities.Test::getInput).sorted().toList(),
                                        createdTests.getTests().stream().map(NewTest::getInput).sorted().toList())),

                        () -> step("Output созданных тестов соответствуют полученным из базы тестам",
                                () -> assertEquals(databaseTests.stream().map(api.database.entities.Test::getOutput).sorted().toList(),
                                        createdTests.getTests().stream().map(NewTest::getOutput).sorted().toList())),

                        () -> step("Id задачи, к которой созданы тесты: соответствует полученному из базы",
                                () -> assertEquals(databaseTests.get(0).getTaskId(), createdTests.getTaskId())
                        ))
                );

    }

    @Test
    @DisplayName("Создаем тесты к задаче, у которой уже есть тесты")
    @Description("POST /api/v1/test")
    public void createTestsToTaskWithExistingTests() {

        var createdTests = step("GIVEN: Сформировано тело запроса с созданными тестами",
                () -> new TestRequest()
                        .taskId(newTaskId)
                        .tests(List.of("1", "2", "3", "4"))
        );
        var testCreatedCount = step("AND: Получено созданное количество тестов к задаче",
                () -> testApi.createTests(createdTests, "dmitry", "12345")
                        .getBody().as(CreatedTests.class));

        var alreadyExistingTests = step("WHEN: Выполнен повторный запрос на создание таких же тестов",
                () -> testApi.createTests(createdTests, "dmitry", "12345")
                        .getBody().as(CreatedTests.class));

        var createdDatabaseTests = step("AND: Получены все созданные тесты из таблицы task",
                () -> testFunctions.getTestsByTaskId(newTaskId));

        step("THEN: Параметры созданных тестов соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Количество созданных тестов соответствует полученному из базы значению",
                                () -> assertEquals(testCreatedCount.getTestsCreated() + alreadyExistingTests.getTestsCreated(),
                                        createdDatabaseTests.size())),

                        () -> step("Input полученных тестов соответствует значению из базы",
                                () -> assertEquals(createdTests.getTests().stream().map(NewTest::getInput).sorted().toList(),
                                        createdDatabaseTests.stream().map(api.database.entities.Test::getInput).distinct().sorted().toList())),

                        () -> step("Output полученных тестов соответствует значению из базы",
                                () -> assertEquals(createdTests.getTests().stream().map(NewTest::getOutput).sorted().toList(),
                                        createdDatabaseTests.stream().map(api.database.entities.Test::getOutput).distinct().sorted().toList()))
                ));


    }

    @Test
    @DisplayName("Создаем тесты к задаче с несуществующим taskId")
    @Description("POST /api/v1/test")
    public void createTestsToNonExistingTask() {

        var createdTests = step("GIVEN: Сформировано тело запроса с созданными тестами и несуществующим taskId",
                () -> new TestRequest()
                        .taskId(newTaskId + 10)
                        .tests(List.of("1", "2", "3", "4"))
        );

        var testCreatedCount = step("WHEN: Попытка получения созданного количества тестов к задаче",
                () -> testApi.createTests(createdTests, "dmitry", "12345"));

        step("THEN: Параметры полученной ошибки соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Код ошибки равен полученному коду",
                                () -> assertEquals(testCreatedCount.getStatusCode(), 500)),

                        () -> step("Сообщение об ошибке соответствует ожидаемому",
                                () -> assertTrue(testCreatedCount.getBody().asPrettyString()
                                        .contains("TaskModel matching query does not exist.")))
                ));
    }

    @Test
    @DisplayName("Создаем тесты с параметрами = null")
    @Description("POST /api/v1/test")
    public void createTestsWithNullParameters() {

        var createdTests = step("GIVEN: Сформировано тело запроса с параметрами = null",
                () -> new TestRequest()
                        .taskId(null)
                        .tests(List.of())
        );

        var testCreatedCount = step("WHEN: Попытка получения созданного количества тестов к задаче",
                () -> testApi.createTests(createdTests, "dmitry", "12345"));

        step("THEN: Параметры полученной ошибки соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Код ошибки равен полученному коду",
                                () -> assertEquals(testCreatedCount.getStatusCode(), 500)),

                        () -> step("Сообщение об ошибке соответствует ожидаемому",
                                () -> assertTrue(testCreatedCount.getBody().asPrettyString()
                                        .contains("TaskModel matching query does not exist.")))
                ));
    }

    @AfterEach
    public void deleteCreatedTask() {
        testFunctions.deleteTestsByTaskId(newTaskId);
        taskFunctions.deleteTaskById(newTaskId);
    }
}
