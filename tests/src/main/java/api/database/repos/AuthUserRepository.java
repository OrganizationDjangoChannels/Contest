package api.database.repos;

import api.database.entities.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    @Query(nativeQuery = true,
    value = "select id from auth_user " +
            "where username = ?1")
    Integer getAuthUserIdByUsername(String username);
}
