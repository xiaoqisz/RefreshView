package org.itheima.refresh.library;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DefaultRefreshHeader
        extends BaseRefreshHeader
{
    private TextView    mTvState;
    private ProgressBar mPbLoading;
    private ImageView   mIvArrow;

    private RotateAnimation mUp2DownAnim;
    private RotateAnimation mDown2UpAnim;

    public DefaultRefreshHeader(Context context)
    {
        super(context);

        mUp2DownAnim = new RotateAnimation(-180,
                                           0,
                                           Animation.RELATIVE_TO_SELF,
                                           0.5f,
                                           Animation.RELATIVE_TO_SELF,
                                           0.5f);
        mUp2DownAnim.setDuration(context.getResources()
                                        .getInteger(android.R.integer.config_shortAnimTime));
        mUp2DownAnim.setFillAfter(true);

        mDown2UpAnim = new RotateAnimation(0,
                                           180,
                                           Animation.RELATIVE_TO_SELF,
                                           0.5f,
                                           Animation.RELATIVE_TO_SELF,
                                           0.5f);
        mDown2UpAnim.setDuration(context.getResources()
                                        .getInteger(android.R.integer.config_shortAnimTime));
        mDown2UpAnim.setFillAfter(true);
    }

    @Override
    protected View initRefreshView(Context context)
    {
        View view = View.inflate(context, R.layout.default_refresh_header, null);

        mIvArrow = (ImageView) view.findViewById(R.id.drh_iv_arrow);
        mPbLoading = (ProgressBar) view.findViewById(R.id.drh_pb_loading);
        mTvState = (TextView) view.findViewById(R.id.drh_tv_state);

        return view;
    }

    @Override
    protected void onRefreshStateChanged(int state)
    {
        switch (state)
        {
            case STATE_PULL_DOWN:
                mTvState.setText("下拉刷新");

                mPbLoading.setVisibility(View.INVISIBLE);
                mIvArrow.setVisibility(View.VISIBLE);

                mIvArrow.startAnimation(mUp2DownAnim);

                break;
            case STATE_RELEASE_REFRESH:
                mTvState.setText("释放刷新");

                mPbLoading.setVisibility(View.INVISIBLE);
                mIvArrow.setVisibility(View.VISIBLE);

                mIvArrow.startAnimation(mDown2UpAnim);

                break;
            case STATE_REFRESHING:
                mTvState.setText("正在刷新");

                mPbLoading.setVisibility(View.VISIBLE);
                mIvArrow.setVisibility(View.INVISIBLE);

                mIvArrow.clearAnimation();

                break;
        }
    }

    @Override
    public void onRefreshScrolled()
    {

    }
}
