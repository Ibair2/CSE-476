package edu.msu.bhushanj.exambhushanj;

/**
 * Created by jaiwant on 11/16/2016.
 */
public class Values {

    private String whatType;

    private int totalScore;

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    private String reset;

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getWhatType() {
        return whatType;
    }

    public void setWhatType(String whatType) {
        this.whatType = whatType;
    }



    public static Values value = new Values();
}
