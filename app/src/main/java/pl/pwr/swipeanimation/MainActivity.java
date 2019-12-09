package pl.pwr.swipeanimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private ImageView imageView1, imageView2;
    private int frameNumber = 360;
    private Handler handler;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView1 = findViewById(R.id.frame1);
        imageView2 = findViewById(R.id.frame2);
        //imageView.setVisibility(View.INVISIBLE);
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        handler = new Handler();
        handler.post(showRed);
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
        Log.d(DEBUG_TAG,"onDown: " + event.toString());
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
        Log.d(DEBUG_TAG, "onScroll.distanceX: " + distanceX + ", onScroll.distanceY:" + distanceY + ", event1: " + event1.toString() + ", event2: " + event2.toString());
        if(distanceX < 0) {
            handler.post(frameDown);
        } else if (distanceX > 0) {
            handler.post(frameUp);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Bitmap bitmap = ((BitmapDrawable)imageView2.getDrawable()).getBitmap();
        try {
            int pixel = bitmap.getPixel(((int)event.getX() - (screenWidth - 1920))/2, (int)event.getY());
            Log.d(DEBUG_TAG, "onSingleTapUp: " + ((int)event.getX() - ((screenWidth - 1920))) + ", " + (int)event.getX() + ", " + Color.red(pixel) + ", " + Color.blue(pixel) + ", " + Color.green(pixel));

            if(100 < Color.red(pixel) && Color.blue(pixel) < 100 && Color.green(pixel) < 100)
                Toast.makeText(this, "Kliknięto czerwone", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) { }

        return true;
    }

    Runnable frameUp = new Runnable() {
        @Override
        public void run() {
            if (frameNumber == 660)
                frameNumber = 360;
            else
                frameNumber += 3;

            imageView1.setImageResource(getResources().getIdentifier("frame0" + frameNumber, "drawable", getPackageName()));
            imageView2.setImageResource(getResources().getIdentifier("framered0" + frameNumber, "drawable", getPackageName()));
        }
    };

    Runnable frameDown = new Runnable() {
        @Override
        public void run() {
            if(frameNumber == 360)
                frameNumber = 660;
            else
                frameNumber -= 3;

            imageView1.setImageResource(getResources().getIdentifier("frame0" + frameNumber, "drawable", getPackageName()));
            imageView2.setImageResource(getResources().getIdentifier("framered0" + frameNumber, "drawable", getPackageName()));
        }
    };

    Runnable showRed = new Runnable() {
        @Override
        public void run() {
            //imageView2.setImageResource(getResources().getIdentifier("framered0" + frameNumber, "drawable", getPackageName()));
            imageView2.setVisibility(View.VISIBLE);
            handler.postDelayed(hideRed, 200);
        }
    };

    Runnable hideRed = new Runnable() {
        @Override
        public void run() {
            imageView2.setVisibility(View.INVISIBLE);
            handler.postDelayed(showRed, 800);
        }
    };
}
