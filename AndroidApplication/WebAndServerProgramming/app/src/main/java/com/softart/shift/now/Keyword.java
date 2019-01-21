package com.softart.shift.now;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DongKyu on 2016-06-03.
 */
public class Keyword {
    private String word;
    private int numberOfKeyword;
    private int score;

    private Map<String, Integer> relatedWords;

    public Keyword(String keyword) {
        this.word = keyword;
        relatedWords = new HashMap<String, Integer>();
    }

    public void addRelatedWord(String word, int numberOfWord) {
        relatedWords.put(word, numberOfWord);
    }

    public String toString() {
        String temp = "";
        temp += word + " " + numberOfKeyword + " " + score + "\n";
        for(String key : relatedWords.keySet()) {
            temp += "\t" + key + " " + relatedWords.get(key) + "\n";
        }

        return temp;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNumberOfKeyword() {
        return numberOfKeyword;
    }

    public void setNumberOfKeyword(int numberOfKeyword) {
        this.numberOfKeyword = numberOfKeyword;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRelatedRanking(String keyword) {
        if(relatedWords.containsKey(keyword))
            return relatedWords.get(keyword);
        else
            return -1;
    }

    public boolean isRelated(String keyword) {
        if(relatedWords.containsKey(keyword))
            return true;
        else
            return false;
    }
}
