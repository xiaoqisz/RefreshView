package org.itheima.refresh.library;


import android.content.Context;
import android.view.View;

public abstract class BaseRefreshHeader
{
    public final static int STATE_PULL_DOWN       = 0;
    public final static int STATE_RELEASE_REFRESH = 1;
    public final static int STATE_REFRESHING      = 2;

    private View mRefreshView;

    public BaseRefreshHeader(Context context)
    {
        mRefreshView = initRefreshView(context);
    }

    /**
     * init layout for refresh view
     * @param context
     * @return
     */
    protected abstract View initRefreshView(Context context);

    /**
     * the callback of refresh state changed
     * @param state
     */
    protected void onRefreshStateChanged(int state)
    {

    }

    public void onRefreshScrolled()
    {

    }

    /**
     * @return refresh view
     */
    public View getRefreshView()
    {
        return mRefreshView;
    }


}
