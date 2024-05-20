package api.database.functions;

import api.database.repos.AuthUserRepository;
import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserFunctions {

    private final AuthUserRepository authUserRepository;

    @Step("Получаем id пользователя c username = {username}")
    public Integer getAuthUserIdByUsername(String username) {
        return authUserRepository.getAuthUserIdByUsername(username);
    }
}
