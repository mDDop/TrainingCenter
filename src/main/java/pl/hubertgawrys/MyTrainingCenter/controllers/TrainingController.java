package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.hubertgawrys.MyTrainingCenter.models.ExerciseModel;
import pl.hubertgawrys.MyTrainingCenter.models.TrainingModel;
import pl.hubertgawrys.MyTrainingCenter.models.WorkoutModel;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.*;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Controller
public class TrainingController {

    final
    UserService userService;

    final
    UserRepository userRepository;

    final
    DayRepository dayRepository;

    final
    TrainingRepository trainingRepository;

    final ExerciseRepository exerciseRepository;

    final WorkoutRepository workoutRepository;

    @Autowired
    public TrainingController(UserService userService, UserRepository userRepository, DayRepository dayRepository, TrainingRepository trainingRepository, ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.dayRepository = dayRepository;
        this.trainingRepository = trainingRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
    }

    @GetMapping("/training/{date}")
    public String mainGet(@PathVariable("date") Date date,
                          Model model){
        List<TrainingModel> trainingsOnDate = new ArrayList<>();
        List<WorkoutModel> workoutsOnDate = workoutRepository.findAllByDayModelAndUserModel(dayRepository.findByDateNameAndUserModels(date, userService.getUserModel()).get(), userService.getUserModel());

        for (int i = 0; i<workoutsOnDate.size(); i++){
            trainingsOnDate.addAll(trainingRepository.findAllByWorkoutModel(workoutsOnDate.get(i)));
        }

        model.addAttribute("username", userService.getUserModel().getUsername());
        model.addAttribute("exerciseNames", exerciseRepository.findAllByUserModelEquals(userService.getUserModel()));
        model.addAttribute("trainings", trainingsOnDate);
        model.addAttribute("workoutsOnDate", workoutsOnDate);
        model.addAttribute("dateId", dayRepository.findByDateNameAndUserModels(date, userService.getUserModel()).get().getId());
        return "training";
    }

    @GetMapping("/training/{date}/{workoutId}")
    public String mainGet(@PathVariable("date") Date date,
                          @PathVariable("workoutId") int workoutId,
                          Model model){
        List<TrainingModel> trainingsOnDate = new ArrayList<>();
        List<WorkoutModel> workoutsOnDate = workoutRepository.findAllByDayModelAndUserModel(dayRepository.findByDateNameAndUserModels(date, userService.getUserModel()).get(), userService.getUserModel());

        for (int i = 0; i<workoutsOnDate.size(); i++){
            trainingsOnDate.addAll(trainingRepository.findAllByWorkoutModel(workoutsOnDate.get(i)));
        }

        model.addAttribute("username", userService.getUserModel().getUsername());
        model.addAttribute("exerciseNames", exerciseRepository.findAllByUserModelEquals(userService.getUserModel()));
        model.addAttribute("trainings", trainingsOnDate);
        model.addAttribute("workoutsOnDate", workoutsOnDate);
        model.addAttribute("workoutId", workoutId);
        model.addAttribute("dateId", dayRepository.findByDateNameAndUserModels(date, userService.getUserModel()).get().getId());
        return "training";
    }


    @PostMapping("/set/add")
    public String setAddPost(@RequestParam("reps") int reps,
                             @RequestParam("kilos") int kilos,
                             @RequestParam("workoutId") int workoutId,
                             Model model){
     TrainingModel trainingModel = new TrainingModel();
     trainingModel.setWorkoutModel(workoutRepository.findById(workoutId).get());
     trainingModel.setKilos(kilos);
     trainingModel.setReps(reps);
     trainingRepository.save(trainingModel);
        model.addAttribute("workoutId", workoutId);
        return "redirect:/training/" + dayRepository.findByIdEquals(workoutRepository.findById(workoutId).get().getDayModel().getId()).getDateName().toString() + "/" + workoutId;
    }

    @PostMapping("/set/delete")
    public String setDeletePost(@RequestParam("trainingId") int trainingId,
                                @RequestParam("workoutId") int workoutId,
                                Model model){
        trainingRepository.deleteById(trainingId);
        return "redirect:/training/" + dayRepository.findByIdEquals(workoutRepository.findById(workoutId).get().getDayModel().getId()).getDateName().toString() + "/" + workoutId;
    }

    @PostMapping("/workout/add")
    public String workoutAddPost(@RequestParam("id") int dateId,
                                 @RequestParam("exerciseId") int exerciseId,
                                 Model model){
        WorkoutModel workoutModel = new WorkoutModel();
        workoutModel.setUserModel(userService.getUserModel());
        workoutModel.setDayModel(dayRepository.findByIdEquals(dateId));
        workoutModel.setExerciseModel(exerciseRepository.findByIdEquals(exerciseId).get());
        workoutRepository.save(workoutModel);
        return "redirect:/training/" + dayRepository.findByIdEquals(dateId).getDateName().toString();
    }
}
