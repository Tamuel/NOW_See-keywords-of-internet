package com.softart.shift.now;

import android.graphics.Point;
import android.support.annotation.ColorInt;
import android.util.Log;

/**
 * Created by DongKyu on 2016-06-01.
 */
public class KeywordCircle {
    private Keyword keyword;

    private int radius;
    private Point position;

    @ColorInt private int fillColor;

    public KeywordCircle(Keyword keyword, int radius, Point position, int fillColor) {
        this.keyword = keyword;
        this.radius = radius;
        this.position = position;
        this.fillColor = fillColor;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

}
