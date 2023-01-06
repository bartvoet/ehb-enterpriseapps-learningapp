package be.ehb.bvo.leanring.model;

import java.util.*;

public class QuestionSeries {

    private String name;
    private Set<? extends Question> questions;

    public QuestionSeries(String name) {
        this.name = name;
        questions = new LinkedHashSet<Question>();
    }

    public QuestionSeries addQuestion(Question question) {
        return this;
    }

    public Collection<? extends Question> getQuestions() {
        return this.questions;
    }
}
