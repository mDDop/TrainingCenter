package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.hubertgawrys.MyTrainingCenter.models.ResultModel;
import pl.hubertgawrys.MyTrainingCenter.models.TrainingModel;
import pl.hubertgawrys.MyTrainingCenter.models.WorkoutModel;
import pl.hubertgawrys.MyTrainingCenter.models.forms.SetForm;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.*;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;
import pl.hubertgawrys.MyTrainingCenter.models.services.WorkService;

import javax.validation.Valid;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TrainingController {

    final UserService userService;

    final UserRepository userRepository;

    final DayRepository dayRepository;

    final TrainingRepository trainingRepository;

    final ExerciseRepository exerciseRepository;

    final WorkoutRepository workoutRepository;

    final WorkService workService;

    @Autowired
    public TrainingController(UserService userService, UserRepository userRepository, DayRepository dayRepository, TrainingRepository trainingRepository, ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository, WorkService workService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.dayRepository = dayRepository;
        this.trainingRepository = trainingRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.workService = workService;
    }

    @ModelAttribute
    public Model modelAddatr(Model model){
        SetForm setForm = new SetForm();
        model.addAttribute("setForm", setForm);
        model.addAttribute("username", userService.getUserModel().getUsername());
        model.addAttribute("exerciseNames", exerciseRepository.findAllByUserModelEquals(userService.getUserModel()));
        return model;
    }

    @GetMapping("/training/{date}")
    public String mainGet(@PathVariable("date") Date date,
                          Model model){
        List<TrainingModel> trainingsOnDate = new ArrayList<>();
        List<WorkoutModel> workoutsOnDate = workoutRepository.findAllByDayModelAndUserModel(dayRepository.findByDateNameAndUserModels(date, userService.getUserModel()).get(), userService.getUserModel());

        for (int i = 0; i<workoutsOnDate.size(); i++){
            trainingsOnDate.addAll(trainingRepository.findAllByWorkoutModel(workoutsOnDate.get(i)));
        }
        List<ResultModel> resultsAllWorkouts = workService.calculateWorkWorkout();


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
        model.addAttribute("trainings", trainingsOnDate);
        model.addAttribute("workoutsOnDate", workoutsOnDate);
        model.addAttribute("workoutId", workoutId);
        model.addAttribute("dateId", dayRepository.findByDateNameAndUserModels(date, userService.getUserModel()).get().getId());
        return "training";
    }


    @PostMapping("/set/add")
    public String setAddPost(@Valid @ModelAttribute("setForm") SetForm setForm,
                             BindingResult bindingResult,
                             @RequestParam("workoutId") int workoutId,
                             Model model){
        if (!bindingResult.hasErrors()) {
            TrainingModel trainingModel = new TrainingModel();
            System.out.println(setForm.toString());
            trainingModel.setWorkoutModel(workoutRepository.findById(workoutId).get());
            trainingModel.setKilos(setForm.getKilos());
            trainingModel.setReps(setForm.getReps());
            trainingRepository.save(trainingModel);
            model.addAttribute("workoutId", workoutId);
        } else {

        }
        return "redirect:/training/" + dayRepository.findByIdEquals(workoutRepository.findById(workoutId).get().getDayModel().getId()).getDateName().toString() + "/" + workoutId;
    }

    @PostMapping("/set/delete")
    public String setDeletePost(@RequestParam("trainingId") int trainingId,
                                @RequestParam("workoutId") int workoutId,
                                Model model){
        trainingRepository.deleteById(trainingId);
        return "redirect:/training/" + dayRepository.findByIdEquals(workoutRepository.findById(workoutId).get().getDayModel().getId()).getDateName().toString() + "/" + workoutId;
    }

    @PostMapping("/workout/delete")
    public String workoutDeletePost(@RequestParam("workoutId") int workoutId,
                                    @RequestParam("dayName") String dayName){
    List<TrainingModel> trainingsToDeleteWithWorkout = trainingRepository.findAllByWorkoutModel(workoutRepository.findById(workoutId).get());
        for (TrainingModel trainingModel : trainingsToDeleteWithWorkout) {
            trainingRepository.deleteById(trainingModel.getId());
        }
        workoutRepository.delete(workoutRepository.findById(workoutId).get());
        return "redirect:/training/" + dayName;
    }

    @PostMapping("/workout/add")
    public String workoutAddPost(@RequestParam("id") int dateId,
                                 @RequestParam("exerciseId") int exerciseId,
                                 Model model){
        if (workoutRepository.existsByDayModel_IdAndExerciseModel_IdAndUserModel(
                dateId, exerciseId, userService.getUserModel())
                ) {
        } else {
            WorkoutModel workoutModel = new WorkoutModel();
            workoutModel.setUserModel(userService.getUserModel());
            workoutModel.setDayModel(dayRepository.findByIdEquals(dateId));
            workoutModel.setExerciseModel(exerciseRepository.findByIdEquals(exerciseId).get());
            workoutRepository.save(workoutModel);
        }
        return "redirect:/training/" + dayRepository.findByIdEquals(dateId).getDateName().toString();
    }
}
