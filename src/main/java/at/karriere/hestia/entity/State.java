package at.karriere.hestia.entity;

import java.util.Queue;

public class State {

    private double meanSum;
    private int meanCount;
    private Queue<String> bufferKeys;
    private String pattern;

    public State() {
    }

    public void addMean(double meanSum) {
        this.meanSum += meanSum;
        this.meanCount++;
    }

    public double getMean() {
        return meanSum / meanCount;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}

