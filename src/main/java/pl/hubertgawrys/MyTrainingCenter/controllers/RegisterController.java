package pl.hubertgawrys.MyTrainingCenter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.hubertgawrys.MyTrainingCenter.models.UserModel;
import pl.hubertgawrys.MyTrainingCenter.models.forms.RegisterForm;
import pl.hubertgawrys.MyTrainingCenter.models.repositories.UserRepository;
import pl.hubertgawrys.MyTrainingCenter.models.services.UserService;

import javax.jws.soap.SOAPBinding;
import javax.validation.Valid;

@Controller
public class RegisterController {

    final
    UserRepository userRepository;
    UserService userService;

    @Autowired
    public RegisterController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @RequestMapping(value="/register", method=RequestMethod.GET)
    public String registerGet(Model model){
        RegisterForm registerForm = new RegisterForm();
        model.addAttribute("registerForm", registerForm);
        return "register";
    }

    @PostMapping(value="/register")
    public String registerPost(@Valid @ModelAttribute("registerForm") RegisterForm registerForm,
                               BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "register";
        } else {
            UserService.RegisterStatus registrationStatus =  userService.register(registerForm);
            switch (registrationStatus) {
                case BUSY_EMAIL: {
                    model.addAttribute("info", "This email already exists!");
                    return "register";
                }
                case BUSY_LOGIN: {
                    model.addAttribute("info", "This username already exists!");
                    return "register";
                }
                case PASSWORD_MISSMATCH:{
                    model.addAttribute("info", "Password missmatch!");
                    return "register";
                }
            }
        }
        return "redirect:/login";
    }
}
