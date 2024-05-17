package api.responses.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedTests {

    @JsonProperty(value = "tests_created")
    private Integer testsCreated;

}
