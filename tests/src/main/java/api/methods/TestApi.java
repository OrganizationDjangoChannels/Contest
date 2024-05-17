package api.methods;

import api.database.entities.Test;
import api.requests.test.TestRequest;
import api.responses.test.TestResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static api.TestUtils.getUserAuthToken;
import static io.restassured.RestAssured.given;

@Service
@RequiredArgsConstructor
public class TestApi {

    @Step("[GET /api/v1/test/?task_id={taskId}] Получаем все тесты у задачи с id = {taskId}")
    public Response getTaskTests(Integer taskId) {
        return given()
                .pathParam("task_id", taskId)
                .get("http://127.0.0.1:8000/api/v1/test/?task_id={task_id}");
    }

    @Step("[POST /api/v1/test] Создаем тесты к задаче")
    public Response createTests(TestRequest testRequest, String username, String password) {
        return given()
                .header("Authorization", getUserAuthToken(username, password))
                .contentType("application/json")
                .body(testRequest)
                .post("http://127.0.0.1:8000/api/v1/test/");
    }

    @Step("[PUT /api/v1/test/?task_id={taskId}] ОБновляем тесты у задачи")
    public Response updateTests(Integer taskId, TestResponse[] updateTestsBody, String username, String password) {
        return given()
                .pathParam("task_id", taskId)
                .header("Authorization", getUserAuthToken(username, password))
                .body(updateTestsBody)
                .contentType("application/json")
                .put("http://127.0.0.1:8000/api/v1/test/{task_id}/");
    }
}
