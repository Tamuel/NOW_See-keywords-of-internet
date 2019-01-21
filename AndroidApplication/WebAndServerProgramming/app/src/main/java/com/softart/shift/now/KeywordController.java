package com.softart.shift.now;

import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by DongKyu on 2016-06-17.
 */
public class KeywordController {


    public static void parseKeyword(String data, ArrayList<Keyword> keywords) {
        int i = 0;
        int j = 0;
        for(String temp : data.split("\n")) {
            Log.i("", temp);
            j++;
            if(i % 8 == 0) {
                String t[] = temp.split(" ");
                Keyword k = new Keyword(t[0]);
                k.setNumberOfKeyword(Integer.parseInt(t[1]));
                k.setScore(Integer.parseInt(t[2]));
                keywords.add(k);
            } else {
                temp = temp.replace("\t", "");
                String t[] = temp.split(" ");
                keywords.get(i / 8).addRelatedWord(t[0], Integer.parseInt(t[1]));
            }
            i++;
        }
    }

    public static void makeCircle(ArrayList<Keyword> keywords, ArrayList<KeywordCircle> circles) {
        int radiusLimit = keywords.get(0).getScore();

        int keyLimit = 7;
        for(int i = 0; i < keywords.size(); i++) {
            Keyword word = keywords.get(i);
            KeywordCircle temp;
            if(i < keyLimit) {
                temp = new KeywordCircle(
                        word,                                 // Keyword
                        (int)(((double)word.getScore() / radiusLimit) * 300),                 // radius
                        new Point(
                                (int) (Math.random() * 50) + (i % 3 + 1) * 500 + 500,  // position X
                                (int) (Math.random() * 50) + (i / 3 + 1) * 500 + 500   // position Y
                        ),
                        Color.rgb(
                                (int) (Math.random() * 255),  // R
                                (int) (Math.random() * 255),  // G
                                (int) (Math.random() * 255)   // B
                        )
                );
            } else {
                int j;
                for(j = 0; j < keyLimit; j++) {
                    if(keywords.get(j).isRelated(word.getWord()))
                        break;
                }
                if(j != keyLimit)
                    temp = new KeywordCircle(
                            word,                                 // Keyword
                            (int)(((double)word.getScore() / radiusLimit) * 300),                 // radius
                            new Point(
                                    circles.get(j).getPosition().x + (int)(Math.random() * 20)  - 10,  // position X
                                    circles.get(j).getPosition().y + (int)(Math.random() * 20)  - 10   // position Y
                            ),
                            circles.get(j).getFillColor()
                    );
                else
                    temp = new KeywordCircle(
                            word,                                 // Keyword
                            (int)(((double)word.getScore() / radiusLimit) * 300),                 // radius
                            new Point(
                                    (int) (Math.random() * 1500) + 500,  // position X
                                    (int) (Math.random() * 1500) + 500   // position Y
                            ),
                            Color.rgb(
                                    (int) (Math.random() * 255),  // R
                                    (int) (Math.random() * 255),  // G
                                    (int) (Math.random() * 255)   // B
                            )
                    );
            }


            circles.add(temp);
        }
    }

}
