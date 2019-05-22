package com.example.asus.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RateActivity extends AppCompatActivity implements Runnable{

    private static final String TAG = "TAG";
    EditText plin;
    TextView rmb;

    float value;
    float dollarRate=0.1f;
    float euroRate=0.2f;
    float wonRate=500f;
    Handler handler;
    String updateDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        plin=findViewById(R.id.plin);
        rmb=findViewById(R.id.rmb);

        //获取sp里的数据
        SharedPreferences sp=getSharedPreferences("myrate",Activity.MODE_PRIVATE);
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate=sp.getFloat("dollar_rate",0.0f);
        euroRate=sp.getFloat("euro_rate",0.0f);
        wonRate=sp.getFloat("won_rate",0.0f);
        updateDate = sharedPreferences.getString("update_date","");

        //获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr = sdf.format(today);
        Log.i(TAG, "onCreate:sp upDate= "+updateDate);

        //判断时间
        if(!todayStr.equals(updateDate)){
            Log.i(TAG, "onCreate:需要更新 ");
            //开启子线程
            Thread t = new Thread(this);
            t.start();
        }else {
           Log.i(TAG, "onCreate:不需要更新 ");
        }

        //开启线程
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    Bundle bd= (Bundle) msg.obj;
                    dollarRate=bd.getFloat("dollar_rate");
                    euroRate=bd.getFloat("euro_rate");
                    wonRate=bd.getFloat("won_rate");

                    Log.i(TAG, "handleMessage: dollarRate:"+dollarRate);
                    Log.i(TAG, "handleMessage: euroRate:"+euroRate);
                    Log.i(TAG, "handleMessage: wonRate:"+wonRate);

                    //保存更新的日期
                    SharedPreferences sharedPreferences=getSharedPreferences("myrate",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("update_date",todayStr);
                    editor.putFloat("dollar_rate",dollarRate);
                    editor.putFloat("euro_rate",euroRate);
                    editor.putFloat("won_rate",wonRate);
                    editor.apply();//或editor.commit();

                    Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();

                }
                super.handleMessage(msg);
            }
        };

    }

    public void onClick(View btn){
        String str=plin.getText().toString();
        float r=Float.parseFloat(str);
        if(btn.getId()==R.id.dollar){
          value=r*dollarRate;
        }else if (btn.getId()==R.id.euro){
           value=r*euroRate;
        }else{
           value=r*wonRate;
        }
        rmb.setText(""+value);
    }

    public void openOne(View btn){
        resetRate();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1&&resultCode==2){
            Bundle bundle=data.getExtras();
            dollarRate=bundle.getFloat("key_dollar",0.1f);
            euroRate=bundle.getFloat("key_euro",0.2f);
            wonRate=bundle.getFloat("key_won",500f);

            //将新的汇率写到sp里
            SharedPreferences sharedPreferences=getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //菜单

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            resetRate();
        }else if (item.getItemId()==R.id.menu_open_list){
            //打开列表窗口
            Intent list=new Intent(this,RateListActivity.class);
            startActivity(list);
            //测试数据库
//            RateItem item1=new RateItem("aaaa","123");
//            RateManager manager=new RateManager(this);
//            manager.add(item1);
//            manager.add(new RateItem("bbbb","23.5"));
//            Log.i(TAG, "onOptionsItemSelected: 写入数据完毕");
//
//            //查询所有数据
//            List<RateItem> testList= manager.listAll();
//
//            for (RateItem i:testList){
//                Log.i(TAG, "onOptionsItemSelected: 取出数据 [id="+i.getId()+"],Name="+i.getCurName()+",Rate="+i.getCurRate());
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetRate() {
        Intent intent=new Intent(this,Rate2Activity.class);
        intent.putExtra("dollar_rate",dollarRate);
        intent.putExtra("euro_rate",euroRate);
        intent.putExtra("won_rate",wonRate);
        //startActivity(intent);
        startActivityForResult(intent,1);
    }

    @Override
    public void run() {

        Bundle bdl=new Bundle();

        //获取网络数据
        /*URL url= null;
        try {
            url = new URL("http://www.boc.cn/sourcedb/whpj/");
            HttpURLConnection http= (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();//获得一个输入流

            String html=inputStream2String(in);
            Log.i(TAG, "html : "+html);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {//输入输出异常
            e.printStackTrace();
        }*/

        getFromBOC();
        //获取msg对象，用于返回主线程
        Message msg =handler.obtainMessage(5);
//        msg.obj="Hello from what()";
        msg.obj=bdl;
        handler.sendMessage(msg);

    }

    private Bundle getFromBOC() {
        Bundle bdl =  new Bundle() ;
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG,"run:"+doc.title());
            Elements tables = doc.getElementsByTag("table");
//            for(Element table:tables){
//                Log.i(TAG,"run: table=" + table);
//            }

            Element table2 = tables.get(1);
            //Log.i(TAG,"run: table2=" + table2);

            Elements tds=table2.getElementsByTag("td");
//            for (Element td:tds){
//                Log.i(TAG, "run: td= "+ td.html());
//            }
            for(int i =0;i<tds.size();i+=8){
                Element td1=tds.get(i);
                Element td6=tds.get(i+5);
                Log.i(TAG, td1.html()+"==>"+td6.html());
                if ("美元".equals(td1.html())){
                    bdl.putFloat("dollar_rate",100f/Float.parseFloat(td6.html()));
                }else if ("欧元".equals(td1.html())){
                    bdl.putFloat("euro_rate",100f/Float.parseFloat(td6.html()));
                }else if ("韩国元".equals(td1.html())){
                    bdl.putFloat("won_rate",100f/Float.parseFloat(td6.html()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bdl;
    }

    private String inputStream2String (InputStream inputStream) throws IOException {
        final int bufferSize=1024;
        final char[] buffer=new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"UTF-8");
        for(;;){
            int rsz=in.read(buffer,0,buffer.length);
            if(rsz<0){
                break;
            }
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}
