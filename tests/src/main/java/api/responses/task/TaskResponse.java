package api.responses.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TaskResponse {

    private Integer id;

    private String title;

    private String description;

    private Integer level;

    private String langs;

    private Owner owner;

    @JsonProperty(value = "sent_solutions")
    private Integer sentSolutions;

    private String message;

}
