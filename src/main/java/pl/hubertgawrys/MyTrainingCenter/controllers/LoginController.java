package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.hubertgawrys.MyTrainingCenter.models.UserModel;
import pl.hubertgawrys.MyTrainingCenter.models.forms.LoginForm;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.UserRepository;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LoginController {

    final
    UserRepository userRepository;

    final
    UserService userService;

    @Autowired
    public LoginController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginGet(Model model){
        LoginForm loginForm = new LoginForm();
        model.addAttribute("loginForm", loginForm);
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                            BindingResult bindingResult,
                            Model model){
        UserService.LoginStatus loginStatus = userService.login(loginForm);
        switch (loginStatus) {
            case WRONG_DATA: {
                model.addAttribute("badLoginInfo", "Wrong login or password! Try again!");
                return "/login";
            }
        }
        return "redirect:/";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }


}
