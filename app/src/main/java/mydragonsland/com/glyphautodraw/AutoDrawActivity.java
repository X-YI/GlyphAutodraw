package mydragonsland.com.glyphautodraw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


public class AutoDrawActivity extends ActionBarActivity {

    private String LOG_TAG = "AutoDrawDragonsLand";

    private ArrayList<Integer[]> VertexList = new ArrayList<>();
    private Integer[] p0 = new Integer[]{540, 650};
    private Integer[] p1 = new Integer[]{100 ,905};
    private Integer[] p2 = new Integer[]{980 ,905};
    private Integer[] p3 = new Integer[]{320 ,1032};
    private Integer[] p4 = new Integer[]{760 ,1032};
    private Integer[] p5 = new Integer[]{540 ,1156};
    private Integer[] p6 = new Integer[]{320 ,1280};
    private Integer[] p7 = new Integer[]{760 ,1280};
    private Integer[] p8 = new Integer[]{100 ,1407};
    private Integer[] p9 = new Integer[]{980 ,1407};
    private Integer[] p10 = new Integer[]{540 ,1662};

    private int[] NOURISH = {5, 10, 8};
    private int[] PEACE = {0, 3, 4, 5, 6, 7, 10};

    private String ADB_TEMPLATE = "adb shell input swipe %d %d %d %d\r\n";

    private void init() {
        VertexList.add(p0);
        VertexList.add(p1);
        VertexList.add(p2);
        VertexList.add(p3);
        VertexList.add(p4);
        VertexList.add(p5);
        VertexList.add(p6);
        VertexList.add(p7);
        VertexList.add(p8);
        VertexList.add(p9);
        VertexList.add(p10);
    }

    private String GlyphToString(int[] p) {
        String s = null;
        for (int i = 0; i < p.length; i++) {
            Integer[] p1 = VertexList.get(p[i]);
            Integer[] p2 = VertexList.get(p[(i + 1) % p.length]);
            String current = String.format(ADB_TEMPLATE, p1[0].intValue(), p1[1].intValue(),
                    p2[0].intValue(), p2[1].intValue());
            s += current;
        }
        return s;
    }

    private void execute() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_draw);
        weakUpScreen();


        init();
        String s = GlyphToString(PEACE);
        Log.e(LOG_TAG, "\r\n" + s);
    }

    public void startDrawService(View view) {
        startService(new Intent(getBaseContext(), ChatHeadService.class));
    }

    private void weakUpScreen() {
        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakeLock.acquire();
    }
}
