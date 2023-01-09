package be.ehb.bvo.leanring.model;


import be.ehb.bvo.leanring.algo.QuestionFeedback;
import be.ehb.bvo.leanring.algo.QuestionInterface;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListQuestionTest {

    //Intermediate state of answer

    private class ListQuestionInterface implements QuestionInterface {
        private List<String> answers;
        private List<String> questions;

        ListQuestionInterface() {

        }

        public ListQuestionInterface withQuestions(String... questions) {
            this.questions = Arrays.asList(questions);
            return this;
        }

        public ListQuestionInterface withAnswers(String... answers) {
            this.answers = Arrays.asList(answers);
            return this;
        }

        @Override
        public List<String> askList(String question, int size) {
            return answers;
        }

        @Override
        public List<String> getListOfAnswers() {
            return answers;
        }


    }

    @Test
    public void myFirstTest() {
        ListQuestion question = new ListQuestion("Countries in Benenlux");
        question.withAnswers("Belgium","Netherlands", "Luxemburg");
        ListQuestionInterface qi = new ListQuestionInterface()
                .withAnswers("Belgium","Netherlands", "Luxemburg")
                .withQuestions("Belgium","Netherlands", "Luxemburg");
        QuestionFeedback feedback = question.askAndValidate(qi);
        assertTrue(feedback.isValid());

//        question.askQuestion(new WuestionInterface(){
//           public void
//        });

    }


    @Test
    public void mySecondTest() {
        ListQuestion question = new ListQuestion("Countries in Benenlux");
        question.withAnswers("Belgium","Netherlands", "Luxemburg");
        ListQuestionInterface qi = new ListQuestionInterface()
                .withAnswers("Belgium","Netherland", "Luxemburg")
                .withQuestions("Belgium","Netherlands", "Luxemburg");
        QuestionFeedback feedback = question.askAndValidate(qi);
        assertTrue(!feedback.isValid());

//        question.askQuestion(new WuestionInterface(){
//           public void
//        });

    }
}
