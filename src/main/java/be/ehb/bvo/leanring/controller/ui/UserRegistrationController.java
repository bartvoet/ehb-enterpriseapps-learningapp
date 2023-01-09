package be.ehb.bvo.leanring.controller.ui;

import be.ehb.bvo.leanring.model.User;
import be.ehb.bvo.leanring.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="ui/user")
public class UserRegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String showUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "home";
    }


    @GetMapping("/create")
    public String greetingForm(Model model) {
        model.addAttribute("newuser", new User());
        return "create";
    }

    @PostMapping("/create")
    public String greetingSubmit(@ModelAttribute User newuser, Model model) {
        userRepository.save(newuser);
        model.addAttribute("newuser", newuser);
        return "confirm";
    }

}
