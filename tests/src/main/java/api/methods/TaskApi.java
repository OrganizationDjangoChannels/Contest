package api.methods;

import api.requests.task.TaskRequest;
import api.responses.task.TaskResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static api.TestUtils.getUserAuthToken;
import static io.restassured.RestAssured.given;

@Service
@RequiredArgsConstructor
public class TaskApi {

    public TaskResponse[] getTasksList() {
        return given()
                .get("http://127.0.0.1:8000/api/v1/task/")
                .getBody().as(TaskResponse[].class);
    }

    @Step("Неотображаемый step")
    public Response getTaskById(Integer id) {
        return given()
                .pathParam("id", id)
                .get("http://127.0.0.1:8000/api/v1/task/{id}");
    }

    @Step("[POST/api/v1/task] Создаем задачу с переданными параметрами")
    public Response createNewTask(TaskRequest taskRequest, String username, String password) {
        return given()
                .header("Authorization", getUserAuthToken(username, password))
                .contentType("application/json")
                .body(taskRequest)
                .post("http://127.0.0.1:8000/api/v1/task/");

    }

    @Step("[PUT/api/v1/task/{id}] Изменяем параметры задачи с id = {id}")
    public Response updateExistingTask(Integer id, TaskRequest taskRequest, String username, String password) {
        return given()
                .contentType("application/json")
                .header("Authorization", getUserAuthToken(username, password))
                .pathParam("id", id)
                .body(taskRequest)
                .put("http://127.0.0.1:8000/api/v1/task/{id}/");
    }


}
