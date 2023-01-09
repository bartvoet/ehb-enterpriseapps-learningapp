package be.ehb.bvo.leanring.model;

import be.ehb.bvo.leanring.algo.QuestionFeedback;
import be.ehb.bvo.leanring.algo.QuestionInterface;
import be.ehb.bvo.leanring.algo.QuestionPicker;
import be.ehb.bvo.leanring.algo.RandomQuestionPicker;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class QuestionSession {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    public Integer getId()
    {
        return id;
    }

    @Transient
    private final QuestionPicker questionPicker;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<ListQuestion> questions = new LinkedHashSet<>();
    @ManyToMany(cascade = CascadeType.ALL)
    private List<ListQuestion> remainingQuestions = new ArrayList<>();

    @ManyToOne
    private ListQuestion currentQuestion = null;

    @ManyToOne
    private ListQuestion previousQuestion = null;

    private boolean started;
    private Date startDate;


    public QuestionSession() {
        this(new RandomQuestionPicker());
    }

    public QuestionSession(QuestionPicker questionPicker) {
        this.questionPicker = questionPicker;
    }


    public QuestionSession addSeries(QuestionSeries series) {
        this.questions.addAll(series.getQuestions());
        return this;
    }

    public QuestionSession addQuestion(ListQuestion question) {
        if(started) {
           throw new IllegalStateException("session has started, no more questions can be added");
        }
        this.questions.add(question);
        return this;
    }

    public QuestionSession startSession() {
        if(!this.started) {
            this.remainingQuestions.addAll(this.questions);
            this.started = true;
            this.startDate = new Date();
        }
        return this;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean hasEnded() {
        return remainingQuestions.isEmpty();
    }

    public void askNextQuestion(QuestionInterface qi) {
        int pick = this.questionPicker.pickQuestion(this.remainingQuestions.size());
        this.currentQuestion = this.remainingQuestions.get(pick);
        this.currentQuestion.ask(qi);
    }

    public QuestionFeedback validateQuestion(QuestionInterface qi) {
        if(currentQuestion == null) {
            throw new IllegalStateException("No question waiting for validation");
        }

        QuestionFeedback state = this.currentQuestion.validate(qi);
        if(state.isValid()) {
            this.remainingQuestions.remove(this.currentQuestion);
        }

        this.previousQuestion = this.currentQuestion; 
        this.currentQuestion = null;
        return state;
    }

    public QuestionFeedback askAndValidate(QuestionInterface qi) {
        this.askNextQuestion(qi);
        return this.validateQuestion(qi);
    }

}
