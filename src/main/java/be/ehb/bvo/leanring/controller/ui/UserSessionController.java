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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;

@Controller
@RequestMapping(path="ui/user/session")
public class UserSessionController {

    private static final Logger logger = LoggerFactory.getLogger(UserSessionController.class);

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
            logger.error("Not logged in...");
        }
        return "questionsession";
    }

    @PostMapping("{id}/question")
    public ModelAndView question(@PathVariable Integer id, @ModelAttribute QuestionForm newserie, Model model) {
        ListQuestionInterface lqi = new ListQuestionInterface();

        QuestionSession session = sessionRepository.findById(id).get();
        QuestionFeedback feedback = session.validateQuestion(newserie);
        logger.info("Feedback: " + feedback.isValid());
        logger.info(feedback.getFeedback());

        if(session.hasEnded()) {
            logger.info("Session ended...");
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
