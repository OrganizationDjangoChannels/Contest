package api.database.functions;

import api.database.entities.Profile;
import api.database.repos.ProfileRepository;
import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileFunctions {

    private final ProfileRepository profileRepository;

    public Profile getProfileById(Integer id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Профиль с заданным id не существует"));
    }

    @Step("Получаем набранные очки пользователя с id = {id}")
    public Double getPointsByUserId(Integer id) {
        return profileRepository.getPointsByUserId(id);
    }

}
