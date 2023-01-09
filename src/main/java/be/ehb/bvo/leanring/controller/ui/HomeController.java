package be.ehb.bvo.leanring.controller.ui;

import be.ehb.bvo.leanring.model.User;
import be.ehb.bvo.leanring.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserRepository repository;

    @GetMapping(value="/")
    @RequestMapping(value="/",method= RequestMethod.GET)
    public String hello(Model model, Principal principal){
        if(principal != null) {
            User user = repository.findByName(principal.getName());
            if(user != null) {
                model.addAttribute("series", user.getQuestions());
            }
        } else {
            System.out.println("Not logged in...");
        }

        return "home";
    }
}