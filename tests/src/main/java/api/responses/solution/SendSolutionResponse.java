package api.responses.solution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SendSolutionResponse {

    private String file;

    private String lang;

    private String status;

    @JsonProperty(value = "passed_tests")
    private Integer passedTests;

    private Double points;

}
