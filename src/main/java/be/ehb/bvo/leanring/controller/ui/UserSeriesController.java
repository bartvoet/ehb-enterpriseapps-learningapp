package be.ehb.bvo.leanring.controller.ui;

import be.ehb.bvo.leanring.model.QuestionSeries;
import be.ehb.bvo.leanring.model.User;
import be.ehb.bvo.leanring.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
@RequestMapping(path="ui/user/series")
public class UserSeriesController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/addserie")
    public String greetingForm(Model model) {
        model.addAttribute("newserie", new QuestionSeries());
        return "createserie";
    }

    @PostMapping("/addserie")
    public RedirectView greetingSubmit(@ModelAttribute QuestionSeries newserie, Model model, Principal principal) {
        if(principal != null) {
            User user = userRepository.findByName(principal.getName());
            if(user != null) {
                user.addSeries(newserie);
                userRepository.save(user);
            }
        } else {
            System.out.println("Not logged in...");
        }
        return new RedirectView("/");
    }

}
