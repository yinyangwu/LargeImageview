package com.example.youngwu.largeimageviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Desc:
 * Created by yyw on 2018/4/23 0023.
 */
public class LargeImageView extends View {
    private BitmapRegionDecoder regionDecoder;
    private int imgWidth, imgHeight;
    private Rect rect = new Rect();
    private BitmapFactory.Options options = new BitmapFactory.Options();
    private int xPoint, yPoint;

    public LargeImageView(Context context) {
        super(context);
        init();
    }

    public LargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LargeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public void setInputstream(InputStream stream) {
        try {
            regionDecoder = BitmapRegionDecoder.newInstance(stream, false);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(stream, null, opt);
            imgWidth = opt.outWidth;
            imgHeight = opt.outHeight;

            requestLayout();
            invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int mWidth = imgWidth;
        int mHeight = imgHeight;

        rect.left = mWidth / 2 - width / 2;
        rect.top = mHeight / 2 - height / 2;
        rect.right = rect.left + width;
        rect.bottom = rect.top + height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xPoint = (int) event.getX();
                yPoint = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int xOffset = (int) (event.getX() - xPoint);
                int yOffset = (int) (event.getY() - yPoint);
                if (imgWidth > getWidth()) {
                    rect.offset(-xOffset, 0);
                    checkWidth();
                    invalidate();
                }
                if (imgHeight > getHeight()) {
                    rect.offset(0, -yOffset);
                    checkHeight();
                    invalidate();
                }
                break;
        }
        return true;
    }

    private void checkWidth() {
        Rect mRect = rect;
        int mWidth = imgWidth;

        if (mRect.right > mWidth) {
            mRect.right = mWidth;
            mRect.left = mWidth - getWidth();
        }
        if (mRect.left < 0) {
            mRect.left = 0;
            mRect.right = getWidth();
        }
    }

    private void checkHeight() {
        Rect mRect = rect;
        int mHeight = imgHeight;

        if (mRect.bottom > mHeight) {
            mRect.bottom = mHeight;
            mRect.top = mHeight - getHeight();
        }
        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = getHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bm = regionDecoder.decodeRegion(rect, options);
        canvas.drawBitmap(bm, 0, 0, null);
    }
}
