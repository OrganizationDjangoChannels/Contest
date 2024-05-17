package api.requests.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {

    private Integer id;

    private String description;

    private String langs;

    private Integer level;

    private String title;

    private String owner;

    public TaskRequest id(Integer id) {
        this.id = id;
        return this;
    }

    public TaskRequest description(String description) {
        this.description = description;
        return this;
    }

    public TaskRequest langs(String langs) {
        this.langs = langs;
        return this;
    }

    public TaskRequest level(Integer level) {
        this.level = level;
        return this;
    }

    public TaskRequest title(String title) {
        this.title = title;
        return this;
    }

    public TaskRequest owner(String owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString() {
        return String.format("{\"id\": %d, \"title\": \"%s\", \"description\": \"%s\", \"level\": \"%s\", \"langs\": \"%s\", \"owner\": null}", id, title, description, level, langs);
    }

}
