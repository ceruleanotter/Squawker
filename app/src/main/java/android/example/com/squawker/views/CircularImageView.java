package android.example.com.squawker.views;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by lyla on 1/11/17.
 */

public class CircularImageView extends ImageView {
    Paint mPaint;
    Shader mShader;

    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShader = new BitmapShader(((BitmapDrawable) getDrawable()).getBitmap(),
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);

    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mPaint != null) {
            // Draw a circle with the required radius.
            final float halfWidth = canvas.getWidth() / 2;
            final float halfHeight = canvas.getHeight() / 2;
            final float radius = Math.max(halfWidth, halfHeight);
            canvas.drawCircle(halfWidth, halfHeight, radius, mPaint);

        }
    }
}
