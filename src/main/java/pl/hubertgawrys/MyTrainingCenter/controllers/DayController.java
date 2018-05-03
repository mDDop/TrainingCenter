package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.hubertgawrys.MyTrainingCenter.models.DayModel;
import pl.hubertgawrys.MyTrainingCenter.models.WorkoutModel;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.DayRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.TrainingRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.WorkoutRepository;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DayController {

    final
    DayRepository dayRepository;

    final
    TrainingRepository trainingRepository;

    final
    UserService userService;

    final WorkoutRepository workoutRepository;

    @Autowired
    public DayController(TrainingRepository trainingRepository, DayRepository dayRepository, UserService userService, WorkoutRepository workoutRepository) {
        this.trainingRepository = trainingRepository;
        this.dayRepository = dayRepository;
        this.userService = userService;
        this.workoutRepository = workoutRepository;
    }

    @ModelAttribute
    public Model addModel(Model model){
        model.addAttribute("username", userService.getUserModel().getUsername());
        return model;
    }

    @GetMapping("/day")
    public String dayGet(Model model){
        System.out.println(dayRepository.findAllByUserModelsEquals(userService.getUserModel()));
        model.addAttribute("days", dayRepository.findAllByUserModelsEquals(userService.getUserModel()));
        
        return "day";
    }

    @PostMapping("/day")
    public String deyAdd(@RequestParam("newDate") String date){
        DayModel newDayModel = new DayModel();
        System.out.println(date);
        newDayModel.setDateName(Date.valueOf(date));
        newDayModel.setUserModels(userService.getUserModel());
        dayRepository.save(newDayModel);
        return "redirect:/day";
    }

    @PostMapping("day/delete/{selectedDay}")
    public String dayPost(@PathVariable("selectedDay")Date selectedDay){

        List<WorkoutModel> workoutsOnDay = workoutRepository.findAllByDayModelAndUserModel(dayRepository.findByDateNameAndUserModels(selectedDay, userService.getUserModel()).get(), userService.getUserModel());
        for (int i = 0; i < workoutsOnDay.size(); i++) {
            trainingRepository.deleteAllByWorkoutId(workoutsOnDay.get(i).getId());
        }
        return "redirect:/training/" + selectedDay;
    }
}
