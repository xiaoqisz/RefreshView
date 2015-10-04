package org.itheima.refresh.sample.listview;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.itheima.refresh.library.OnRefreshListener;
import org.itheima.refresh.library.RefreshListView;
import org.itheima.refresh.sample.R;

import java.util.ArrayList;
import java.util.List;

public class RefreshListViewSticky
        extends AppCompatActivity
{
    private static final int[] PICS = new int[]{R.mipmap.pic_1,
                                                R.mipmap.pic_2,
                                                R.mipmap.pic_3,
                                                R.mipmap.pic_4};

    private RefreshListView mListView;

    private List<String>   mDatas;
    private ViewPager      mPager;
    private RefreshAdapter mAdapter;

    private int mTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_sticky);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View view = View.inflate(this, R.layout.top, null);

        // initView
        mListView = (RefreshListView) findViewById(R.id.refresh_list_view);
        mPager = (ViewPager) view.findViewById(R.id.viewpager);
        mListView.addHeaderView(view);

        // initData
        mAdapter = new RefreshAdapter();
        mListView.setAdapter(mAdapter);
        mPager.setAdapter(new PicAdapter());

        loadDatas();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(RefreshListViewSticky.this,
                               "click " + (position - mListView.getHeaderViewsCount()),
                               Toast.LENGTH_SHORT)
                     .show();
            }
        });

        mListView.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(2000);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }


                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                loadDatas();

                                mListView.setRefreshFinish();
                            }
                        });
                    }
                }).start();

            }
        });
    }

    private void loadDatas()
    {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 50; i++)
        {
            mDatas.add("第" + mTime + "次-数据" + i);
        }

        mAdapter.notifyDataSetChanged();

        mTime++;
    }

    private class RefreshAdapter
            extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            if (mDatas != null)
            {
                return mDatas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position)
        {
            if (mDatas != null)
            {
                return mDatas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder = null;
            if (convertView == null)
            {
                convertView = View.inflate(RefreshListViewSticky.this,
                                           R.layout.item_listview,
                                           null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.tvData = (TextView) convertView.findViewById(R.id.item_tv_data);
            } else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            final String data = mDatas.get(position);
            holder.tvData.setText(data);

            return convertView;
        }
    }

    private class ViewHolder
    {
        TextView tvData;
    }

    private class PicAdapter
            extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return PICS.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView iv = new ImageView(RefreshListViewSticky.this);
            iv.setImageResource(PICS[position]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            container.addView(iv);

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    }
}
