package pl.hubertgawrys.MyTrainingCenter.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import pl.hubertgawrys.MyTrainingCenter.models.forms.RegisterForm;

import javax.persistence.*;

@Entity
@Table(name="user")
@Data
@NoArgsConstructor
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private String password;
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(name="account_type")
    private UserType accountType;

    public UserModel(RegisterForm registerForm){
        this.username = registerForm.getUsername();
        this.password = Utils.hash256SHA(registerForm.getPassword());
        this.email = registerForm.getEmail();
        this.accountType = UserType.USER;
    }
}
