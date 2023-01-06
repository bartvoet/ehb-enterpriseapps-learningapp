package be.ehb.bvo.leanring.model;

import be.ehb.bvo.leanring.algo.QuestionFeedback;
import be.ehb.bvo.leanring.algo.QuestionInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListQuestion extends Question {
    private String question;
    private List<String> answers;

    public ListQuestion() {
            this.answers = new ArrayList<String>();
    }
    public ListQuestion(String question) {
        this();
        this.question = question;
    }

    public ListQuestion withAnswers(String... answers) {
        List<String> listOfAnswers = Arrays.asList(answers);
        this.answers.addAll(listOfAnswers);
        return this;
    }

    public void ask(QuestionInterface qi) {
        qi.askList(this.question, this.answers.size());
    }



    public QuestionFeedback validate(QuestionInterface qi) {
        List answers = qi.getListOfAnswers();
        return new QuestionFeedback(this.answers.equals(answers));
    }

    public QuestionFeedback askAndValidate(QuestionInterface qi) {
        this.ask(qi);
        return validate(qi);
    }

}
