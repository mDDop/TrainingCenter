package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.hubertgawrys.MyTrainingCenter.models.DayModel;
import pl.hubertgawrys.MyTrainingCenter.models.ResultModel;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.BodypartRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.DayRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.TrainingRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.UserRepository;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;
import pl.hubertgawrys.MyTrainingCenter.models.services.WorkService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    final UserService userService;
    final UserRepository userRepository;
    final DayRepository dayRepository;
    final BodypartRepository bodypartRepository;
    final TrainingRepository trainingRepository;
    final WorkService workService;

    @Autowired
    public MainController(UserService userService, UserRepository userRepository, TrainingRepository trainingRepository, DayRepository dayRepository, BodypartRepository bodypartRepository, WorkService workService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
        this.dayRepository = dayRepository;
        this.bodypartRepository = bodypartRepository;
        this.workService = workService;
    }


    @GetMapping("/")
    public String mainGet(Model model){
            model.addAttribute("username", userService.getUserModel().getUsername());
        model.addAttribute("bodyparts", bodypartRepository.findAllByUserModelEquals(userService.getUserModel()));
        return "main";
    }

    @PostMapping("/")
    public String mainPost(@RequestParam("choseBodypartId") int choseBodypartId,
                           Model model){
        model.addAttribute("username", userService.getUserModel().getUsername());
        model.addAttribute("bodyparts", bodypartRepository.findAllByUserModelEquals(userService.getUserModel()));

        if (choseBodypartId!=-1) {
            model.addAttribute("bodypartSelected", choseBodypartId);
            Map<DayModel, Integer> resultsBodypart = workService.calculateWorkBodyPart(choseBodypartId);
            model.addAttribute("resultsExercise", resultsBodypart);
        }
        return "main";
    }
}
