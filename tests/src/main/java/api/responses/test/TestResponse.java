package api.responses.test;

import api.responses.task.TaskResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestResponse {

    private Integer id;

    private String status;

    private TaskResponse task;

    private String input;

    private String output;

    @JsonProperty(value = "test_number")
    private Integer testNumber;

    public TestResponse id(Integer id) {
        this.id = id;
        return this;
    }

    public TestResponse status(String status) {
        this.status = status;
        return this;
    }

    public TestResponse input(String input) {
        this.input = input;
        return this;
    }

    public TestResponse output(String output) {
        this.output = output;
        return this;
    }

    public TestResponse testNumber(Integer testNumber) {
        this.testNumber = testNumber;
        return this;
    }

    public TestResponse task(TaskResponse task) {
        this.task = task;
        return this;
    }

}
