package api.responses.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Owner {

    private Integer id;

    private User user;

    private Integer points;

    @JsonProperty(value = "created_at")
    private String createdAt;

    @JsonProperty(value = "solved_tasks")
    private Integer solvedTasks;

}