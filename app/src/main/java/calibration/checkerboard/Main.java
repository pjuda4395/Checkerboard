package calibration.checkerboard;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Pawel on 4/9/2016.
 */

public class Main extends Activity {

    private double[] screenSize;
    private int mWidthPixels;
    private int mHeightPixels;
    private ImageView view;
    private double[] centerScreenMM;
    private double[] centerScreenPX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeOrientationLayout();
        screenSize = initializeScreenSize();
        centerScreenMM = new double[]{screenSize[0] / 2, screenSize[1] / 2};
        centerScreenPX = new double[]{mWidthPixels/2,mHeightPixels/2};
        Log.d("TEST" , Arrays.toString(screenSize) + "\n" + mHeightPixels + " " + mWidthPixels);

        draw();

        setContentView(view);

    }

    private void draw() {
        view = new ImageView(this);
        Paint paint = new Paint();
        paint.setTextSize(24);
        float sizeSquare = gcd(new long[]{(long)mWidthPixels,(long)mHeightPixels});
        Bitmap blank = Bitmap.createBitmap(mWidthPixels,mHeightPixels,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(blank);
        view.setImageBitmap(blank);
        paint.setColor(Color.BLACK);
        logDebug(mWidthPixels / sizeSquare + " " + mHeightPixels / sizeSquare);
        for(int i = 0; i < mWidthPixels/sizeSquare; i++){
            for(int j = 0; j < mHeightPixels/sizeSquare; j++){
                if(paint.getColor() == Color.BLACK)
                    paint.setColor(Color.WHITE);
                else
                    paint.setColor(Color.BLACK);
                canvas.drawRect(i * sizeSquare, j * sizeSquare,(i+1)*sizeSquare,(j+1)*sizeSquare,paint);
            }
        }
        paint.setColor(Color.WHITE);
        for(int i = 0, index = 0; i < mWidthPixels/sizeSquare; i++){
            for(int j = 0; j < mHeightPixels/sizeSquare; j++){
                if(paint.getColor() == Color.BLACK)
                    paint.setColor(Color.WHITE);
                else
                    paint.setColor(Color.BLACK);
                canvas.drawText(index + "", i * sizeSquare + sizeSquare/2, j * sizeSquare + sizeSquare/2,paint);
                index++;
            }
        }
        paint.setColor(Color.RED);
        for(int i = 0; i < mWidthPixels/sizeSquare - 1; i++){
            for(int j = 0; j < mHeightPixels/sizeSquare - 1; j++){
                canvas.drawCircle((i+1) * sizeSquare, (j+1) * sizeSquare,4,paint);
            }
        }

    }

    private void logDebug(String string){
        Log.d("TEST",string);
    }


    /**
     * Helper method used for finding the greatest common denominator*/
    private static long gcd(long a, long b)
    {
        while (b > 0)
        {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    /**
     * Greatest common denominator method*/
    private static long gcd(long[] input)
    {
        long result = input[0];
        for(int i = 1; i < input.length; i++) result = gcd(result, input[i]);
        return result;
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

        // includes window decorations (status bar/menu bar)
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
