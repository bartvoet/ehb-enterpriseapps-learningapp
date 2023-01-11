package be.ehb.bvo.leanring.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class QuestionSeries {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestions(Set<ListQuestion> questions) {
        this.questions = questions;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String name;
    @OneToMany(cascade = CascadeType.MERGE)
    private Set<ListQuestion> questions;

    public QuestionSeries() {
        this("");
    }

    public QuestionSeries(String name) {
        this.name = name;
        questions = new LinkedHashSet<ListQuestion>();
    }

    public QuestionSeries addQuestion(ListQuestion question) {
        this.questions.add(question);
        return this;
    }

    public Set<ListQuestion> getQuestions() {
        return this.questions;
    }

    public void removeQuestion(ListQuestion question) {
        this.questions.remove(question);
    }
}
