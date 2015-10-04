package org.itheima.refresh.sample.listview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CustomProgressView
        extends View
{
    private Paint mPaint = new Paint();
    private int   mProgress;
    private float mRadius;
    private RectF mOval;

    public CustomProgressView(Context context)
    {
        this(context, null);
    }

    public CustomProgressView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setProgress(int progress)
    {
        this.mProgress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthSize  = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int size = widthSize > heightSize
                   ? heightSize
                   : widthSize;

        mRadius = size / 2f;

        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.reset();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);

        if (mOval == null)
        {
            mOval = new RectF(0, 0, mRadius * 2, mRadius * 2);
        }

        float sweep = mProgress * 360f / 100;

        canvas.drawArc(mOval, -90, sweep, true, mPaint);
    }

}
