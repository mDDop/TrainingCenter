package pl.hubertgawrys.MyTrainingCenter.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import pl.hubertgawrys.MyTrainingCenter.models.ExerciseModel;
import pl.hubertgawrys.MyTrainingCenter.models.forms.ExerciseForm;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.BodypartRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.ExerciseRepository;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExerciseService {


    final ExerciseRepository exerciseRepository;

    final UserService userService;

    final BodypartRepository bodypartRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, UserService userService, BodypartRepository bodypartRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
        this.bodypartRepository = bodypartRepository;
    }

    public void addGroup(ExerciseForm exerciseForm){
        ExerciseModel newExerciseModel = new ExerciseModel();
        newExerciseModel.setName(exerciseForm.getNameGroup());
        newExerciseModel.setUserModel(userService.getUserModel());
        newExerciseModel.setBodypartModel(bodypartRepository.findByIdEquals(exerciseForm.getBodypartId()));
        exerciseRepository.save(newExerciseModel);
    }
}
