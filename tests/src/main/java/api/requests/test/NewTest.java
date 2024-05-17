package api.requests.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewTest {

    private String input;

    private String output;

    @JsonProperty(value = "test_number")
    private Integer testNumber;

    public NewTest input(String input) {
        this.input = input;
        return this;
    }

    public NewTest output(String output) {
        this.output = output;
        return this;
    }

    public NewTest testNumber(Integer testNumber) {
        this.testNumber = testNumber;
        return this;
    }
}
