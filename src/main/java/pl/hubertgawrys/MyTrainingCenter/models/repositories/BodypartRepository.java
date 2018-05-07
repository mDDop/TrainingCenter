package pl.hubertgawrys.MyTrainingCenter.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.hubertgawrys.MyTrainingCenter.models.BodypartModel;
import pl.hubertgawrys.MyTrainingCenter.models.UserModel;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Repository
public interface BodypartRepository extends CrudRepository<BodypartModel, Integer> {

    List<BodypartModel> findAllByUserModelEquals(UserModel usermodel);
    BodypartModel findByIdEquals(int id);
    boolean existsByIdEquals(int id);
}
