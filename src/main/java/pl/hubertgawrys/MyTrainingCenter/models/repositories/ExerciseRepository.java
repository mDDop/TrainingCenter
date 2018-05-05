package pl.hubertgawrys.MyTrainingCenter.models.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.hubertgawrys.MyTrainingCenter.models.ExerciseModel;
import pl.hubertgawrys.MyTrainingCenter.models.UserModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends CrudRepository<ExerciseModel, Integer> {
    @Query(value ="SELECT * FROM `exercise` WHERE id>?1", nativeQuery = true)
    List<ExerciseModel> findThat(int idNumber);


    Optional<ExerciseModel> findByIdEquals(int idNumber);
    List<ExerciseModel> findAllByUserModelEquals(UserModel userModel);
    boolean existsByNameAndUserModel(String name, UserModel userModel);
}
