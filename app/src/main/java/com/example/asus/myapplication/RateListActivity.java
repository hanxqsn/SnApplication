package com.example.asus.myapplication;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{

    String data[]={"one","two","three"};
    Handler handler;
    private String logDate="";
    private final String DATE_SP_KEY="lastRateDateStr";
    String TAG="RateListActivity";

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
                    setListAdapter(adapter);
                }
            }
        };

        SharedPreferences sp=getSharedPreferences("myrate",Context.MODE_PRIVATE);
        logDate=sp.getString(DATE_SP_KEY,"");
        Log.i(TAG, "lastRateDateStr="+logDate);
    }

    @Override
    public void run() {
        List<String> retList = new ArrayList<>();
        //获取当前日期
        String curDateStr=(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Log.i(TAG, "run: curDateStr="+curDateStr+",logDate="+logDate);

        if (curDateStr.equals(logDate)){
            //如果日期相同，则从数据库中获取数据，而不从网络获取数据
            Log.i(TAG, "run: 日期相同，从数据库中获取数据");
            RateManager manager=new RateManager(this);
            for(RateItem item:manager.listAll()){
                retList.add(item.getCurName()+"-->"+item.getCurRate());
            }
        }else {
            Log.i(TAG, "run: 日期不相同，从网络中获取数据");
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
                List<RateItem> rateList=new ArrayList<>();
//            for (Element td:tds){
//                Log.i(TAG, "run: td= "+ td.html());
//            }
                for(int i =0;i<tds.size();i+=8){
                    Element td1=tds.get(i);
                    Element td6=tds.get(i+5);

                    String str1 = td1.text();
                    String val = td6.text();

                    retList.add(str1+"==>"+val);
                    rateList.add(new RateItem(str1,val));
                }

                //数据写入数据库中
                RateManager manager=new RateManager(this);
                manager.deleteAll();
                manager.addAll(rateList);
                //记录更新日期
                SharedPreferences sp = getSharedPreferences("myrate",Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sp.edit();
                edit.putString(DATE_SP_KEY,curDateStr);
                edit.commit();
                Log.i(TAG, "run: 更新日期完成："+curDateStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //获取msg对象，用于返回主线程
        Message msg =handler.obtainMessage(7);
        // msg.obj="Hello from what()";
        msg.obj=retList;
        handler.sendMessage(msg);
    }
}
