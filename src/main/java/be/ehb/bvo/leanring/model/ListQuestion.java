package be.ehb.bvo.leanring.model;

import be.ehb.bvo.leanring.algo.QuestionFeedback;
import be.ehb.bvo.leanring.algo.QuestionInterface;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class ListQuestion extends Question {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String question;
    private List<String> answers;

    public ListQuestion() {
            this.answers = new ArrayList<String>();
    }
    public ListQuestion(String question) {
        this();
        this.question = question;
    }

    public ListQuestion(String question, List<String> answers) {
        this();
        this.question = question;
        this.answers = answers;
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
        return new QuestionFeedback(this.answers.equals(answers),this.answers.toString());
    }

    public QuestionFeedback askAndValidate(QuestionInterface qi) {
        this.ask(qi);
        return validate(qi);
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "ListQuestion{" +
                "question='" + question + '\'' +
                ", answers=" + answers +
                '}';
    }

}
