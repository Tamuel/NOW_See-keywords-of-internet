package com.softart.shift.now;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;

public class ViewActivity extends Activity {
    private RelativeLayout viewRelativeLayout;
    private RelativeLayout canvasView;

    private ArrayList<Keyword> keywords;
    private ArrayList<KeywordCircle> circles;


    private String dataFromServer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        viewRelativeLayout = (RelativeLayout) findViewById(R.id.KeywordViewRelativeLayout);
        canvasView = (RelativeLayout) findViewById(R.id.drawingLayout);

        canvasView.addView(new CanvasView(this));

        keywords = new ArrayList<>();
        circles = new ArrayList<>();


        refresh(null);
    }

    public void refresh(View v) {
        keywords.clear();
        circles.clear();
        dataFromServer = ServerConnector.getInstance().getWordDataFromServer(
                "http://ydkserver.iptime.org:8080/sortbyscore"
        );
        KeywordController.parseKeyword(dataFromServer, keywords);
        KeywordController.makeCircle(keywords, circles);
    }



    protected class CanvasView extends View implements View.OnTouchListener, View.OnClickListener{
        // For coordinate system
        private final int aX = 0, aY = 1;

        // For dragging
        private int pressedPos[]        = {0, 0};
        private int movePos[]           = {600, 600};
        private int criteriaPos[]       = {0, 0};
        private int prevPos[]           = {0, 0};

        // For zooming
        private float magnitude         = 1.5f;
        private float zoomRate          = 0;
        private boolean zooming         = false;
        private int multitouchPos[][]   = {{0, 0}, {0, 0}};
        private int zoomingPos[]        = {0, 0};
        private int magClamp[]          = {1, 10};

        // For drawing
        private Paint paint = new Paint();
        private int r, g, b;

        // For longclick
        private long downTime = 0;
        private long upTime = 0;

        public CanvasView(Context context) {
            super(context);
            this.setOnTouchListener(this);
            this.setOnClickListener(this);

            paint.setAntiAlias(true);
            paint.setShader(null);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setStyle(Paint.Style.FILL);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            for(KeywordCircle circle : circles) {
                paint.setColor(circle.getFillColor());
                for(KeywordCircle circle1 : circles) {
                    if(!circle.equals(circle1)) {
                        int distance = (int) Math.sqrt(
                                Math.pow(circle.getPosition().x - circle1.getPosition().x, 2) +
                                Math.pow(circle.getPosition().y - circle1.getPosition().y, 2)
                        );
                        if(distance < circle.getRadius() + circle1.getRadius()) {
                            if(distance == 0) distance = 1;
                            int moveX = 0, moveY = 0;
                            try {
                                moveX = (int)((circle.getRadius() + circle1.getRadius()) / distance * 3 *
                                    circle.getPosition().x / circle1.getPosition().x / Math.sqrt(circle.getRadius()));
                                moveY = (int)((circle.getRadius() + circle1.getRadius()) / distance * 3 *
                                    circle.getPosition().y / circle1.getPosition().y / Math.sqrt(circle.getRadius()));
                            } catch (ArithmeticException e) {
                                moveX = (int)((circle.getRadius() + circle1.getRadius()) / 1);
                                moveY = (int)((circle.getRadius() + circle1.getRadius()) / 1);
                            }

                            if(moveX < 1)
                                    moveX = 1;
                            if(moveY < 1)
                                    moveY = 1;

                            if(circle.getPosition().x > circle1.getPosition().x) {
                                    circle.getPosition().x += moveX;
                                    circle1.getPosition().x -= 1;
                            }else {
                                    circle.getPosition().x -= moveX;
                                    circle1.getPosition().x += 1;
                            }
                            if(circle.getPosition().y > circle1.getPosition().y) {
                                    circle.getPosition().y += moveY;
                                    circle1.getPosition().y -= 1;
                            } else {
                                    circle.getPosition().y -= moveY;
                                    circle1.getPosition().y += 1;
                            }
                        }
                    }
                }
                canvas.drawCircle(
                        criteriaPos[aX] + circle.getPosition().x * magnitude,
                        criteriaPos[aY] + circle.getPosition().y * magnitude,
                        circle.getRadius() * magnitude,
                        paint
                );
            }

            for(KeywordCircle circle : circles) {
                float textSize = circle.getRadius() / 3 * magnitude;
                r = (circle.getFillColor() >> 16) & 0xFF;
                g = (circle.getFillColor() >>  8) & 0xFF;
                b =  circle.getFillColor()        & 0xFF;
                paint.setTextSize(textSize);
                paint.setColor(Color.rgb(255 - r, 255 - g, 255 - b));
                canvas.drawText(
                        circle.getKeyword().getWord(),
                        criteriaPos[aX] + circle.getPosition().x * magnitude,
                        criteriaPos[aY] + circle.getPosition().y * magnitude + textSize / 2,
                        paint
                );
            }

            invalidate();
        }

         @Override
         public void onClick(View v) {
                 Log.i("clicked!", (pressedPos[aX] * magnitude) + " " + (pressedPos[aY] * magnitude));
            for(KeywordCircle circle : circles) {
                    int length = (int)Math.sqrt(
                            Math.pow(circle.getPosition().x * magnitude - (pressedPos[aX] * magnitude), 2) +
                            Math.pow(circle.getPosition().x * magnitude - (pressedPos[aX] * magnitude), 2)
                    );
                    if(length < circle.getRadius()) {
                            Uri uri = Uri.parse("https://www.google.co.kr/webhp?hl=ko#newwindow=1&hl=ko&q=" + URLEncoder.encode(circle.getKeyword().getWord()));
                            Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                            startActivity(it);
                            break;
                    }
            }
         }
            public int getLength(int x1, int x2, int y1, int y2) {
                    return (int)Math.sqrt(
                            Math.pow(x1 - x2, 2) +
                            Math.pow(y1 - y2, 2)
                    );
            }

            @Override
        public boolean onTouch(View v, MotionEvent event) {
            int pointerIndex = event.getActionIndex();
            int fingerId = event.getPointerId(pointerIndex);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_DOWN:
                    downTime = System.currentTimeMillis();
                    if(event.getPointerCount() <= 2) {
                        multitouchPos[fingerId][aX] = (int) event.getX(pointerIndex);
                        multitouchPos[fingerId][aY] = (int) event.getY(pointerIndex);
                    }
                    if(event.getPointerCount() == 2) {
                        zooming = true;
                        zoomRate = (float)(magnitude / Math.sqrt(
                                Math.pow(multitouchPos[0][aX] - multitouchPos[1][aX], 2) +
                                        Math.pow(multitouchPos[0][aY] - multitouchPos[1][aY], 2)
                        )
                        );
                        pressedPos[aX] = (multitouchPos[0][aX] + multitouchPos[1][aX]) / 2;
                        pressedPos[aY] = (multitouchPos[0][aY] + multitouchPos[1][aY]) / 2;
                        prevPos[aX] = criteriaPos[aX];
                        prevPos[aY] = criteriaPos[aY];
                    }

                    if(!zooming) {
                        pressedPos[aX] = (int) event.getX();
                        pressedPos[aY] = (int) event.getY();

                        prevPos[aX] = criteriaPos[aX];
                        prevPos[aY] = criteriaPos[aY];
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if(event.getPointerCount() == 1 && zooming) {
                        zooming = false;
                        pressedPos[aX] = (int) event.getX();
                        pressedPos[aY] = (int) event.getY();
                        break;
                    }


                    if(!zooming) {
                        movePos[aX] = (int) event.getX() - pressedPos[aX];
                        movePos[aY] = (int) event.getY() - pressedPos[aY];

                        criteriaPos[aX] = prevPos[aX] + movePos[aX];
                        criteriaPos[aY] = prevPos[aY] + movePos[aY];
                    } else {
                        if(event.getPointerCount() >= 3)
                            break;

                        for(int i = 0; i < event.getPointerCount(); i++) {
                            multitouchPos[i][aX] = (int) event.getX(i);
                            multitouchPos[i][aY] = (int) event.getY(i);
                        }

                        if(event.getPointerCount() == 2) {
                            int tempX = (int)(magnitude * (multitouchPos[0][aX] + multitouchPos[1][aX]) / 2);
                            int tempY = (int)(magnitude * (multitouchPos[0][aY] + multitouchPos[1][aY]) / 2);
                            magnitude = (float)(zoomRate *
                                    Math.sqrt(
                                            Math.pow(multitouchPos[0][aX] - multitouchPos[1][aX], 2) +
                                                    Math.pow(multitouchPos[0][aY] - multitouchPos[1][aY], 2)
                                    )
                            );
                            if(magnitude <= magClamp[0])
                                magnitude = magClamp[0];
                            else if(magnitude > magClamp[1])
                                magnitude = magClamp[1];
                            int tempX2 = (int)(magnitude * (multitouchPos[0][aX] + multitouchPos[1][aX]) / 2);
                            int tempY2 = (int)(magnitude * (multitouchPos[0][aY] + multitouchPos[1][aY]) / 2);
                            zoomingPos[aX] = tempX2 - tempX;
                            zoomingPos[aY] = tempY2 - tempY;
                        }

                        prevPos[aX] -= zoomingPos[aX] / 3;
                        prevPos[aY] -= zoomingPos[aY] / 3;

                        criteriaPos[aX] = prevPos[aX] +
                                (multitouchPos[0][aX] + multitouchPos[1][aX]) / 2 -
                                pressedPos[aX];

                        criteriaPos[aY] = prevPos[aY] +
                                (multitouchPos[0][aY] + multitouchPos[1][aY]) / 2 -
                                pressedPos[aY];

                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    upTime = System.currentTimeMillis();
                    if(event.getPointerCount() == 1 && zooming)
                        zooming = false;

                    Log.i("le", getLength(prevPos[aX], criteriaPos[aX], prevPos[aY], criteriaPos[aY]) + "");
                    if(!zooming && getLength(prevPos[aX], criteriaPos[aX], prevPos[aY], criteriaPos[aY]) < 15) {
                        if(upTime - downTime >= 1500) {
                            for (KeywordCircle circle : circles) {
                                int length = (int) Math.sqrt(
                                        Math.pow(criteriaPos[aX] + circle.getPosition().x * magnitude - (pressedPos[aX]), 2) +
                                                Math.pow(criteriaPos[aY] + circle.getPosition().y * magnitude - (pressedPos[aY]), 2)
                                );
                                if (length < circle.getRadius() * magnitude) {
                                    ServerConnector.getInstance().getWordDataFromServer("http://ydkserver.iptime.org:8080/keyword/" + circle.getKeyword().getWord() + "/" + 1);
                                    Toast.makeText(ViewActivity.this, "+1 at " + circle.getKeyword().getWord(), Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                        } else {
                            for (KeywordCircle circle : circles) {
                                int length = (int) Math.sqrt(
                                        Math.pow(criteriaPos[aX] + circle.getPosition().x * magnitude - (pressedPos[aX]), 2) +
                                                Math.pow(criteriaPos[aY] + circle.getPosition().y * magnitude - (pressedPos[aY]), 2)
                                );
                                if (length < circle.getRadius() * magnitude) {
                                    Uri uri = Uri.parse("https://www.google.co.kr/webhp?hl=ko#newwindow=1&hl=ko&q=" + URLEncoder.encode(circle.getKeyword().getWord()));
                                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(it);
                                    break;
                                }
                            }
                        }
                    }
                    break;
            }

            return true;
        }
    }
}
