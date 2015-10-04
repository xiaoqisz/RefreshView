package org.itheima.refresh.sample.listview;

import android.content.Context;
import android.view.View;

import org.itheima.refresh.library.BaseRefreshHeader;
import org.itheima.refresh.sample.R;

public class CustomRefreshHeader
        extends BaseRefreshHeader
{
    private CustomProgressView mProgressView;

    public CustomRefreshHeader(Context context)
    {
        super(context);
    }

    @Override
    protected View initRefreshView(Context context)
    {
        View view = View.inflate(context, R.layout.listview_custom_header, null);

        mProgressView = (CustomProgressView) view.findViewById(R.id.custom_header_progress);

        return view;
    }

    @Override
    protected void onRefreshScrolled(int pullDown, int refreshHeight)
    {
        int progress = (int) (pullDown * 100f / refreshHeight + 0.5f);
        mProgressView.setProgress(progress);
    }
}
