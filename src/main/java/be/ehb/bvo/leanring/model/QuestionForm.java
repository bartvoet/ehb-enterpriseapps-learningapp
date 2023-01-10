package be.ehb.bvo.leanring.model;

import be.ehb.bvo.leanring.algo.QuestionInterface;

import java.util.*;

public class QuestionForm implements QuestionInterface {

    public QuestionForm() {
        this.answerList = new ArrayList<>();
    }

    public QuestionForm(int number) {
        this();
        for(int i=0;i < number;i++) {
            this.answerList.add("");
        }

    }


    private String question;
    private String answers;
    private String token = ";";
    private List<String> answerList = new ArrayList<>();

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

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answers) {
        this.answerList = answers;
    }


    public List<String> answersAsList() {
        String[] parts=answers.split(token);
        return Arrays.asList(parts);
    }


    @Override
    public List<String> askList(String question, int size) {
        return null;
    }

    @Override
    public List getListOfAnswers() {
        return this.answerList;
        //return this.answersAsList();
    }
}
