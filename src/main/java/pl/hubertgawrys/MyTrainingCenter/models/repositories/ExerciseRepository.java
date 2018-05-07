package pl.hubertgawrys.MyTrainingCenter.models.repositories;

import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.hubertgawrys.MyTrainingCenter.models.ExerciseModel;
import pl.hubertgawrys.MyTrainingCenter.models.UserModel;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ExerciseRepository extends CrudRepository<ExerciseModel, Integer> {
    @Query(value ="SELECT * FROM `exercise` WHERE id>?1", nativeQuery = true)
    List<ExerciseModel> findThat(int idNumber);


    Optional<ExerciseModel> findByIdEquals(int idNumber);
    List<ExerciseModel> findAllByUserModelEquals(UserModel userModel);
    boolean existsByNameAndUserModel(String name, UserModel userModel);
    Optional<ExerciseModel> findByIdEqualsAndUserModelEquals(int id, UserModel userModel);

    @Modifying
    @Query(value = "UPDATE exercise e SET e.name_group = ?2 WHERE e.id = ?1", nativeQuery = true)
    void updateExerciseName(int exerciseId, String exerciseName);

    @Modifying
    @Query(value = "UPDATE exercise e SET e.bodypart_id =:bodypartId WHERE e.id =:exerciseId", nativeQuery = true)
    void updateExerciseBodypart(@Param("exerciseId") int exerciseId, @Param("bodypartId") int bodypartId);
}
