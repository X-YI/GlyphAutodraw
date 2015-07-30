package mydragonsland.com.glyphautodraw;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;


/**
 * Created by wangxin on 15/6/16.
 */
public class FloatingWindowsService extends Service {

    private WindowManager windowManager;
    private ImageView floatingWindows;
    private Button startRecordButton;
    private Button endRecordButton;
    private int buttonOffsetX = 200;
    private boolean visible = false;


    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        floatingWindows = new ImageView(this);
        floatingWindows.setImageResource(R.mipmap.ic_launcher);
        startRecordButton = new Button(this);
        startRecordButton.setText("开录");
        endRecordButton = new Button(this);
        endRecordButton.setText("结录");

        startRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        final WindowManager.LayoutParams startRecordParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        final WindowManager.LayoutParams endRecordParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        startRecordParams.gravity = Gravity.TOP | Gravity.LEFT;
        startRecordParams.x = (params.x + buttonOffsetX);
        startRecordParams.y = (params.y);


        endRecordParams.gravity = Gravity.TOP | Gravity.LEFT;
        endRecordParams.x = (startRecordParams.x + buttonOffsetX);
        endRecordParams.y = (startRecordParams.y);

        windowManager.addView(floatingWindows, params);
        windowManager.addView(startRecordButton, startRecordParams);
        windowManager.addView(endRecordButton, endRecordParams);

        setVisibility(false);

        floatingWindows.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        if (initialTouchX == event.getRawX() && initialTouchY == event.getRawY()) {
                            return false;
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (initialTouchX == event.getRawX() && initialTouchY == event.getRawY()) {
                            return false;
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        startRecordParams.x = params.x + buttonOffsetX;
                        startRecordParams.y = params.y;
                        endRecordParams.x = startRecordParams.x + buttonOffsetX;
                        endRecordParams.y = startRecordParams.y;
                        windowManager.updateViewLayout(floatingWindows, params);
                        windowManager.updateViewLayout(startRecordButton, startRecordParams);
                        windowManager.updateViewLayout(endRecordButton, endRecordParams);
                        return true;
                }
                return false;
            }
        });

        floatingWindows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(visible);
                visible = !visible;
            }
        });
    }

    private void setVisibility(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        startRecordButton.setVisibility(visibility);
        endRecordButton.setVisibility(visibility);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingWindows != null) windowManager.removeView(floatingWindows);
        if (startRecordButton != null) windowManager.removeView(startRecordButton);
        if (endRecordButton != null) windowManager.removeView(endRecordButton);
    }
}