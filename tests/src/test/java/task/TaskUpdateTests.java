package task;

import api.database.ContestDatabaseAutoConfiguration;
import api.database.functions.TaskFunctions;
import api.methods.TaskApi;
import api.requests.task.TaskRequest;
import api.responses.task.TaskResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Param;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Random;

import static api.TestUtils.generateTaskRequestBody;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ContestDatabaseAutoConfiguration.class, TaskApi.class})
@ActiveProfiles(profiles = "test")
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@Epic("Task API")
@Feature("Task PUT")
@DisplayName("Тесты на обновление информации о задаче")
public class TaskUpdateTests {

    @Autowired
    private TaskFunctions taskFunctions;

    @Autowired
    private TaskApi taskApi;

    private Integer newTaskId;

    @BeforeAll
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
    @DisplayName("Попытка отредактировать задачу пользователем, не создававшем ее")
    @Description("PUT /api/v1/task/{taskId}")
    public void updateTaskNonCreatorUser() {

        var taskRequestBody = step("GIVEN: Сформировано тело для редактирования задачи",
                () -> new TaskRequest()
                        .id(1)
                        .description(RandomStringUtils.random(50, true, false))
                        .title(RandomStringUtils.random(15, true, true))
                        .langs("Java")
                        .level(new Random().nextInt(1, 3))
                        .owner(null)
        );

        var updatedTask = step("WHEN: Попытка редактирования задачи пользователем, не создававшим ее",
                () -> taskApi.updateExistingTask(newTaskId, taskRequestBody, "test_user", "12345"));

        step("THEN: Параметры полученной ошибки соответствуют ожидаемым",
                () -> assertAll(
                        () -> step("Код ошибки равен полученному коду",
                                () -> assertEquals(updatedTask.getStatusCode(), 500)),

                        () -> step("Сообщение об ошибке соответствует ожидаемому",
                                () -> assertTrue(updatedTask.getBody().asPrettyString()
                                        .contains("You do not have rights to edit this")))
                ));
    }

    @Test
    @DisplayName("Попытка отредактировать несуществующую задачу")
    @Description("PUT /api/v1/task/{taskId}")
    public void updateNonExistingTask() {

        var taskRequestBody = step("GIVEN: Сформировано тело для редактирования задачи",
                () -> new TaskRequest()
                        .id(1)
                        .description(RandomStringUtils.random(50, true, false))
                        .title(RandomStringUtils.random(15, true, true))
                        .langs("Java")
                        .level(new Random().nextInt(1, 3))
                        .owner(null)
        );

        var updatedTask = step("WHEN: Попытка редактирования несуществующей задачи",
                () -> taskApi.updateExistingTask(newTaskId + 10, taskRequestBody, "dmitry", "12345"));

        step("THEN: Параметры полученной ошибки соответствуют ожидаемым",
                () -> assertAll(
                        () -> step("Код ошибки равен полученному коду",
                                () -> assertEquals(updatedTask.getStatusCode(), 500)),

                        () -> step("Сообщение об ошибке соответствует ожидаемому",
                                () -> assertTrue(updatedTask.getBody().asPrettyString()
                                        .contains("Task does not exist")))
                ));
    }

    @Test
    @DisplayName("Редактируем параметры существующей задачи")
    @Description("PUT /api/v1/task/{taskId}")
    public void updateExistingTask() {

        var taskRequestBody = step("GIVEN: Сформировано тело для редактирования задачи",
                () -> new TaskRequest()
                        .id(1)
                        .description(RandomStringUtils.random(50, true, false))
                        .title(RandomStringUtils.random(15, true, true))
                        .langs("Java")
                        .level(new Random().nextInt(1, 3))
                        .owner(null)
        );

        var updatedTask = step("WHEN: Параметры задачи отредактированы",
                () -> taskApi.updateExistingTask(newTaskId, taskRequestBody, "dmitry", "12345")
                        .getBody().as(TaskResponse.class));

        var updatedDatabaseTask = step("AND: Получена обновленная задача из таблицы task с id = {newTaskId}",
                () -> taskFunctions.getTaskByTaskId(newTaskId));

        step("THEN: Обновленные параметры соответствуют ожидаемым",
                () -> assertAll(
                        () -> step("Поле description успешно обновлено",
                                () -> assertEquals(updatedTask.getDescription(),
                                        updatedDatabaseTask.getDescription())),

                        () -> step("Поле title успешно обновлено",
                                () -> assertEquals(updatedTask.getTitle(),
                                        updatedDatabaseTask.getTitle())),

                        () -> step("Поле langs успешно обновлено",
                                () -> assertEquals(updatedTask.getLangs(),
                                        updatedDatabaseTask.getLangs())),

                        () -> step("Поле title успешно обновлено",
                                () -> assertEquals(updatedTask.getLevel(),
                                        updatedDatabaseTask.getLevel()))
                ));
    }

    public static List<Arguments> getTaskRequestBodyWithNullParameters() {
        return List.of(
                Arguments.of(generateTaskRequestBody(
                        1,
                        RandomStringUtils.random(50, true, false),
                        RandomStringUtils.random(15, true, true),
                        "C++|Java",
                        null,
                        null
                )),
                Arguments.of(generateTaskRequestBody(
                        1,
                        RandomStringUtils.random(50, true, false),
                        RandomStringUtils.random(15, true, true),
                        null,
                        new Random().nextInt(1, 3),
                        null
                )),
                Arguments.of(generateTaskRequestBody(
                        1,
                        null,
                        RandomStringUtils.random(15, true, true),
                        "Java",
                        new Random().nextInt(1, 3),
                        null
                )),
                Arguments.of(generateTaskRequestBody(
                        1,
                        RandomStringUtils.random(50, true, false),
                        null,
                        "Java",
                        new Random().nextInt(1, 3),
                        null
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("getTaskRequestBodyWithNullParameters")
    @DisplayName("Попытка обновить параметры задачи на параметры = null")
    @Description("PUT /api/v1/task/{taskId}")
    public void updateTaskWithNullParameters(@Param("Тело запроса") TaskRequest taskRequest) {

        var updatedTask = step("WHEN: Параметры задачи отредактированы на параметры = null",
                () -> taskApi.updateExistingTask(newTaskId, taskRequest, "dmitry", "12345"));

        step("THEN: Параметры полученной ошибки соответствуют ожидаемым",
                () -> assertAll(
                        () -> step("Код ошибки равен полученному коду",
                                () -> assertEquals(updatedTask.getStatusCode(), 400)),

                        () -> step("Сообщение об ошибке соответствует ожидаемому",
                                () -> assertTrue(updatedTask.getBody().asPrettyString()
                                        .contains("This field may not be null.")))
                ));
    }

    @AfterAll
    public void deleteCreatedTask() {
        taskFunctions.deleteTaskById(newTaskId);
    }
}
