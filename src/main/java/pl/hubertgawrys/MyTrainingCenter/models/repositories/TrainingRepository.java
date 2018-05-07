package pl.hubertgawrys.MyTrainingCenter.models.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.hubertgawrys.MyTrainingCenter.models.TrainingModel;
import pl.hubertgawrys.MyTrainingCenter.models.UserModel;
import pl.hubertgawrys.MyTrainingCenter.models.WorkoutModel;

import java.util.List;

@Repository
public interface TrainingRepository extends CrudRepository<TrainingModel, Integer> {

    //List<TrainingModel> findAllByUserIdAndDateId(int id, int id);
    List<TrainingModel> findAllByWorkoutModel(WorkoutModel workoutModel);
    //List<TrainingModel> findAllByWorkoutIdIsLike (List<Integer> listId);
    //void deleteTrainingModelsByDateId(int id);
    //void deleteTrainingModelsByWorkoutId(int workoutId);
    void deleteById(int trainingId);
    void deleteAllByWorkoutModel(WorkoutModel workoutModel);

    @Query(value ="SELECT * FROM `training` INNER JOIN `workout` ON training.workout_id=workout.id", nativeQuery = true)
    List<TrainingModel> findJoin();

    @Query(value ="SELECT * FROM `training` INNER JOIN `workout` ON training.workout_id=workout.id WHERE workout.user_id=?1", nativeQuery = true)
    List<TrainingModel> findTrainingForUser(int userId);

    @Query(value ="SELECT SUM(kilos*reps), workout.date_id " +
            "FROM `training` JOIN `workout` ON workout_id=workout.id GROUP BY workout.date_id", nativeQuery = true)
    int[][] findWorkByDays();

    boolean existsByWorkoutModel_ExerciseModel_Id(int exerciseId);
   // List<TrainingModel> findAllByExerciseIdEquals(int idExercise);
  //  Optional<TrainingModel> findFirstByExerciseIdEquals(int idExercise);
    List<TrainingModel> findByWorkoutModel_ExerciseModel_BodypartModel_IdAndWorkoutModel_UserModel(int bodypartId, UserModel userModel);
}
