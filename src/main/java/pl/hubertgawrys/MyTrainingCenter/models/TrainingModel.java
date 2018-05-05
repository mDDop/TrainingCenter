package pl.hubertgawrys.MyTrainingCenter.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;


@Entity
@Table(name="training")
@Data
@NoArgsConstructor
public class TrainingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "workout_id")
    private WorkoutModel workoutModel;

    private int kilos;
    private int reps;

    @Column(name = "time_current")
    private LocalDateTime currentTime;

}
