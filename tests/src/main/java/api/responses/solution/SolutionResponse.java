package api.responses.solution;

import api.responses.task.Owner;
import api.responses.task.TaskResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SolutionResponse {

    private Integer id;

    private String file;

    private String lang;

    private Double points;

    private String status;

    private TaskResponse task;

    private Owner owner;

    @JsonProperty(value = "created_at")
    private String createdAt;

    @JsonProperty(value = "passed_tests")
    private Integer passedTests;

}
