/*package solution;

import api.database.ContestDatabaseAutoConfiguration;
import api.database.functions.*;
import api.methods.SolutionApi;
import api.methods.TaskApi;
import api.methods.TestApi;
import api.requests.test.TestRequest;
import api.responses.solution.SendSolutionResponse;
import api.responses.task.TaskResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Param;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.List;

import static api.TestUtils.generateTaskRequestBody;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { ContestDatabaseAutoConfiguration.class, SolutionApi.class, TaskApi.class, TestApi.class })
@ActiveProfiles(profiles = "test")
@Epic("Solution API")
@Feature("Solution POST")
@DisplayName("Тесты на отправку решения к задаче")
public class SolutionSendTests {

    @Autowired
    private SolutionFunctions solutionFunctions;

    @Autowired
    private SolutionApi solutionApi;

    @Autowired
    private ProfileFunctions profileFunctions;

    @Autowired
    private AuthUserFunctions authUserFunctions;

    @Autowired
    private TaskApi taskApi;

    @Autowired
    private TestApi testApi;

    @Autowired
    private TaskFunctions taskFunctions;

    @Autowired
    private TestFunctions testFunctions;

    private Integer newTaskId;

    private static final String BASE_URL = "src/test/resources/";

    @BeforeEach
    public void createTaskAndTests() {
        newTaskId = taskApi.createNewTask(generateTaskRequestBody(
                1,
                "Вывести сумму двух переданных чисел",
                "Сумма чисел",
                "С|С++|Python|Java",
                1,
                null
        ), "dmitry", "12345").getBody().as(TaskResponse.class).getId();

        testApi.createTests(new TestRequest()
                .taskId(newTaskId)
                .tests(List.of("2 4", "6", "3 8", "11")), "dmitry", "12345");
    }

    public static List<Arguments> getSolutionFileAndLang() {
        return List.of(
                Arguments.of("Solution.py", "Python"),
                Arguments.of("Solution.cpp", "C++"),
                Arguments.of("Solution.c", "C"),
                Arguments.of("Solution.java", "Java")
        );
    }

    @ParameterizedTest
    @MethodSource("getSolutionFileAndLang")
    @DisplayName("Проверка отправки успешного решения на всех доступных языках программирования")
    @Description("POST /api/v1/solution")
    public void sendSuccessfulSolution(@Param("Имя файла с решением") String fileName,
                                       @Param("Язык, на котором написано решение") String lang) {

        var userPoints = step("GIVEN: Получено количество очков пользователя до решения задачи",
                () -> profileFunctions.getPointsByUserId(authUserFunctions.getAuthUserIdByUsername("dmitry")));

        var sendedSolution = step("WHEN: Отправлено успешное решение задачи",
                () -> solutionApi.sendTaskSolution(new File(BASE_URL + fileName), lang, newTaskId, "dmitry", "12345")
                        .getBody().as(SendSolutionResponse.class));

        var newUserPoints = step("AND: Получено количество очков пользователя после решения задачи",
                () -> profileFunctions.getPointsByUserId(authUserFunctions.getAuthUserIdByUsername("dmitry")));

        var taskInfo = step("AND: Получена информация о созданной задаче",
                () -> taskFunctions.getTaskByTaskId(newTaskId));

        var sendedDatabaseSolution = step("AND: Получено отправленное решение по задачи из таблицы solution",
                () -> solutionFunctions.getTaskSolutionsByTaskId(newTaskId));

        step("THEN: Параметры полученного решения соответствуют ожидаемым",
                () -> assertAll(

                        () -> step("Количество очков у пользователя увеличилось на полученные очки за решение задачи",
                                () -> assertEquals(userPoints + sendedSolution.getPoints(), newUserPoints)),

                        () -> step("Количество решений задачи увеличилось на единицу",
                                () -> assertEquals(taskInfo.getSentSolution(), 1)),

                        () -> step("Количество баллов, полученных за задачу, соответствует ожидаемому",
                                () -> assertEquals(sendedSolution.getPoints(), sendedDatabaseSolution.get(0).getPoints())),

                        () -> step("Значение passed_tests соответствует ожидаемому",
                                () -> assertEquals(sendedSolution.getPassedTests(), sendedDatabaseSolution.get(0).getPassedTests())),

                        () -> step("Файл с решением соответствует тому, который отправляли",
                                () -> assertTrue(sendedDatabaseSolution.get(0).getFile().contains(fileName))),

                        () -> step("Язык, на котором отправлено решение, соответствует ожидаемому",
                                () -> assertEquals(sendedSolution.getLang(), sendedDatabaseSolution.get(0).getLang())),

                        () -> step("Статус решения задачи соответствует ожидаемому",
                                () -> assertEquals(sendedDatabaseSolution.get(0).getStatus(), "solved"))
                ));
    }
    @AfterEach
    public void deleteSolutionAndTask() {
        solutionFunctions.deleteSolutionByTaskId(newTaskId);
        testFunctions.deleteTestsByTaskId(newTaskId);
        taskFunctions.deleteTaskById(newTaskId);
    }
}
*/