package pl.hubertgawrys.MyTrainingCenter.models.services;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import pl.hubertgawrys.MyTrainingCenter.models.UserModel;
import pl.hubertgawrys.MyTrainingCenter.models.Utils;
import pl.hubertgawrys.MyTrainingCenter.models.forms.LoginForm;
import pl.hubertgawrys.MyTrainingCenter.models.forms.RegisterForm;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.UserRepository;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService {

    public enum LoginStatus{
        ACCEPTED, WRONG_DATA
    }

    public enum RegisterStatus{
        ACCEPTED, BUSY_LOGIN, BUSY_EMAIL, PASSWORD_MISSMATCH
    }

    final
    UserRepository userRepository;
    UserService userService;

    @Autowired
    public UserService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Getter @Setter
    private boolean isLogged;

    @Getter @Setter
    private UserModel userModel;

    public RegisterStatus register(RegisterForm registerForm){
        if (userRepository.existsUserModelByUsername(registerForm.getUsername())){
            return RegisterStatus.BUSY_LOGIN;
        } else if (userRepository.existsUserModelByEmail(registerForm.getEmail())) {
            return RegisterStatus.BUSY_EMAIL;
        } else if (!registerForm.getPassword().equals(registerForm.getPasswordRepeat())){
            return RegisterStatus.PASSWORD_MISSMATCH;
        } else  {
            UserModel newUserModel = new UserModel(registerForm);
            System.out.println(newUserModel);
            userRepository.save(newUserModel);
            return RegisterStatus.ACCEPTED;
        }
    }

    public LoginStatus login(LoginForm loginForm){
        if(userRepository.existsUserModelByUsernameAndPassword(loginForm.getUsername(), Utils.hash256SHA(loginForm.getPassword()))){
            userService.setLogged(true);
            userService.setUserModel(userRepository.findUserModelByUsernameAndPassword(loginForm.getUsername(),
                    Utils.hash256SHA(loginForm.getPassword())));
            return LoginStatus.ACCEPTED;
        }
        return LoginStatus.WRONG_DATA;
    }
}
