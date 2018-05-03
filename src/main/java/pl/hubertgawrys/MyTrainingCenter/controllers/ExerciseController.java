package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.hubertgawrys.MyTrainingCenter.models.forms.ExerciseForm;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.ExerciseRepository;
import pl.hubertgawrys.MyTrainingCenter.models.services.ExerciseService;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;

@Controller
public class ExerciseController {

    final
    ExerciseRepository exerciseRepository;

    final
    ExerciseService exerciseService;

    final UserService userService;


    @Autowired
    public ExerciseController(ExerciseRepository exerciseRepository, ExerciseService exerciseService, UserService userService) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseService = exerciseService;
        this.userService = userService;
    }

    @ModelAttribute
    public Model inputModel(Model model){
        model.addAttribute("username", userService.getUserModel().getUsername());
        return model;
    }


    @GetMapping("/group")
    public String groupGet(Model model){
        System.out.println(exerciseRepository.findThat(0));
        model.addAttribute("groups", exerciseRepository.findThat(0));
        ExerciseForm exerciseForm = new ExerciseForm();
        model.addAttribute("exerciseForm", exerciseForm);
        return "group";
    }

    @PostMapping("/group")
    public String groupPost(@ModelAttribute("exerciseForm") ExerciseForm exerciseForm,
                            Model model){
        exerciseService.addGroup(exerciseForm);
        model.addAttribute("groups", exerciseRepository.findThat(0));
        return "redirect:/group";
    }

    @GetMapping("/group/delete/{idDelete}")
    public String groupDelete(@PathVariable("idDelete") int idDelete){
        exerciseRepository.deleteById(idDelete);
        return "redirect:/group";
    }
}
