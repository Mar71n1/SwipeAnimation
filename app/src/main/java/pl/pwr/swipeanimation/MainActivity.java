package pl.pwr.swipeanimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private ImageView imageView;
    private int frameNumber = 360;
    private Handler handler;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.frame);
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);


//        imageView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
//            public void onSwipeRight() {
//                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
//                handler = new Handler();
//
//                final Runnable runnable = new Runnable() {
//                    int count = 0;
//                    public void run() {
//                        if (count++ < 60) {
//                            if(frameNumber == 360)
//                                frameNumber = 660;
//                            else
//                                frameNumber--;
//                            imageView.setImageResource(getResources().getIdentifier("frame0" + frameNumber, "drawable", getPackageName()));
//                            handler.postDelayed(this, 1);
//                        }
//                    }
//                };
//
//                handler.post(runnable);
//            }
//            public void onSwipeLeft() {
//                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
//                handler = new Handler();
//
//                final Runnable runnable = new Runnable() {
//                    int count = 0;
//                    public void run() {
//                        if (count++ < 60) {
//                            if(frameNumber == 660)
//                                frameNumber = 360;
//                            else
//                                frameNumber++;
//                            imageView.setImageResource(getResources().getIdentifier("frame0" + frameNumber, "drawable", getPackageName()));
//                            handler.postDelayed(this, 1);
//                        }
//                    }
//                };
//
//                handler.post(runnable);
//            }
//        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        //Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        //Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        Log.d(DEBUG_TAG, "onScroll.distanceX: " + distanceX + ", onScroll.distanceY:" + distanceY);

        //int distance = (int) distanceX;
        if(distanceX < 0) {
            for(int i = 0; i < 5; i++) {
                if(frameNumber == 360)
                    frameNumber = 300;
                else
                    frameNumber--;
                imageView.setImageResource(getResources().getIdentifier("frame0" + frameNumber, "drawable", getPackageName()));
            }
        } else if (distanceX > 0) {
            handler = new Handler();

            final Runnable runnable = new Runnable() {
                int count = 0;
                public void run() {
                    if (count++ < 5) {
                        if(frameNumber == 660)
                            frameNumber = 360;
                        else
                            frameNumber++;
                        imageView.setImageResource(getResources().getIdentifier("frame0" + frameNumber, "drawable", getPackageName()));
                        handler.postDelayed(this, 1);
                    }
                }
            };

            handler.post(runnable);
        }


        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }
}
