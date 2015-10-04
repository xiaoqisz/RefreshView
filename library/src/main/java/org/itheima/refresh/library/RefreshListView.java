package org.itheima.refresh.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.lang.reflect.Constructor;

public class RefreshListView
        extends ListView
{

    private static final String TAG           = "RefreshListView";
    private              int    mCurrentState = BaseRefreshHeader.STATE_PULL_DOWN;

    private LinearLayout mRefreshContainer;

    private String            mRefreshHeaderClass;
    private BaseRefreshHeader mRefreshHeader;
    private int               mRefreshHeight;
    private int               mDownX;
    private int mDownY = -1;

    private View mFirstCustomHeader;
    private int mCurrentLocationY = -1;

    private int     mHiddenSpace    = -1;
    private boolean mInterceptSuper = true;

    private OnItemClickListener mOnItemClickListener;
    private Drawable            mItemSelector;

    private Animator mHeaderAnimator;

    private Handler mHandler = new Handler();

    public RefreshListView(Context context)
    {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet set)
    {
        super(context, set);

        TypedArray ta = context.obtainStyledAttributes(set, R.styleable.RefreshListView);

        mRefreshHeaderClass = ta.getString(R.styleable.RefreshListView_rlvRefreshHeader);

        ta.recycle();

        initHeaderLayout();
    }

    private void initHeaderLayout()
    {
        //init header layout
        if (TextUtils.isEmpty(mRefreshHeaderClass))
        {
            mRefreshHeaderClass = DefaultRefreshHeader.class.getName();
            mRefreshHeader = new DefaultRefreshHeader(getContext());
        } else
        {
            try
            {
                Class<?> clazz = Class.forName(mRefreshHeaderClass);
                Constructor<?> constructor = clazz.getConstructor(Context.class);
                Object o = constructor.newInstance(getContext());
                if (o instanceof BaseRefreshHeader)
                {
                    mRefreshHeader = (BaseRefreshHeader) o;
                } else
                {
                    throw new IllegalArgumentException("class " + mRefreshHeaderClass + " must extends BaseRefreshHeader");
                }
            } catch (ClassNotFoundException e)
            {
                throw new IllegalArgumentException("refresh header class not found : " + mRefreshHeaderClass);
            } catch (NoSuchMethodException e)
            {
                throw new IllegalArgumentException("class " + mRefreshHeaderClass + " constructor error");
            } catch (Exception e)
            {
                throw new IllegalArgumentException("class " + mRefreshHeaderClass + " constructor error");
            }
        }

        mRefreshContainer = new LinearLayout(getContext());
        mRefreshContainer.setBackgroundColor(Color.WHITE);
        mRefreshContainer.setOrientation(LinearLayout.VERTICAL);
        mRefreshContainer.addView(mRefreshHeader.getRefreshView());

        // add refresh part
        addHeaderView(mRefreshContainer);

        // hiden refresh part
        mRefreshHeight = mRefreshHeader.getRefreshHeight();
        mRefreshContainer.setPadding(0, -mRefreshHeight, 0, 0);
    }

    @Override
    public void addHeaderView(View v)
    {
        if (v != mRefreshContainer)
        {
            mFirstCustomHeader = v;
        }
        super.addHeaderView(v);
    }

    @Override
    public void addHeaderView(View v, Object data, boolean isSelectable)
    {
        if (v != mRefreshContainer)
        {
            mFirstCustomHeader = v;
        }
        super.addHeaderView(v, data, isSelectable);
    }

    private int getFirstViewLocationY()
    {
        int y = 0;
        if (mFirstCustomHeader != null)
        {
            // as first header(except refresh header)
            int[] loc = new int[2];
            mFirstCustomHeader.getLocationOnScreen(loc);
            y = loc[1] - getDividerHeight();
        } else
        {
            int count = getChildCount();
            if (count > 0)
            {
                //as item view
                View view = getChildAt(0);
                int[] loc = new int[2];
                view.getLocationOnScreen(loc);
                y = loc[1];
            } else
            {
                // as listview
                int[] loc = new int[2];
                getLocationOnScreen(loc);
                y = loc[1];
            }
        }
        return y;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mOnItemClickListener = listener;
        super.setOnItemClickListener(listener);
    }

    @Override
    public void setSelector(Drawable sel)
    {
        this.mItemSelector = sel;
        super.setSelector(sel);
    }

    @Override
    public boolean performItemClick(View view, int position, long id)
    {
        if (position == 0)
        {
            view.setPressed(false);
            return false;
        }
        return super.performItemClick(view, position, id);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) (ev.getX() + 0.5f);
                mDownY = (int) (ev.getY() + 0.5f);

                if (mHeaderAnimator != null)
                {
                    mHeaderAnimator.cancel();
                    mHeaderAnimator = null;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) (ev.getX() + 0.5f);
                int moveY = (int) (ev.getY() + 0.5f);

                if (mDownY == -1)
                {
                    //never run action_down
                    mDownY = moveY;
                }

                int diffY = moveY - mDownY;

                //if is refreshing, cannot pull
                if (mCurrentState == BaseRefreshHeader.STATE_REFRESHING)
                {
                    break;
                }

                int mFirstViewY = getFirstViewLocationY();
                if (mCurrentLocationY == -1)
                {
                    int[] loc = new int[2];
                    getLocationOnScreen(loc);
                    mCurrentLocationY = loc[1];
                }

                Log.d(TAG, mCurrentLocationY + "     " + mFirstViewY);

                if (mCurrentLocationY > (mFirstViewY))
                {
                    // a part hidden
                    if (mHiddenSpace == -1)
                    {
                        mHiddenSpace = mCurrentLocationY - mFirstViewY;
                    }
                } else
                {
                    if (mHiddenSpace == -1 && mCurrentLocationY == mFirstViewY)
                    {
                        mHiddenSpace = 0;
                        mInterceptSuper = false;
                    }
                }

                int firstVisiblePosition = getFirstVisiblePosition();
                if (diffY > 0 && firstVisiblePosition == 0)
                {
                    //up to down,is pull down refresh
                    final int pullDown = diffY - mHiddenSpace;
                    int top = pullDown - mRefreshHeight;
                    mRefreshContainer.setPadding(0, top, 0, 0);

                    notifyScrolled(pullDown);

                    if (top >= 0 && mCurrentState != BaseRefreshHeader.STATE_RELEASE_REFRESH)
                    {
                        // release refresh
                        mCurrentState = BaseRefreshHeader.STATE_RELEASE_REFRESH;

                        // state change
                        mRefreshHeader.onRefreshStateChanged(mCurrentState);

                    } else if (top < 0 && mCurrentState != BaseRefreshHeader.STATE_PULL_DOWN)
                    {
                        //pull down
                        mCurrentState = BaseRefreshHeader.STATE_PULL_DOWN;

                        // state change
                        mRefreshHeader.onRefreshStateChanged(mCurrentState);
                    }

                    if (!mInterceptSuper)
                    {
                        super.setOnItemClickListener(null);
                        super.setSelector(new ColorDrawable(Color.TRANSPARENT));
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                // reset
                mDownY = -1;
                mHiddenSpace = -1;
                mInterceptSuper = true;

                Log.d(TAG, "up....");
                //if state is pull refresh,then pull refresh
                if (mCurrentState == BaseRefreshHeader.STATE_PULL_DOWN)
                {
                    //hide the refresh part
                    int top = -mRefreshHeight;
                    mHeaderAnimator = doHeaderAnimator(mRefreshContainer.getPaddingTop(), top);

                } else if (mCurrentState == BaseRefreshHeader.STATE_RELEASE_REFRESH)
                {
                    //if state is release refresh,then refreshing
                    int top = 0;
                    mHeaderAnimator = doHeaderAnimator(mRefreshContainer.getPaddingTop(),
                                                       top,
                                                       new HeaderAnimationListener());
                }
                break;
            default:
                break;
        }

        boolean flag = super.onTouchEvent(ev);

        super.setOnItemClickListener(mOnItemClickListener);
        super.setSelector(mItemSelector);

        return flag;
    }

    private void notifyScrolled(final int pullDown)
    {
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                mRefreshHeader.onRefreshScrolled(pullDown, mRefreshHeader.getRefreshHeight());
            }
        });
    }


    private Animator doHeaderAnimator(int start, int end)
    {
        return doHeaderAnimator(start, end, null);
    }

    private Animator doHeaderAnimator(int start, int end, Animator.AnimatorListener listener)
    {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        long          duration = Math.abs(end - start) * 10;
        if (duration >= getResources().getInteger(android.R.integer.config_mediumAnimTime))
        {

            duration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        }
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                int value = (int) animation.getAnimatedValue();
                mRefreshContainer.setPadding(0, value, 0, 0);

                // notify scrolled
                notifyScrolled(value + mRefreshHeight);
            }
        });
        animator.setInterpolator(new AccelerateInterpolator(1.5f));
        if (listener != null)
        {
            animator.addListener(listener);
        }
        animator.start();
        return animator;
    }

    private class HeaderAnimationListener
            implements Animator.AnimatorListener
    {
        boolean isCanceled = false;

        @Override
        public void onAnimationStart(Animator animation)
        {

        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            if (isCanceled)
            {
                return;
            }
            //state change
            mCurrentState = BaseRefreshHeader.STATE_REFRESHING;
            mRefreshHeader.onRefreshStateChanged(mCurrentState);
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
            isCanceled = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {

        }
    }
}
