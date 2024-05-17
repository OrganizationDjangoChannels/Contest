package api;

import api.requests.auth.TokenRequestBody;
import api.requests.task.TaskRequest;
import api.responses.auth.TokenResponseBody;
import org.springframework.lang.Nullable;

import static io.restassured.RestAssured.given;

public class TestUtils {

    public static TaskRequest generateTaskRequestBody(Integer id, String description, String title, String langs, Integer level, @Nullable String owner) {
        return new TaskRequest()
                .id(id)
                .description(description)
                .langs(langs)
                .level(level)
                .title(title)
                .owner(owner);
    }

    public static String getUserAuthToken(String username, String password) {
        return "Token " + given()
                .contentType("application/json")
                .body(new TokenRequestBody()
                        .username(username)
                        .password(password))
                .post("http://127.0.0.1:8000/api/v1/login/").getBody().as(TokenResponseBody.class).getToken();
    }
}
