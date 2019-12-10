package pl.pwr.swipeanimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import static android.net.Uri.*;
import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private ConstraintLayout popUp;
    private ImageView imageView1, imageView2, photo;
    private TextView name, price, description;
    private Button buyButton;
    private String url;
    //private String frameString = "frame0";
    private int frameNumber = 360;
    private Handler handler;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    private int screenWidth;
    private boolean isRed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popUp = findViewById(R.id.popupbackground);
        imageView1 = findViewById(R.id.frame1);
        imageView2 = findViewById(R.id.frame2);
        photo = findViewById(R.id.photo);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        description.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        buyButton = findViewById(R.id.przycisk_kup);
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
            if(frameNumber == 360)
                frameNumber = 660;
            else
                frameNumber -= 3;

            imageView1.setImageResource(getResources().getIdentifier("frame0" + frameNumber, "drawable", getPackageName()));
            if(isRed) imageView2.setImageResource(getResources().getIdentifier("framered0" + frameNumber, "drawable", getPackageName()));
        } else if (distanceX > 0) {
            if (frameNumber == 660)
                frameNumber = 360;
            else
                frameNumber += 3;

            imageView1.setImageResource(getResources().getIdentifier("frame0" + frameNumber, "drawable", getPackageName()));
            if(isRed) imageView2.setImageResource(getResources().getIdentifier("framered0" + frameNumber, "drawable", getPackageName()));
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        if(popUp.getVisibility() == View.VISIBLE)
            popUp.setVisibility(View.INVISIBLE);
        else {
            imageView2.setImageResource(getResources().getIdentifier("framered0" + frameNumber, "drawable", getPackageName()));
            Bitmap bitmap = ((BitmapDrawable)imageView2.getDrawable()).getBitmap();
            int x;
            try {
                x = (int)event.getX();
                int pixel = bitmap.getPixel(((int)event.getX() - (screenWidth - 1920)), (int)event.getY());
                Log.d(DEBUG_TAG, "onSingleTapUp: " + frameNumber + ", " + (x - (screenWidth - 1920)) + ", " + x + ", " + Color.red(pixel) + ", " + Color.blue(pixel) + ", " + Color.green(pixel));

                if(100 < Color.red(pixel) && Color.blue(pixel) < 100 && Color.green(pixel) < 100) {
                    if(frameNumber <= 379) { // BRAK WAZONU
                        if(x <= 1400) setTVPopUp();
                        else setDrawerPopUp();
                    } else if(frameNumber <= 393) { //wszystkie trzy
                        if (2000 <= x) setVasePopUp();
                        else if(x <= 1300) setTVPopUp();
                        else setDrawerPopUp();
                    } else if(frameNumber <= 450) {
                        if(x <= 1400) setDrawerPopUp();
                        else if(1400 < x) setVasePopUp();
                    } else if(447 <= frameNumber && frameNumber <= 553) {
                        if(x < 900) setTVPopUp();
                        else if(1400 < x) setVasePopUp();
                        else setDrawerPopUp();
                    } else if(553 <= frameNumber) // JEST TYLKO SZAFKA
                        setDrawerPopUp();

                    popUp.setVisibility(View.VISIBLE);
                }
            }
            catch (Exception ex) { }
        }
        return true;
    }

    Runnable showRed = new Runnable() {
        @Override
        public void run() {
            imageView2.setImageResource(getResources().getIdentifier("framered0" + frameNumber, "drawable", getPackageName()));
            imageView2.setVisibility(View.VISIBLE);
            isRed = true;
            handler.postDelayed(hideRed, 200);
        }
    };

    Runnable hideRed = new Runnable() {
        @Override
        public void run() {
            //imageView2.setImageResource(getResources().getIdentifier(frameString + frameNumber, "drawable", getPackageName()));
            imageView2.setVisibility(View.INVISIBLE);
            isRed = false;
            handler.postDelayed(showRed, 800);
        }
    };

    private void setTVPopUp() {
        photo.setImageResource(getResources().getIdentifier("pobrane", "drawable", getPackageName()));
        name.setText("Telewizor Samsung 55'");
        price.setText("Cena: 5000 złotych");
        description.setText(R.string.tv_description);
        url = "https://www.euro.com.pl/telewizory-led-lcd-plazmowe/samsung-qled-qe55q67rat.bhtml?gclid=CjwKCAiAob3vBRAUEiwAIbs5TgrqbQn8fwYi2BVgUTPO0WLza03GlkEV2QeAhUNic1jq-41L_VOT1BoCy4IQAvD_BwE&gclsrc=aw.ds";
    }

    private void setDrawerPopUp() {
        photo.setImageResource(getResources().getIdentifier("szafka", "drawable", getPackageName()));
        name.setText("Szafka");
        price.setText("Cena: 1500 złotych");
        description.setText(R.string.drawer_description);
        url = "https://www.ikea.com/pl/pl/p/kallax-regal-bialy-80275887/?gclid=CjwKCAiAob3vBRAUEiwAIbs5Tv_EI7LDHU2De7lUp71htIkFmJCiz-K9X76AeIhnYtHxkv0W_pYfUxoCs3EQAvD_BwE";
    }

    private void setVasePopUp() {
        photo.setImageResource(getResources().getIdentifier("images", "drawable", getPackageName()));
        name.setText("Waza");
        price.setText("Cena: 500 złotych");
        description.setText(R.string.vase_description);
        url = "https://www.ikea.com/pl/pl/p/vasen-wazon-szklo-bezbarwne-00017133/?gclid=CjwKCAiAob3vBRAUEiwAIbs5Tu0V3s3CT1Zgb7hbRskrLuyGPqlOWaAtCkoF64fjXd-8AEmmpL_ymhoCDjYQAvD_BwE";
    }

    public void buy(View view) {
        Uri uri = parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
