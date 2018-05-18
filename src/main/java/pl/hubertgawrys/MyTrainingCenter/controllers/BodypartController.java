package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.hubertgawrys.MyTrainingCenter.models.BodypartModel;
import pl.hubertgawrys.MyTrainingCenter.models.WorkoutModel;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.BodypartRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.DayRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.TrainingRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.WorkoutRepository;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;
import pl.hubertgawrys.MyTrainingCenter.models.services.WorkService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BodypartController  {

    final DayRepository dayRepository;

    final TrainingRepository trainingRepository;

    final UserService userService;

    final WorkoutRepository workoutRepository;
    final BodypartRepository bodypartRepository;
    final WorkService workService;

    @Autowired
    public BodypartController(DayRepository dayRepository, TrainingRepository trainingRepository, UserService userService, WorkoutRepository workoutRepository, BodypartRepository bodypartRepository, WorkService workService) {
        this.dayRepository = dayRepository;
        this.trainingRepository = trainingRepository;
        this.userService = userService;
        this.workoutRepository = workoutRepository;
        this.bodypartRepository = bodypartRepository;
        this.workService = workService;

    }

    @GetMapping("/bodyparts")
    public String bodypartsGet(Model model){
        List<BodypartModel> bodypartModelList= bodypartRepository.findAllByUserModelEquals(userService.getUserModel());
        List<WorkoutModel> workoutsUser = workoutRepository.findAllByUserModel(userService.getUserModel());
        Map<BodypartModel, Boolean> bodypartWithUsage = new HashMap<>();
        for (BodypartModel bodypartModel : bodypartModelList) {
            bodypartWithUsage.put(bodypartModel, false);
        }
            for (WorkoutModel workoutModel : workoutsUser) {
                if (bodypartWithUsage.containsKey(workoutModel.getExerciseModel().getBodypartModel())) {
                    bodypartWithUsage.put(workoutModel.getExerciseModel().getBodypartModel(), true);
                }
        }
        model.addAttribute("bodyparts", bodypartWithUsage);
    return "bodyparts";
    }

    @PostMapping("/bodypart/delete/{bodypartId}")
    public String bodypartPost(@PathVariable("bodypartId") int bodypartId,
                               Model model){
        if (workoutRepository.existsWorkoutModelByExerciseModel_BodypartModel_Id(bodypartId)){
            return "redirect:/bodyparts";
        } else {
            bodypartRepository.deleteById(bodypartId);
        }

        return "redirect:/bodyparts";
    }

    @PostMapping("bodyparts/add")
    public String bodypartPostAdd(@RequestParam("nameBodypart") String nameBodypart){
        BodypartModel bodypartModel = new BodypartModel();
        bodypartModel.setName(nameBodypart);
        bodypartModel.setUserModel(userService.getUserModel());
        bodypartRepository.save(bodypartModel);
        return "redirect:/bodyparts";
    }
}
