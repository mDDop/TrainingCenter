package pl.hubertgawrys.MyTrainingCenter.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "day")
@Data
@NoArgsConstructor
public class DayModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "day_name")
    private Date dateName;

    @OneToOne
    @JoinColumn(name = "user_id")
    UserModel userModels;
}
