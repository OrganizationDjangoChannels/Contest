package api.database.repos;

import api.database.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    @Query(nativeQuery = true,
            value = "select points from profile " +
                    "where user_id = ?1")
    Double getPointsByUserId(Integer id);

}
