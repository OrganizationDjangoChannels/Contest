package api.requests.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TestRequest {

    @JsonProperty(value = "task_id")
    private Integer taskId;

    private List<NewTest> tests = new ArrayList<>();

    public TestRequest taskId(Integer taskId) {
        this.taskId = taskId;
        return this;
    }

    public TestRequest tests(List<String> tests) {
        for (int i = 0; i < tests.size() - 1; i+=2) {
            this.tests.add(new NewTest()
                    .input(tests.get(i))
                    .output(tests.get(i + 1))
                    .testNumber((i + 1) / 2 + 1)
            );
        }
        return this;
    }
}
