package api.requests.solution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class SolutionRequest {

    private File file;

    private String lang;

    @JsonProperty(value = "task_id")
    private Integer taskId;

    public SolutionRequest file(File file) {
        this.file = file;
        return this;
    }

    public SolutionRequest lang(String lang) {
        this.lang = lang;
        return this;
    }

    public SolutionRequest taskId(Integer taskId) {
        this.taskId = taskId;
        return this;
    }
}
