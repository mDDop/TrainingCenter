package pl.hubertgawrys.MyTrainingCenter.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.hubertgawrys.MyTrainingCenter.models.forms.ExerciseForm;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name="exercise")
@Data
@NoArgsConstructor
public class ExerciseModel implements Comparable<ExerciseModel>{

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "name_group")
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    UserModel userModel;


    @Override
    public int compareTo(ExerciseModel o) {
        if (id==o.getId()){
            return 0;
        } return Integer.valueOf(id).compareTo(Integer.valueOf(o.getId()));
    }
}
