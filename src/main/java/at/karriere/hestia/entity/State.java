package at.karriere.hestia.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class State {

    private double meanSum;
    private int meanCount;
    private Queue<String> bufferKeys;
    private String pattern;
    private Long cursor;
    private String keys;
    private Connection connection;
    private Integer db;
    private String cookie;

    public State() {
        bufferKeys = new LinkedBlockingQueue<>();
        pattern = "";
        cursor = 0L;
    }

    public void addMean(double meanSum) {
        this.meanSum += meanSum;
        this.meanCount++;
    }

    public double getMean() {
        if(meanCount == 0)
            return 0;
        return meanSum / meanCount;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Long getCursor() {
        return cursor;
    }

    public void setCursor(Long cursor) {
        this.cursor = cursor;
    }

    public void clearQueue() {
        bufferKeys.clear();
    }

    public void addToQueue(String[] keys) {
        bufferKeys.addAll(Arrays.asList(keys));
    }

    public String getFromQueue(Long count) {
        String sum = "";
        if(getSizeOfQueue() > 0) {
            sum = bufferKeys.poll();
        } else {
            return "";
        }
        for (int i = 1; i < count; i++) {
            String head = bufferKeys.poll();
            if(head != null) {
                sum += "\n" + head;
            } else {
                return sum;
            }
        }
        return sum;
    }

    public int getSizeOfQueue() {
        return bufferKeys.size();
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public void setConnection(String host, Integer port, Integer db) {
        connection = new Connection(host, port);
        this.db = db;
    }

    public boolean isSameConnection(String host, Integer port, Integer db) {
        if(host == null && port == null && this.connection == null)
            return true;
        if((host == null || port == null) && this.connection != null)
            return false;
        if(this.db == null && db == null)
            return true;
        if(connection == null)
            return false;
        return connection.getHostname().equals(host) && connection.getPort().equals(port) && this.db.equals(db);
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}

