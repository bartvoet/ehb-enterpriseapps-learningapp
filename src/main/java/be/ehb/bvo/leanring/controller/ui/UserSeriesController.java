package be.ehb.bvo.leanring.controller.ui;

import be.ehb.bvo.leanring.model.ListQuestion;
import be.ehb.bvo.leanring.model.QuestionForm;
import be.ehb.bvo.leanring.model.QuestionSeries;
import be.ehb.bvo.leanring.model.User;
import be.ehb.bvo.leanring.repo.QuestionRepository;
import be.ehb.bvo.leanring.repo.QuestionSeriesRepository;
import be.ehb.bvo.leanring.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
@RequestMapping(path="ui/user/series")
public class UserSeriesController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionSeriesRepository seriesRepository;

    @GetMapping("/addserie")
    public String createNewSerie(Model model) {
        model.addAttribute("newserie", new QuestionSeries());
        return "createserie";
    }

    @PostMapping("/addserie")
    public RedirectView createNewSerieSubmit(@ModelAttribute QuestionSeries newserie, Model model, Principal principal) {
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

    @GetMapping("/questionseriedetails/{id}")
    public String editExistingSerie(Model model, @PathVariable Integer id) {
        QuestionSeries series = seriesRepository.findById(id).orElseThrow(() -> new RuntimeException("id " + id +  " not found"));
        model.addAttribute("currentseries", series);
        model.addAttribute("questions", series.getQuestions());
        model.addAttribute("newquestion", new QuestionForm());
        return "questionseriedetails";
    }

    @PostMapping("/addquestion/{id}")
    public String adQuestion(@ModelAttribute QuestionForm question, Model model, @PathVariable Integer id) {
        QuestionSeries series = seriesRepository.findById(id).orElseThrow(() -> new RuntimeException("id " + id +  " not found"));
        model.addAttribute("currentseries", series);
        model.addAttribute("questions", series.getQuestions());
        model.addAttribute("newquestion", new QuestionForm());
        ListQuestion newQuestion = new ListQuestion(question.getQuestion(), question.answersAsList());
        series.addQuestion(newQuestion);
        seriesRepository.save(series);
        return "questionseriedetails";
    }

}
