package be.ehb.bvo.leanring.util;

import be.ehb.bvo.leanring.algo.QuestionInterface;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScannerQuestionInterface implements QuestionInterface {
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
        for (int i = 0; i < size; i++) {
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