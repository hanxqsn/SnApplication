package com.example.asus.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{

    String data[]={"one","two","three"};
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);

        List<String> list1=new ArrayList<>();
        for (int i =1;i<100;i++){
            list1.add("item"+i);
        }

        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
        setListAdapter(adapter);

        //开启线程
        Thread thread = new Thread(this);
        thread.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==7){
                    List<String> list2= (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
                }
            }
        };
    }

    @Override
    public void run() {
        List<String> retList = new ArrayList<>();

        //获取网络数据，放入list带回到主线程中
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Elements tables = doc.getElementsByTag("table");
//            for(Element table:tables){
//                Log.i(TAG,"run: table=" + table);
//            }

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

                retList.add(str1+"==>"+val);

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
}
