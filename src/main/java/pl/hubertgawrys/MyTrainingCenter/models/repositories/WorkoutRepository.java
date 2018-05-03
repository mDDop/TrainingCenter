package pl.hubertgawrys.MyTrainingCenter.models.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.hubertgawrys.MyTrainingCenter.models.DayModel;
import pl.hubertgawrys.MyTrainingCenter.models.UserModel;
import pl.hubertgawrys.MyTrainingCenter.models.WorkoutModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends CrudRepository<WorkoutModel, Integer> {


    List<WorkoutModel> findAllByDayModelAndUserModel(DayModel dayModel, UserModel userModel);
    List<WorkoutModel> findAllByUserModel(UserModel userModel);



}
