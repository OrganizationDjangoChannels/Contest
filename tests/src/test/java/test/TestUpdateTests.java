package test;

import api.database.ContestDatabaseAutoConfiguration;
import api.database.functions.TaskFunctions;
import api.database.functions.TestFunctions;
import api.methods.TaskApi;
import api.methods.TestApi;
import api.requests.test.TestRequest;
import api.responses.task.TaskResponse;
import api.responses.test.TestResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
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
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@Epic("Task API")
@Feature("Task PUT")
@DisplayName("Тесты на редактирование тестов к задаче")
public class TestUpdateTests {

    @Autowired
    private TestApi testApi;

    @Autowired
    private TestFunctions testFunctions;

    @Autowired
    private TaskApi taskApi;

    @Autowired
    private TaskFunctions taskFunctions;

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

        testApi.createTests(new TestRequest()
                .taskId(newTaskId)
                .tests(List.of("1", "2", "3", "4")), "dmitry", "12345");
    }

    @Test
    @DisplayName("Редактируем тесты к созданной задаче")
    @Description("PUT /api/v1/test/?task_id={taskId}")
    public void updateExistingTests() {

        var givenTests = step("GIVEN: Получены тесты созданной задачи",
                () -> testApi.getTaskTests(newTaskId).getBody().as(TestResponse[].class));

        step("AND: Значения input и output тестов изменены",
                () -> {
                    for (TestResponse givenTest : givenTests) {
                        givenTest.setInput("new input");
                        givenTest.setOutput("new output");
                    }
                });

        step("WHEN: Сделан запрос на обновление тестов",
                () -> testApi.updateTests(newTaskId, givenTests, "dmitry", "12345"));

        var updatedTests = step("AND: Получены обновленные тесты из таблицы test",
                () -> testFunctions.getTestsByTaskId(newTaskId));

        step("THEN: Параметры полученных тестов соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Количество тестов после редактирования не изменилось",
                                () -> assertEquals(givenTests.length, updatedTests.size())),

                        () -> step("Input соответствует ожидаемому",
                                () -> assertEquals(updatedTests.stream().map(api.database.entities.Test::getInput)
                                                .distinct().toList().get(0), "new input")),

                        () -> step("Output соответствует ожидаемому",
                                () -> assertEquals(updatedTests.stream().map(api.database.entities.Test::getOutput)
                                        .distinct().toList().get(0), "new output"))
                ));
    }

    @Test
    @DisplayName("Редактируем несуществующие тесты")
    @Description("PUT /api/v1/test/?task_id={taskId}")
    public void updateNonExistingTests() {

        step("GIVEN: Созданные тесты к задаче удалены",
                () -> testFunctions.deleteTestsByTaskId(newTaskId));

        var updatedTests = step("WHEN: Сделан запрос на обновление тестов",
                () -> testApi.updateTests(newTaskId, new TestResponse[]{}, "dmitry", "12345"));

        step("THEN: Параметры полученного ответа соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Код ответа соответствует полученному",
                                () -> assertEquals(updatedTests.getStatusCode(), 204)),

                        () -> step("Количество тестов осталось неизменным",
                                () -> assertEquals(testFunctions.getTestsByTaskId(newTaskId).size(), 0))
                ));
    }

    @Test
    @DisplayName("Редактируем тесты под пользователем, не создававшем задачу")
    @Description("PUT /api/v1/test/?task_id={taskId}")
    public void updateExistingTestsByNotCreatorTaskUser() {

        var givenTests = step("GIVEN: Получены тесты созданной задачи",
                () -> testApi.getTaskTests(newTaskId).getBody().as(TestResponse[].class));

        step("AND: Значения input и output тестов изменены",
                () -> {
                    for (TestResponse givenTest : givenTests) {
                        givenTest.setInput("new input");
                        givenTest.setOutput("new output");
                    }
                });

        var updatedTests = step("WHEN: Сделан запрос на обновление тестов",
                () -> testApi.updateTests(newTaskId, givenTests, "test_user", "12345"));

        step("THEN: Параметры полученной ошибки соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Код ошибки равен полученному коду",
                                () -> assertEquals(updatedTests.getStatusCode(), 500)),

                        () -> step("Сообщение об ошибке соответствует ожидаемому",
                                () -> assertTrue(updatedTests.getBody().asPrettyString()
                                        .contains("You do not have rights to edit this")))
                ));
    }

    @Test
    @DisplayName("Редактируем тесты с некорректными параметрами в body")
    @Description("PUT /api/v1/test/?task_id={taskId}")
    public void updateTestsWithNullParameters() {

        var givenTests = step("GIVEN: Получены тесты созданной задачи",
                () -> testApi.getTaskTests(newTaskId).getBody().as(TestResponse[].class));

        step("AND: Значения input и output тестов изменены",
                () -> {
                    for (TestResponse givenTest : givenTests) {
                        givenTest.setInput(null);
                        givenTest.setOutput(null);
                    }
                });

        var updateRequest = step("WHEN: Сделан запрос на обновление тестов",
                () -> testApi.updateTests(newTaskId, givenTests, "dmitry", "12345"));

        var updatedTests = step("AND: Получены обновленные тесты из таблицы test",
                () -> testFunctions.getTestsByTaskId(newTaskId));

        step("THEN: Параметры полученных тестов соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Тесты были удалены",
                                () -> assertEquals(updatedTests.size(), 0)),

                        () -> step("Запрос на обновление тестов вернул ожидаемый код ответа",
                                () -> assertEquals(updateRequest.getStatusCode(), 204))
                ));
    }

    @AfterEach
    public void deleteCreatedTask() {
        testFunctions.deleteTestsByTaskId(newTaskId);
        taskFunctions.deleteTaskById(newTaskId);
    }
}
