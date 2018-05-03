package pl.hubertgawrys.MyTrainingCenter.models.forms;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class LoginForm {

    private String username;
    @NotEmpty
    private String password;
}
