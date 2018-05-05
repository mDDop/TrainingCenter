package pl.hubertgawrys.MyTrainingCenter.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.hubertgawrys.MyTrainingCenter.models.UserModel;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Integer> {

    boolean existsUserModelByUsernameAndPassword(String username, String password);
    boolean existsUserModelByUsername(String username);
    boolean existsUserModelByEmail(String email);
    UserModel findUserModelByUsernameAndPassword(String username, String password);

}
