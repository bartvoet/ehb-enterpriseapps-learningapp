package be.ehb.bvo.leanring.algo;

public class QuestionFeedback {

    private boolean valid;
    private String feedback;

    public QuestionFeedback(boolean valid, String feedback) {
        this.valid = valid;
        this.feedback = feedback;
    }

    public QuestionFeedback(boolean valid) {
        this(valid,"");
    }

    public boolean isValid() {
        return valid;
    }

    public String getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return "QuestionFeedback{" +
                "valid=" + valid +
                ", feedback='" + feedback + '\'' +
                '}';
    }

}
