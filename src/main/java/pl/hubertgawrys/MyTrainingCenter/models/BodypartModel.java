package pl.hubertgawrys.MyTrainingCenter.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "bodypart")
public class BodypartModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "part")
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    UserModel userModel;

}
