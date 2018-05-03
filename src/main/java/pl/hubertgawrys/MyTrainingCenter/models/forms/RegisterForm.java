package pl.hubertgawrys.MyTrainingCenter.models.forms;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class RegisterForm {

    @Size(min=6, max=20)
    private String username;
    @Size(min=6, max=30)
    private String password;
    @Size(min=6, max=30)
    private String passwordRepeat;
    @Email
    private String email;

}
