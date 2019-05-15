package com.example.asus.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity  implements Runnable,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    Handler handler;
    private List<HashMap<String,String>> listItems;//存放文字、图片信息
    private SimpleAdapter listItemAdapter;//配适器
    private static final String TAG = "MyList2Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_list2);

        initListView();
        this.setListAdapter(listItemAdapter);

        //开启线程
        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==7){
                    listItems= (List<HashMap<String,String>>) msg.obj;
                    listItemAdapter = new SimpleAdapter(MyList2Activity.this, listItems,//ListItems数据源
                            R.layout.activity_my_list2, //ListItem的XML布局
                            new String[]{"ItemTitle", "ItemDetail"},//key
                            new int[]{R.id.itemTitle, R.id.itemDetail}
                    );
                    setListAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }
    private void initListView(){
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("ItemTitle", "Rate: " + i);//标题文字
            map.put("ItemDetail", "detail: " + i);//详情描述
            listItems.add(map);
        }
        //生成配适器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems,//ListItems数据源
                R.layout.activity_my_list2, //ListItem的XML布局
                new String[]{"ItemTitle", "ItemDetail"},//key
                new int[]{R.id.itemTitle, R.id.itemDetail}
                );
    }

    @Override
    public void run() {
        List<HashMap<String,String>> retList = new ArrayList<HashMap<String,String>>();

        //获取网络数据，放入list带回到主线程中
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Elements tables = doc.getElementsByTag("table");
//            for(Element table:tables){
  //            Log.i(TAG,"run: table=" + table);
            //       }

            Element table2 = tables.get(1);
//            Log.i(TAG,"run: table2=" + table2);

            Elements tds=table2.getElementsByTag("td");
//            for (Element td:tds){
//                Log.i(TAG, "run: td= "+ td.html());
//            }
            for(int i =0;i<tds.size();i+=8){
                Element td1=tds.get(i);
                Element td6=tds.get(i+5);

                String str1 = td1.text();
                String val = td6.text();

                HashMap<String,String> map = new HashMap<String, String>();
                map.put("ItemTitle",str1);
                map.put("ItemDetail",val);
                retList.add(map);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取msg对象，用于返回主线程
        Message msg =handler.obtainMessage(7);
        // msg.obj="Hello from what()";
        msg.obj=retList;
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onListItemClick((ListView) parent, view, position, id);
        HashMap<String,String> map= (HashMap<String, String>) getListView().getItemAtPosition(position);
        String titlestr=map.get("ItemTitle");
        String detailstr=map.get("ItemDetail");
        Log.i(TAG, "onListItemClick: titlestr=" + titlestr);

        TextView title = getListView().findViewById(R.id.itemTitle);
        TextView detail = getListView().findViewById(R.id.itemDetail);

        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());

        Log.i(TAG, "onListItemClick: title2="+title2);
        Log.i(TAG, "onListItemClick: detail2="+detail2);

        //打开新的页面，传入参数
        Intent rateCalc = new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titlestr);
        rateCalc.putExtra("rate",Float.parseFloat(detailstr));

        startActivity(rateCalc);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: 长按列表项position"+position);

        //构造对话框进行确认
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //删除操作
                listItems.remove(position);
                listItemAdapter.notifyDataSetChanged();
                Log.i(TAG, "onItemLongClick: 已删除");
            }
        })
        .setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}
