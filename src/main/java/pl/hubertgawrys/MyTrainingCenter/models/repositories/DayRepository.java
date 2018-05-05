package pl.hubertgawrys.MyTrainingCenter.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.hubertgawrys.MyTrainingCenter.models.DayModel;
import pl.hubertgawrys.MyTrainingCenter.models.TrainingModel;
import pl.hubertgawrys.MyTrainingCenter.models.UserModel;

import javax.jws.soap.SOAPBinding;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DayRepository extends CrudRepository<DayModel, Integer> {

    Optional<DayModel> findByDateNameAndUserModels(Date date, UserModel userModel);
    List<DayModel> findAllByUserModelsEquals(UserModel userModel);
    DayModel findByIdEquals(int id);
}
