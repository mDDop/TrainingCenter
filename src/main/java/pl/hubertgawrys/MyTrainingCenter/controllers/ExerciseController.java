package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.hubertgawrys.MyTrainingCenter.models.ExerciseModel;
import pl.hubertgawrys.MyTrainingCenter.models.ResultModel;
import pl.hubertgawrys.MyTrainingCenter.models.forms.ExerciseForm;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.BodypartRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.ExerciseRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.TrainingRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.WorkoutRepository;
import pl.hubertgawrys.MyTrainingCenter.models.services.ExerciseService;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;
import pl.hubertgawrys.MyTrainingCenter.models.services.WorkService;

import java.util.LinkedList;
import java.util.List;

@Controller
public class ExerciseController {

    final ExerciseRepository exerciseRepository;

    final ExerciseService exerciseService;

    final UserService userService;

    final TrainingRepository trainingRepository;

    final WorkoutRepository workoutRepository;

    final WorkService workService;

    final BodypartRepository bodypartRepository;

    @Autowired
    public ExerciseController(ExerciseRepository exerciseRepository, ExerciseService exerciseService, UserService userService, TrainingRepository trainingRepository, WorkoutRepository workoutRepository, WorkService workService, BodypartRepository bodypartRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseService = exerciseService;
        this.userService = userService;
        this.trainingRepository = trainingRepository;
        this.workoutRepository = workoutRepository;
        this.workService = workService;
        this.bodypartRepository = bodypartRepository;
    }

    @ModelAttribute
    public Model inputModel(Model model){
        model.addAttribute("username", userService.getUserModel().getUsername());
        model.addAttribute("bodyparts", bodypartRepository.findAllByUserModelEquals(userService.getUserModel()));
        return model;
    }


    @GetMapping("/group")
    public String groupGet(Model model){
        List<ExerciseModel> exercisesName = exerciseRepository.findAllByUserModelEquals(userService.getUserModel());
        List<ResultModel> exerciseNamesWithUsage = new LinkedList<>();
        model.addAttribute("groups", exercisesName);
        for (ExerciseModel exerciseModel : exercisesName) {
            ResultModel resultModel = new ResultModel();
            resultModel.setT(exerciseModel);
             if (trainingRepository.existsByWorkoutModel_ExerciseModel_Id(exerciseModel.getId())
                     || workoutRepository.existsByExerciseModel_Id(exerciseModel.getId())){
                 resultModel.setResult(0);
             } else {
                 resultModel.setResult(1);
             }
             exerciseNamesWithUsage.add(resultModel);
        }
        ExerciseForm exerciseForm = new ExerciseForm();

        model.addAttribute("exerciseForm", exerciseForm);
        model.addAttribute("warningDelete", "");
        model.addAttribute("exercisesWithUsage", exerciseNamesWithUsage);
        return "group";
    }

    @PostMapping("/group")
    public String groupPost(@ModelAttribute("exerciseForm") ExerciseForm exerciseForm,
                            Model model){
        if (!exerciseRepository.existsByNameAndUserModel(exerciseForm.getNameGroup(),userService.getUserModel())){
        exerciseService.addGroup(exerciseForm);
        } else {

        }
        return "redirect:/group";
    }

    @GetMapping("/group/delete/{idDelete}")
    public String groupDelete(@PathVariable("idDelete") int idDelete,
                                Model model){
        if (!trainingRepository.existsByWorkoutModel_ExerciseModel_Id(idDelete) && !workoutRepository.existsByExerciseModel_Id(idDelete)) {
            System.out.println(idDelete);
            exerciseRepository.deleteById(idDelete);
        } else {
            model.addAttribute("warningDelete", "Can't remove this exercise. There are existing training using this exercise. Remove them first!");
        }
        return "redirect:/group";
    }

    @GetMapping("/modify/{exerciseId}")
    public String groupModify(@PathVariable("exerciseId") int exerciseId,
                                Model model){

            List<ResultModel> resultsWorkouts = workService.calculateWorkWorkout();
        System.out.println(resultsWorkouts);
            model.addAttribute("resultsExercise", resultsWorkouts);

            model.addAttribute("exercise", exerciseRepository.findByIdEqualsAndUserModelEquals(exerciseId, userService.getUserModel()));
            model.addAttribute("bodyparts", bodypartRepository.findAllByUserModelEquals(userService.getUserModel()));

    return "groupchange";
    }

    @PostMapping("/modify/{exerciseId}")
    public String groupModifyPost(@PathVariable("exerciseId") int exerciseId,
                                    @RequestParam("newName") String newName,
                                    @RequestParam("bodypartId") int bodypartId){
        System.out.println( newName + " " + bodypartId + " " + exerciseId);
        exerciseRepository.findByIdEquals(exerciseId).get().setName(newName);
        exerciseRepository.findByIdEquals(exerciseId).get().setBodypartModel(bodypartRepository.findByIdEquals(bodypartId));
        exerciseRepository.updateExerciseBodypart(exerciseId, bodypartId);
        exerciseRepository.updateExerciseName(exerciseId, newName);
        return "redirect:/group";
    }
}
