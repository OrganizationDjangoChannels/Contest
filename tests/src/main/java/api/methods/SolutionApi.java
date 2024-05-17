package api.methods;

import api.requests.solution.SolutionRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

import static api.TestUtils.getUserAuthToken;
import static io.restassured.RestAssured.given;

@Service
@RequiredArgsConstructor
public class SolutionApi {

    @Step("[GET /api/v1/solution/?task_id={taskId}] Получаем список решений к задаче по id = {taskId}")
    public Response getTaskSolutions(Integer taskId) {
        return given()
                .contentType("application/json")
                .pathParam("task_id", taskId)
                .get("http://127.0.0.1:8000/api/v1/solution/?task_id={task_id}");
    }

    @Step("[POST /api/v1/solution] Отправляем решение к задаче")
    public Response sendTaskSolution(File file, String lang, Integer taskId, String username, String password) {
        return given()
                .contentType("multipart/form-data")
                .header("Authorization", getUserAuthToken(username, password))
                .multiPart("file", file)
                .multiPart("lang", lang)
                .multiPart("task_id",taskId)
                .relaxedHTTPSValidation()
                .post("http://127.0.0.1:8000/api/v1/solution/");
    }
}
