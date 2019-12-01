package pl.pwr.swipeanimation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private int frameNumber = 360;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.frame);

        imageView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                handler = new Handler();

                final Runnable runnable = new Runnable() {
                    int count = 0;
                    public void run() {
                        if (count++ < 60) {
                            if(frameNumber == 360)
                                frameNumber = 660;
                            else
                                frameNumber--;
                            imageView.setImageResource(getResources().getIdentifier("frame0" + frameNumber, "drawable", getPackageName()));
                            handler.postDelayed(this, 1);
                        }
                    }
                };

                handler.post(runnable);
            }
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                handler = new Handler();

                final Runnable runnable = new Runnable() {
                    int count = 0;
                    public void run() {
                        if (count++ < 60) {
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
        });
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
}
