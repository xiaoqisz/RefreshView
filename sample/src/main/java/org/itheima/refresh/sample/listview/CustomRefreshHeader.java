package org.itheima.refresh.sample.listview;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import org.itheima.refresh.library.BaseRefreshHeader;
import org.itheima.refresh.sample.R;

public class CustomRefreshHeader
        extends BaseRefreshHeader
{
    //    private CustomProgressView mBezierView;
    private CustomBezierView mBezierView;
    private ProgressBar      mProgressBar;

    public CustomRefreshHeader(Context context)
    {
        super(context);
    }

    @Override
    protected View initRefreshView(Context context)
    {
        View view = View.inflate(context, R.layout.listview_custom_header, null);

        //        mBezierView = (CustomProgressView) view.findViewById(R.id.custom_header_progress);
        mBezierView = (CustomBezierView) view.findViewById(R.id.custom_header_bezier);
        mProgressBar = (ProgressBar) view.findViewById(R.id.custom_header_pb);

        return view;
    }

    @Override
    protected void onRefreshStateChanged(int state)
    {
        if (state == STATE_REFRESHING)
        {
            mBezierView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else
        {
            mBezierView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onRefreshScrolled(int pullDown, int refreshHeight)
    {
        int progress = (int) (pullDown * 100f / refreshHeight + 0.5f);
        if (progress > 100)
        {
            progress = 100;
        }
        //        mBezierView.setProgress(progress);
        //        mBezierView.setMax(refreshHeight);

        mBezierView.setProgress(progress);
    }
}
