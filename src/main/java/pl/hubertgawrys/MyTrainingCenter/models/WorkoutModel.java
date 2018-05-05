package pl.hubertgawrys.MyTrainingCenter.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "workout")
@Data
@NoArgsConstructor
public class WorkoutModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne
    @JoinColumn(name = "date_id")
    private DayModel dayModel;

    @OneToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseModel exerciseModel;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserModel userModel;
}
