package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.hubertgawrys.MyTrainingCenter.models.TrainingModel;
import pl.hubertgawrys.MyTrainingCenter.models.UserModel;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.DayRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.TrainingRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.UserRepository;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Controller
public class MainController {

    final
    UserService userService;

    final
    UserRepository userRepository;

    final
    DayRepository dayRepository;

    final
    TrainingRepository trainingRepository;

    @Autowired
    public MainController(UserService userService, UserRepository userRepository, TrainingRepository trainingRepository, DayRepository dayRepository) {
        this.userService = userService;
        this.userRepository = userRepository;

        this.trainingRepository = trainingRepository;
        this.dayRepository = dayRepository;
    }


    @GetMapping("/")
    public String mainGet(Model model){
            model.addAttribute("username", userService.getUserModel().getUsername());
        return "main";
    }
}
