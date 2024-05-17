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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static api.TestUtils.generateTaskRequestBody;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { ContestDatabaseAutoConfiguration.class, TaskApi.class })
@ActiveProfiles(profiles = "test")
@Epic("Task API")
@Feature("Task POST")
@DisplayName("Тесты на создание задач")
public class TaskCreateTests {
    @Autowired
    private TaskApi taskApi;

    @Autowired
    private TaskFunctions taskFunctions;

    private Integer createdTaskDatabaseId;

    public static List<Arguments> getTaskRequestBody() {
        return List.of(
                Arguments.of(generateTaskRequestBody(
                        1,
                        RandomStringUtils.random(50, true, false),
                        RandomStringUtils.random(15, true, true),
                        "C++|Java",
                        new Random().nextInt(1, 3),
                        null
                )),
                Arguments.of(generateTaskRequestBody(
                        1,
                        "",
                        RandomStringUtils.random(15, true, true),
                        "C++|Java",
                        new Random().nextInt(1, 3),
                        null
                )),
                Arguments.of(generateTaskRequestBody(
                        1,
                        RandomStringUtils.random(50, true, false),
                        "",
                        "C++|Java",
                        new Random().nextInt(1, 3),
                        null
                )),
                Arguments.of(generateTaskRequestBody(
                        1,
                        RandomStringUtils.random(50, true, false),
                        RandomStringUtils.random(15, true, true),
                        "Java",
                        new Random().nextInt(1, 3),
                        null
                )),
                Arguments.of(generateTaskRequestBody(
                        null,
                        RandomStringUtils.random(50, true, false),
                        RandomStringUtils.random(15, true, true),
                        "C++|Java",
                        new Random().nextInt(1, 3),
                        null
                )),
                Arguments.of(generateTaskRequestBody(
                        null,
                        "",
                        "",
                        "Java",
                        new Random().nextInt(1, 3),
                        null
                ))
        );
    }
    @ParameterizedTest(name = "Тело запроса - {0}")
    @MethodSource("getTaskRequestBody")
    @DisplayName("Создаем задачу с различным набором аргументов")
    @Description("POST /api/v1/task")
    public void createNewTaskTest(@Param("Тело запроса на создание задачи") TaskRequest taskRequest) {

        var createdTask = step("GIVEN: Создана задача со всеми необходимыми параметрами",
                () -> taskApi.createNewTask(taskRequest, "dmitry", "12345").getBody().as(TaskResponse.class));

        var givenDatabaseTask = step("WHEN: Получена информация о задаче из таблицы task",
                () -> taskFunctions.getTaskByTaskId(createdTask.getId()));

        createdTaskDatabaseId = givenDatabaseTask.getId();

        step("THEN: Параметры созданной задачи соответствуют ожидаемым",
                () -> assertAll(
                        () -> step("Поле id заполнено", () -> assertNotNull(givenDatabaseTask.getId())),

                        () -> step("Поле title = указывали при создании задачи",
                                () -> assertEquals(createdTask.getTitle(),
                                        givenDatabaseTask.getTitle())),

                        () -> step("Поле description = указывали при создании задачи",
                                () -> assertEquals(createdTask.getDescription(),
                                        givenDatabaseTask.getDescription())),

                        () -> step("Поле langs = указывали при создании задачи",
                                () -> assertEquals(createdTask.getLangs(),
                                        givenDatabaseTask.getLangs())),

                        () -> step("Поле level = указывали при создании задачи",
                                () -> assertEquals(createdTask.getLevel(),
                                        givenDatabaseTask.getLevel())),

                        () -> step("Поле sent_solutions равно нулю",
                                () -> assertEquals(createdTask.getSentSolutions(),
                                        givenDatabaseTask.getSentSolution())),

                        () -> step("Owner_id = id пользователя, создавшего задачу",
                                () -> assertEquals(createdTask.getOwner().getId(),
                                        givenDatabaseTask.getOwnerId())),

                        () -> step("Время создания задачи соответствует ожидаемому",
                                () -> assertTrue(LocalDateTime.now().minusMinutes(1L)
                                        .isBefore(givenDatabaseTask.getCreatedAt())))
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
    @ParameterizedTest(name = "body - {0}")
    @MethodSource("getTaskRequestBodyWithNullParameters")
    @DisplayName("Попытка создать задачу с параметрами =  null")
    @Description("POST /api/v1/task")
     public void createTaskWithNullParameters(@Param("Тело запроса") TaskRequest taskRequest) {

        var createdTask = step("GIVEN: Попытка создания задачи без параметра level",
                () -> taskApi.createNewTask(taskRequest, "dmitry", "12345"));

        step("THEN: Параметры полученной ошибки соответствуют ожидаемым",
                () -> assertAll(
                        () -> step("Код ошибки равен полученному коду",
                                () -> assertEquals(createdTask.getStatusCode(), 400)),

                        () -> step("Сообщение об ошибке соответствует ожидаемому",
                                () -> assertTrue(createdTask.getBody().asPrettyString()
                                        .contains("This field may not be null.")))
                ));
    }

    @AfterEach
    public void deleteCreatedTask() {
        if (createdTaskDatabaseId != null) {
            taskFunctions.deleteTaskById(createdTaskDatabaseId);
        }
    }
}
