package be.ehb.bvo.leanring.model;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class QuestionForm {

    private String question;
    private String answers;
    private String token = ";";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public List<String> answersAsList() {
        String[] parts=answers.split(token);
        return Arrays.asList(parts);
    }



}
