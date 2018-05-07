package pl.hubertgawrys.MyTrainingCenter.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import pl.hubertgawrys.MyTrainingCenter.models.*;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.BodypartRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.DayRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.TrainingRepository;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.WorkoutRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WorkService {

    final TrainingRepository trainingRepository;
    final UserService userService;
    final DayRepository dayRepository;
    final WorkoutRepository workoutRepository;
    final WorkService workService;
    final BodypartRepository bodypartRepository;

    @Autowired
    public WorkService(TrainingRepository trainingRepository, UserService userService, DayRepository dayRepository, WorkoutRepository workoutRepository, WorkService workService, BodypartRepository bodypartRepository) {
        this.trainingRepository = trainingRepository;
        this.userService = userService;
        this.dayRepository = dayRepository;
        this.workoutRepository = workoutRepository;
        this.workService = workService;
        this.bodypartRepository = bodypartRepository;
    }

    public List<ResultModel> calculateWorkDay(){
        List<DayModel> daysUser = dayRepository.findAllByUserModelsEquals(userService.getUserModel());
        List<TrainingModel> trainingJoinAll = trainingRepository.findJoin();
        List<ResultModel> resultOnDay = new ArrayList<>();
        for (DayModel day: daysUser) {
            ResultModel resultModel = new ResultModel();
            resultModel.setT(day);
            resultModel.setResult(0);
            resultOnDay.add(resultModel);
        }
        for (TrainingModel trainingModel : trainingJoinAll) {
            for (ResultModel resultModel : resultOnDay) {
                if (resultModel.getT().equals(trainingModel.getWorkoutModel().getDayModel())) {
                    resultModel.setResult(resultModel.getResult() + trainingModel.getKilos() * trainingModel.getReps());
                }
            }
        }
        return resultOnDay;
    }
    public List<ResultModel> calculateWorkWorkout(){
        List<WorkoutModel> workoutsUser = workoutRepository.findAllByUserModel(userService.getUserModel());
        List<TrainingModel> trainingJoinAll = trainingRepository.findTrainingForUser(userService.getUserModel().getId());
        List<ResultModel> resultOnWorkout = new ArrayList<>();
        for (WorkoutModel workout: workoutsUser) {
            ResultModel resultModel = new ResultModel();
            resultModel.setT(workout);
            resultModel.setResult(0);
            resultOnWorkout.add(resultModel);
        }
        for (TrainingModel trainingModel : trainingJoinAll) {
            for (ResultModel resultModel : resultOnWorkout) {
                if (resultModel.getT().equals(trainingModel.getWorkoutModel())) {
                    resultModel.setResult(resultModel.getResult() + trainingModel.getKilos() * trainingModel.getReps());
                }
            }
        }

        return resultOnWorkout;
    }
    public Map<DayModel, Integer> calculateWorkBodyPart(int bodypartId){
        List<TrainingModel> trainingJoinAll =
                trainingRepository.findByWorkoutModel_ExerciseModel_BodypartModel_IdAndWorkoutModel_UserModel(
                        bodypartId, userService.getUserModel());
        Map<DayModel, Integer> days = new HashMap<>();
        for (TrainingModel trainingModel : trainingJoinAll) {
            DayModel trainingDay = trainingModel.getWorkoutModel().getDayModel();
            if (days.containsKey(trainingDay)){
                days.put(trainingDay,days.get(trainingDay)+trainingModel.getReps()*trainingModel.getKilos());
            } else {
                days.put(trainingDay, trainingModel.getReps()*trainingModel.getKilos());
            }
        }

        return days;
    }


    public List<WorkModel> calculateWorkTraining(){
        List<TrainingModel> trainingJoinAll = trainingRepository.findJoin();
        List<WorkModel> workOnTraining = new ArrayList<>();
        for (TrainingModel trainingModel : trainingJoinAll){
            WorkModel workModel = new WorkModel();
            workModel.setId(trainingModel.getId());
            workModel.setWork(trainingModel.getReps()*trainingModel.getKilos());
            workOnTraining.add(workModel);
        }
        return workOnTraining;
    }

    public List<ResultModel> calculateWorkWorkoutAndExerciseId(){
        List<WorkoutModel> workoutsUser = workoutRepository.findAllByUserModel(userService.getUserModel());
        List<TrainingModel> trainingJoinAll = trainingRepository.findJoin();
        List<ResultModel> resultOnWorkout = new ArrayList<>();
        for (WorkoutModel workout: workoutsUser) {
            ResultModel resultModel = new ResultModel();
            resultModel.setT(workout.getExerciseModel().getId());
            resultModel.setResult(0);
            resultOnWorkout.add(resultModel);
        }
        for (TrainingModel trainingModel : trainingJoinAll) {
            for (ResultModel resultModel : resultOnWorkout) {
                if (resultModel.getT().equals(trainingModel.getWorkoutModel())) {
                    resultModel.setResult(resultModel.getResult() + trainingModel.getKilos() * trainingModel.getReps());
                }
            }
        }

        return resultOnWorkout;
    }
}
