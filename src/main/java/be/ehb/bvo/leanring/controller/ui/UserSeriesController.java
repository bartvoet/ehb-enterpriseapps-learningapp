package be.ehb.bvo.leanring.controller.ui;

import be.ehb.bvo.leanring.model.ListQuestion;
import be.ehb.bvo.leanring.model.QuestionForm;
import be.ehb.bvo.leanring.model.QuestionSeries;
import be.ehb.bvo.leanring.model.User;
import be.ehb.bvo.leanring.repo.QuestionRepository;
import be.ehb.bvo.leanring.repo.QuestionSeriesRepository;
import be.ehb.bvo.leanring.repo.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.Arrays;

@Controller
@RequestMapping(path="ui/user/series")
public class UserSeriesController {

    private static final Logger logger = LoggerFactory.getLogger(UserSeriesController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionSeriesRepository seriesRepository;

    @Autowired
    private QuestionRepository questionRepository;

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
                seriesRepository.save(newserie);
                userRepository.save(user);
            }
        } else {
            logger.error("Not logged in...");
        }
        return new RedirectView("/ui/user/series/questionseriedetails/" + newserie.getId());
    }

    @GetMapping("/removeserie/{id}")
    public RedirectView removeSerie(Model model, @PathVariable Integer id, Principal principal) {
        if(principal != null) {
            User user = userRepository.findByName(principal.getName());
            if(user != null) {
                QuestionSeries series = seriesRepository.findById(id).orElseThrow(() -> new RuntimeException("id " + id +  " not found"));
                user.removeSerie(series);
                userRepository.save(user);
            }
        } else {
            logger.error("Not logged in...");
        }


;
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

    @GetMapping("/removeallquetionsfromserie/{id}")
    public String removeAllQuestions(Model model, @PathVariable Integer id) {
        QuestionSeries series = seriesRepository.findById(id).orElseThrow(() -> new RuntimeException("id " + id +  " not found"));
        series.removeAllQuestions();
        seriesRepository.save(series);

        model.addAttribute("currentseries", series);
        model.addAttribute("questions", series.getQuestions());
        model.addAttribute("newquestion", new QuestionForm());

        return "questionseriedetails";
    }

    @GetMapping("/removequestion/{id}")
    public String removeQuestionFromSerie(Model model, @PathVariable Integer id, @RequestParam Integer qid) {
        QuestionSeries series = seriesRepository.findById(id).orElseThrow(() -> new RuntimeException("id " + id +  " not found"));
        ListQuestion question = questionRepository.findById(qid).orElseThrow(() -> new RuntimeException("id " + id +  " not found"));

        series.removeQuestion(question);
        seriesRepository.save(series);

        model.addAttribute("currentseries", series);
        model.addAttribute("questions", series.getQuestions());
        model.addAttribute("newquestion", new QuestionForm());
        return "questionseriedetails";
    }

    @PostMapping("/addquestion/{id}")
    public String addQuestion(@ModelAttribute QuestionForm question, Model model, @PathVariable Integer id) {
        QuestionSeries series = seriesRepository.findById(id).orElseThrow(() -> new RuntimeException("id " + id +  " not found"));
        model.addAttribute("currentseries", series);
        model.addAttribute("questions", series.getQuestions());
        model.addAttribute("newquestion", new QuestionForm());
        ListQuestion newQuestion = new ListQuestion(question.getQuestion(), question.answersAsList());
        series.addQuestion(newQuestion);
        seriesRepository.save(series);
        return "questionseriedetails";
    }

    @PostMapping("/addquestionscsv/{id}")
    public String uploadCSV(@PathVariable Integer id, Model model, @RequestParam("file") MultipartFile file)
    {
        QuestionSeries series = seriesRepository.findById(id).orElseThrow(() -> new RuntimeException("id " + id +  " not found"));
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            while(reader.ready()) {
                String line = reader.readLine();
                String[] tokens = line.split(";");
                if(tokens.length >= 2 ) {
                    ListQuestion question = new ListQuestion(tokens[0])
                                                .withAnswers(Arrays.copyOfRange(
                                                        tokens, 1, tokens.length));
                    series.addQuestion(question);
                }
            }
            seriesRepository.save(series);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("currentseries", series);
        model.addAttribute("questions", series.getQuestions());
        model.addAttribute("newquestion", new QuestionForm());
        return "questionseriedetails";
    }

    @RequestMapping(value = "downloadasCSV/{id}")
    public void downloadCSV(HttpServletResponse response, @PathVariable Integer id) throws IOException {
        String csvFileName = "questions.csv";
        response.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);

        BufferedWriter writer = new BufferedWriter(response.getWriter());

        QuestionSeries series = seriesRepository.findById(id).get();
        writeSeriesAsCsv(writer, series);
        writer.flush();
    }

    private static void writeSeriesAsCsv(BufferedWriter writer, QuestionSeries series) throws IOException {
        for(ListQuestion question : series.getQuestions()) {
            writer.write(question.getQuestion());
            writer.write(";");
            for(String answer : question.getAnswers()) {
                writer.write(answer);
                writer.write(";");
            }
            writer.newLine();
        }
    }

}
