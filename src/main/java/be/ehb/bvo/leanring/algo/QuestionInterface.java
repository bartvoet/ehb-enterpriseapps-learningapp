package be.ehb.bvo.leanring.algo;

import java.util.List;

public interface QuestionInterface {
    List<String> askList(String question, int size);

    List getListOfAnswers();

}
