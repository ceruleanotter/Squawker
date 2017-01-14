/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

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
 * Creates a circular view for profile images
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

    /**
     * Generates paint and a shader for the image whenever the image is changed
     *
     * @param resId Resource ID of the image
     */
    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShader = new BitmapShader(((BitmapDrawable) getDrawable()).getBitmap(),
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);

    }

    /**
     * Draws the image and clips it to a circle
     */
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
