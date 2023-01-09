package be.ehb.bvo.leanring.controller.ui;

import be.ehb.bvo.leanring.algo.QuestionInterface;

import java.util.List;

public class ListQuestionInterface implements QuestionInterface
    {

        private String question;

        public String getQuestion() {
            return question;
        }

        public int getSize() {
            return size;
        }

        private int size;



        @Override
        public List<String> askList(String question, int size) {
            this.question = question;
            this.size = size;
            return null;
        }

        @Override
        public List getListOfAnswers() {
            return null;
        }
    }