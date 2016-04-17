package calibration.checkerboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


public class CheckerBoardView extends SurfaceView implements Runnable{

    SurfaceHolder holder;
    Paint paint;
    Canvas canvas;

    public CheckerBoardView(Context context) {
        super(context);
        holder = getHolder();
        paint = new Paint();

        Log.d("TEST", holder.getSurface().isValid() + "");

        /*
        canvas = holder.lockCanvas();
        paint.setColor(Color.BLUE);
        canvas.drawText("100", 20, 20, paint);*/
    }

    @Override
    public void run() {
        
    }
}
