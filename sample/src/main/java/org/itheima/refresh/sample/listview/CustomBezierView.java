package org.itheima.refresh.sample.listview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class CustomBezierView
        extends View
{
    private Paint mPaint;
    private Path  mPath;
    private int   mProgress;

    public CustomBezierView(Context context)
    {
        this(context, null);
    }

    public CustomBezierView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);
    }

    public void setProgress(int progress)
    {
        this.mProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        int width  = getMeasuredWidth();
        int height = getMeasuredHeight();

        float topCenterX = width / 2f;
        float topCenterY = width / 2f;
        float topRadius  = width / 2f - 2;

        float bottomRadius  = width / 12f;
        float bottomCenterX = width / 2f;
        float bottomCenterY = height - bottomRadius - 2;


        //draw top
        float tRadius  = topRadius - (topRadius - bottomRadius) * mProgress / 100f;
        float tCenterX = width / 2;
        float tCenterY = 2 + tRadius;
        canvas.drawCircle(tCenterX, tCenterY, tRadius, mPaint);

        //draw bottom
        float bRadius  = topRadius - tRadius;
        float bCenterX = width / 2;
        float bCenterY = 2 + topCenterY + (bottomCenterY - topCenterY) * mProgress / 100 - 2 * bRadius;
        canvas.drawCircle(bCenterX, bCenterY, bRadius, mPaint);


        float x1 = tCenterX - tRadius;
        float y1 = tCenterY;

        float x2 = bCenterX - bRadius;
        float y2 = bCenterY;

        float x3 = bCenterX + bRadius;
        float y3 = bCenterY;

        float x4 = tCenterX + tRadius;
        float y4 = tCenterY;

        float authorX1 = width / 2f;
        float authorY1 = (y1 + y2) / 2f;
        float authorX2 = width / 2f;
        float authorY2 = (y3 + y4) / 2f;

        mPath.reset();
        mPath.moveTo(x1, y1);
        mPath.quadTo(authorX1, authorY1, x2, y2);
        mPath.lineTo(x3, y3);
        mPath.quadTo(authorX2, authorY2, x4, y4);
        mPath.lineTo(x1, y1);
        canvas.drawPath(mPath, mPaint);
        //        calculate();
        //        canvas.drawColor(Color.TRANSPARENT);
        //        canvas.drawPath(mPath, mPaint);
        //        canvas.drawCircle(mStartX, mStartY, mRadius, mPaint);
        //        canvas.drawCircle(mCurrentX, mCurrentY, mRadius, mPaint);
    }

    //    private void calculate()
    //    {
    //        float distance = (float) Math.sqrt(Math.pow(mCurrentY - mStartY,
    //                                                    2) + Math.pow(mCurrentX - mStartX, 2));
    //        mRadius = -distance / 15 + 20;
    //
    //        //        if (radius < 9)
    //        //        {
    //        //            isAnimStart = true;
    //        //
    //        //            //            exploredImageView.setVisibility(View.VISIBLE);
    //        //            //            exploredImageView.setImageResource(R.drawable.tip_anim);
    //        //            //            ((AnimationDrawable) exploredImageView.getDrawable()).stop();
    //        //            //            ((AnimationDrawable) exploredImageView.getDrawable()).start();
    //        //            //
    //        //            //            tipImageView.setVisibility(View.GONE);
    //        //        }
    //
    //        // 根据角度算出四边形的四个点
    //        float offsetX = (float) (mRadius * Math.sin(Math.atan((mCurrentY - mStartY) / (mCurrentX - mStartX))));
    //        float offsetY = (float) (mRadius * Math.cos(Math.atan((mCurrentY - mStartY) / (mCurrentX - mStartX))));
    //
    //        float x1 = mStartX - offsetX;
    //        float y1 = mStartY + offsetY;
    //
    //        float x2 = mCurrentX - offsetX;
    //        float y2 = mCurrentY + offsetY;
    //
    //        float x3 = mCurrentX + offsetX;
    //        float y3 = mCurrentY - offsetY;
    //
    //        float x4 = mStartX + offsetX;
    //        float y4 = mStartY - offsetY;
    //
    //        mPath.reset();
    //        mPath.moveTo(x1, y1);
    //        mPath.quadTo(mMaxRadius, mMaxRadius, x2, y2);
    //        mPath.lineTo(x3, y3);
    //        mPath.quadTo(mMaxRadius, mMaxRadius, x4, y4);
    //        mPath.lineTo(x1, y1);
    //
    //        //        // 更改图标的位置
    //        //        tipImageView.setX(x - tipImageView.getWidth() / 2);
    //        //        tipImageView.setY(y - tipImageView.getHeight() / 2);
    //    }

}
