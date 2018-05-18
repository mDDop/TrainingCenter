package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.hubertgawrys.MyTrainingCenter.models.DayModel;
import pl.hubertgawrys.MyTrainingCenter.models.ResultModel;
import pl.hubertgawrys.MyTrainingCenter.models.WorkoutModel;
import pl.hubertgawrys.MyTrainingCenter.models.WorkModel;
import pl.hubertgawrys.MyTrainingCenter.models.services.WorkService;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.DayRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.TrainingRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.WorkoutRepository;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@Controller
public class DayController {

    final DayRepository dayRepository;

    final TrainingRepository trainingRepository;

    final UserService userService;

    final WorkoutRepository workoutRepository;

    final WorkService workService;

    @Autowired
    public DayController(TrainingRepository trainingRepository, DayRepository dayRepository, UserService userService, WorkoutRepository workoutRepository, WorkService workService) {
        this.trainingRepository = trainingRepository;
        this.dayRepository = dayRepository;
        this.userService = userService;
        this.workoutRepository = workoutRepository;
        this.workService = workService;
    }

    @ModelAttribute
    public Model addModel(Model model){
        model.addAttribute("username", userService.getUserModel().getUsername());
        return model;
    }

    @GetMapping("/day")
    public String dayGet(Model model){
        List<DayModel> daysUser = dayRepository.findAllByUserModelsEquals(userService.getUserModel());
        List<ResultModel> workOnDay =  workService.calculateWorkDay();
        model.addAttribute("days", daysUser);
        model.addAttribute("workOnDays", workOnDay);

        return "day";
    }

    @PostMapping("/day")
    public String deyAdd(@RequestParam("newDate") String date){
        DayModel newDayModel = new DayModel();
        newDayModel.setDateName(Date.valueOf(date));
        newDayModel.setUserModels(userService.getUserModel());
        dayRepository.save(newDayModel);
        return "redirect:/day";
    }

    @Transactional
    @GetMapping("day/delete/{selectedDay}")
    public String dayPost(@PathVariable("selectedDay")Date selectedDay){

        List<WorkoutModel> workoutsOnDay = workoutRepository.findAllByDayModelAndUserModel(dayRepository.findByDateNameAndUserModels(selectedDay, userService.getUserModel()).get(), userService.getUserModel());
        for (int i = 0; i < workoutsOnDay.size(); i++) {
            trainingRepository.deleteAllByWorkoutModel(workoutsOnDay.get(i));
            workoutRepository.delete(workoutsOnDay.get(i));
        }
        dayRepository.delete(dayRepository.findByDateNameAndUserModels(selectedDay, userService.getUserModel()).get());
        return "redirect:/day";
    }
}
