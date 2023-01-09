package be.ehb.bvo.leanring.model;

import be.ehb.bvo.leanring.algo.QuestionFeedback;
import be.ehb.bvo.leanring.algo.QuestionInterface;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuestionSessionTest {

    private static class ScannerQuestionInterface implements QuestionInterface
    {
        private Scanner scanner;
        private List<String> test = new ArrayList<>();

        public ScannerQuestionInterface(Scanner scanner) {
            this.scanner = scanner;
        }

        public ScannerQuestionInterface(InputStream in) {
            this(new Scanner(in));
        }

        public ScannerQuestionInterface() {
            this(System.in);
        }

        @Override
        public List<String> askList(String question, int size) {
            System.out.println(question);

            test.clear();
            String nextOne = "";
            for(int i = 0;i < size;i++) {
                System.out.println(">");
                nextOne = scanner.nextLine();
                test.add((nextOne));
            }

            return test;
        }

        @Override
        public List getListOfAnswers() {
            return test;
        }
    }

    public static void main(String[] args) {
        QuestionSession session = new QuestionSession()
                .addQuestion(new ListQuestion("Naam?").withAnswers("Bart", "Voet"))
                .addQuestion(new ListQuestion("Leeftijd").withAnswers("48"))
                .startSession();

        ScannerQuestionInterface qi = new ScannerQuestionInterface();

        while(!session.hasEnded()) {
            QuestionFeedback feecback = session.askAndValidate(qi);
            if(feecback.isValid()) {
                System.out.println("OK");
            } else {
                System.out.println("NOK");
            }
        }
    }
}
