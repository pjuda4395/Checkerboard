package calibration.checkerboard;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Pawel on 4/9/2016.
 */
public class Main extends Activity {

    private double[] screenSize;
    private int mWidthPixels;
    private int mHeightPixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeOrientationLayout();
        screenSize = initializeScreenSize();
        Log.d("TEST", Arrays.toString(screenSize));
        setContentView(new CheckerBoardView(this));

    }


    /**
     * Initializes the screen in landscape mode and places the screen into fullscreen
     * */
    private void initializeOrientationLayout(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    /**
     * Call to initialize the screen size to a double array
     * Must have 2 fields declared:
     *  int mWidthPixels
     *  int mHeightPixels
     * */
    private double[] initializeScreenSize() {
        setRealDeviceSizeInPixels();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double x = Math.pow(mWidthPixels/metrics.xdpi,2);
        double y = Math.pow(mHeightPixels/metrics.ydpi,2);
        return new double[]{Math.sqrt(x)/0.039370, Math.sqrt(y)/0.039370};
    }

    /**
     * Sets the pixel values of the screen to and width and height integer fields
     * */
    private void setRealDeviceSizeInPixels() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);


        // since SDK_INT = 1;
        mWidthPixels = displayMetrics.widthPixels;
        mHeightPixels = displayMetrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception ignored) {
            }
        }

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                mWidthPixels = realSize.x;
                mHeightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Returns the screen's height in millimeters
     * */
    public double getScreenHeightMillimeters(){
        return screenSize[1];
    }

    /**
     * Returns the screen's width in millimeters
     * */
    public double getScreenWidthMillimeters(){
        return screenSize[0];
    }

}
