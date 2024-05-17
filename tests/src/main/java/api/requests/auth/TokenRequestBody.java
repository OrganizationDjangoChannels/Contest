package api.requests.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestBody {

    private String username;

    private String password;

    public TokenRequestBody username(String username) {
        this.username = username;
        return this;
    }

    public TokenRequestBody password(String password) {
        this.password = password;
        return this;
    }

}
