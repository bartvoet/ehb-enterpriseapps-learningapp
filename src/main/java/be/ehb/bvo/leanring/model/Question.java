package be.ehb.bvo.leanring.model;

import be.ehb.bvo.leanring.algo.QuestionFeedback;
import be.ehb.bvo.leanring.algo.QuestionInterface;

import java.util.List;

public abstract class Question {
    public abstract void ask(QuestionInterface qi);

    public abstract QuestionFeedback validate(QuestionInterface qi);

    public abstract QuestionFeedback askAndValidate(QuestionInterface qi);

}
