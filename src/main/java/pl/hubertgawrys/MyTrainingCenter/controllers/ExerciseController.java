package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.hubertgawrys.MyTrainingCenter.models.ExerciseModel;
import pl.hubertgawrys.MyTrainingCenter.models.ResultModel;
import pl.hubertgawrys.MyTrainingCenter.models.forms.ExerciseForm;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.ExerciseRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.TrainingRepository;
import pl.hubertgawrys.MyTrainingCenter.models.services.ExerciseService;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;

import java.util.LinkedList;
import java.util.List;

@Controller
public class ExerciseController {

    final
    ExerciseRepository exerciseRepository;

    final
    ExerciseService exerciseService;

    final UserService userService;

    final TrainingRepository trainingRepository;


    @Autowired
    public ExerciseController(ExerciseRepository exerciseRepository, ExerciseService exerciseService, UserService userService, TrainingRepository trainingRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseService = exerciseService;
        this.userService = userService;
        this.trainingRepository = trainingRepository;
    }

    @ModelAttribute
    public Model inputModel(Model model){
        model.addAttribute("username", userService.getUserModel().getUsername());
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
             if (trainingRepository.existsByWorkoutModel_ExerciseModel_Id(exerciseModel.getId())){
                 resultModel.setResult(0);
             } else {
                 resultModel.setResult(1);
             }
             exerciseNamesWithUsage.add(resultModel);
        }
        ExerciseForm exerciseForm = new ExerciseForm();
        System.out.println(exerciseNamesWithUsage);

        model.addAttribute("exerciseForm", exerciseForm);
        model.addAttribute("warningDelete", "");
        model.addAttribute("exercisesWithUsage", exerciseNamesWithUsage);
        return "group";
    }

    @PostMapping("/group")
    public String groupPost(@ModelAttribute("exerciseForm") ExerciseForm exerciseForm,
                            Model model){
        exerciseService.addGroup(exerciseForm);
        return "redirect:/group";
    }

    @GetMapping("/group/delete/{idDelete}")
    public String groupDelete(@PathVariable("idDelete") int idDelete,
                                Model model){
        System.out.println(trainingRepository.existsByWorkoutModel_ExerciseModel_Id(idDelete));
        if (!trainingRepository.existsByWorkoutModel_ExerciseModel_Id(idDelete)) {
            exerciseRepository.deleteById(idDelete);
        } else {
            model.addAttribute("warningDelete", "Can't remove this exercise. There are existing training using this exercise. Remove them first!");
        }
        return "redirect:/group";
    }
}
