package task;

import api.database.ContestDatabaseAutoConfiguration;
import api.database.entities.Task;
import api.database.functions.TaskFunctions;
import api.methods.TaskApi;
import api.responses.task.TaskResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Random;

import static api.TestUtils.generateTaskRequestBody;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { ContestDatabaseAutoConfiguration.class, TaskApi.class })
@ActiveProfiles(profiles = "test")
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@Epic("Task API")
@Feature("Task GET")
@DisplayName("Тесты на получение информации о задаче")
public class TaskGetTests {

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
    @DisplayName("Получение списка всех задач")
    @Description("GET /api/v1/task")
    public void TaskGetTest() {

        var taskList = step("GIVEN: Получен список задач", () -> taskApi.getTasksList());

        var databaseTaskList = step("WHEN: Получены все записи из таблицы task", () -> taskFunctions.getAllTasks());

        step("THEN: Параметры полученных задач соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Количество полученных задач совпадает с количеством задач в базе",
                                () -> assertEquals(taskList.length, databaseTaskList.size())),

                        () -> step("Id задач равны задачам, полученным из базы",
                                () -> assertEquals(Arrays.stream(taskList).map(TaskResponse::getId).sorted().toList(),
                                        databaseTaskList.stream().map(Task::getId).sorted().toList())),

                        () -> step("title задач равны задачам, полученным из базы",
                                () -> assertEquals(Arrays.stream(taskList).map(TaskResponse::getTitle).sorted().toList(),
                                        databaseTaskList.stream().map(Task::getTitle).sorted().toList())),

                        () -> step("Description задач равны задачам, полученным из базы",
                                () -> assertEquals(Arrays.stream(taskList).map(TaskResponse::getDescription).sorted().toList(),
                                        databaseTaskList.stream().map(Task::getDescription).sorted().toList())),

                        () -> step("level задач равны задачам, полученным из базы",
                                () -> assertEquals(Arrays.stream(taskList).map(TaskResponse::getLevel).sorted().toList(),
                                        databaseTaskList.stream().map(Task::getLevel).sorted().toList())),

                        () -> step("langs задач равны задачам, полученным из базы",
                                () -> assertEquals(Arrays.stream(taskList).map(TaskResponse::getLangs).sorted().toList(),
                                        databaseTaskList.stream().map(Task::getLangs).sorted().toList())),

                        () -> step("Id создателя задач равны задачам, полученным из базы",
                                () -> assertEquals(Arrays.stream(taskList).map(a -> a.getOwner().getId()).sorted().toList(),
                                        databaseTaskList.stream().map(Task::getOwnerId).sorted().toList()))
                ));
    }

    @Test
    @DisplayName("Получение информации о задаче по ее id")
    @Description("GET /api/v1/task")
    public void TaskGetByIdTest() {

        var givenTask = step("GIVEN: Получена задача с определенным id", () -> taskApi.getTaskById(newTaskId).getBody().as(TaskResponse.class));

        var databaseGivenTask = step("WHEN: Получена запись из таблицы task с id = " + newTaskId, () -> taskFunctions.getTaskByTaskId(newTaskId));

        step("THEN: Параметры полученной задачи соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Id задачи соответствует ожидаемому",
                                () -> assertEquals(givenTask.getId(),
                                        databaseGivenTask.getId())),

                        () -> step("title задачи соответствует ожидаемому",
                                () -> assertEquals(givenTask.getTitle(),
                                        databaseGivenTask.getTitle())),

                        () -> step("description задачи соответствует ожидаемому",
                                () -> assertEquals(givenTask.getDescription(),
                                        databaseGivenTask.getDescription())),

                        () -> step("level задачи соответствует ожидаемому",
                                () -> assertEquals(givenTask.getLevel(),
                                        databaseGivenTask.getLevel())),

                        () -> step("langs задачи соответствует ожидаемому",
                                () -> assertEquals(givenTask.getLangs(),
                                        databaseGivenTask.getLangs())),

                        () -> step("sent_solutions задачи соответствует ожидаемому",
                                () -> assertEquals(givenTask.getSentSolutions(),
                                        databaseGivenTask.getSentSolution())),

                        () -> step("Id создателя задачи соответствует ожидаемому",
                                () -> assertEquals(givenTask.getOwner().getId(),
                                        databaseGivenTask.getOwnerId()))
                ));
    }
    @Test
    @DisplayName("Получение задачи по несуществующему id")
    @Description("GET /api/v1/task")
    public void taskGetByIncorrectIdTest() {

        var givenTask = step("WHEN: Попытка получить задачу по несуществующему id завершается ошибкой",
                () -> taskApi.getTaskById(newTaskId + 10));

        step("THEN: Параметры полученной ошибки соответствуют ожидаемым",
                () -> assertAll(
                        () -> step("Код ошибки равен полученному коду",
                                () -> assertEquals(givenTask.getStatusCode(), 404)),

                        () -> step("Сообщение об ошибке соответствует ожидаемому",
                                () -> assertTrue(givenTask.getBody().asPrettyString()
                                        .contains("The item does not exist")))
                ));
    }

    @AfterAll
    public void deleteCreatedTask() {
        taskFunctions.deleteTaskById(newTaskId);
    }
}
