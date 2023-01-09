package be.ehb.bvo.leanring.controller.ui;

import be.ehb.bvo.leanring.algo.QuestionFeedback;
import be.ehb.bvo.leanring.model.*;
import be.ehb.bvo.leanring.repo.QuestionSeriesRepository;
import be.ehb.bvo.leanring.repo.QuestionSessionRepository;
import be.ehb.bvo.leanring.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
@RequestMapping(path="ui/user/session")
public class UserSessionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionSeriesRepository seriesRepository;

    @Autowired
    private QuestionSessionRepository sessionRepository;

    @GetMapping("/startsession")
    public String startNewSession(@RequestParam Integer seriesId, Model model, Principal principal) {
        ListQuestionInterface lqi = new ListQuestionInterface();

        if(principal != null) {
            User user = userRepository.findByName(principal.getName());
            QuestionSeries series = seriesRepository.findById(seriesId).get();
            if(user != null && series != null) {
                QuestionSession session = user.addSession(series);
                sessionRepository.save(session);
                session.startSession();
                session.askNextQuestion(lqi);
                model.addAttribute("questionandsize",lqi);
                QuestionForm form = new QuestionForm();
                form.setQuestion(lqi.getQuestion());
                model.addAttribute("questionandanswer",form);
                userRepository.save(user);
                model.addAttribute("qsession",session);
            }
        } else {
            System.out.println("Not logged in...");
        }
        return "questionsession";
    }

    @PostMapping("{id}/question")
    public ModelAndView question(@PathVariable Integer id, @ModelAttribute QuestionForm newserie, Model model) {
        ListQuestionInterface lqi = new ListQuestionInterface();

        QuestionSession session = sessionRepository.findById(id).get();
        QuestionFeedback feedback = session.validateQuestion(newserie);
        System.out.println(feedback.isValid());
        System.out.println(feedback.getFeedback());

        if(session.hasEnded()) {
            System.out.println("Session ended...");
            return new ModelAndView(new RedirectView("/"));
        } else {
            session.askNextQuestion(lqi);
            model.addAttribute("questionandsize", lqi);
            QuestionForm form = new QuestionForm();
            form.setQuestion(lqi.getQuestion());
            model.addAttribute("questionandanswer", form);
            sessionRepository.save(session);
            model.addAttribute("qsession", session);
            return new ModelAndView("questionsession");
        }
    }

}
