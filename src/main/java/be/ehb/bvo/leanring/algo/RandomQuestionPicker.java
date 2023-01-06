package be.ehb.bvo.leanring.algo;

import java.util.Random;

public class RandomQuestionPicker implements QuestionPicker {

    private Random random = new Random();

    @Override
    public int pickQuestion(int fromTotal) {
        return random.nextInt(fromTotal);
    }
}
