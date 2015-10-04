package org.itheima.refresh.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.itheima.refresh.sample.listview.RefreshListViewCustom;
import org.itheima.refresh.sample.listview.RefreshListViewDefault;

public class ListViewActivity
        extends AppCompatActivity
{

    private String[] datas = new String[]{"Default",
                                          "Custom Header"};
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mListView = (ListView) findViewById(R.id.main_listview);
        mListView.setAdapter(new ArrayAdapter<String>(this,
                                                      android.R.layout.simple_list_item_1,
                                                      datas));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                        intent = new Intent(ListViewActivity.this, RefreshListViewDefault.class);
                        break;
                    case 1:
                        intent = new Intent(ListViewActivity.this, RefreshListViewCustom.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }
}
