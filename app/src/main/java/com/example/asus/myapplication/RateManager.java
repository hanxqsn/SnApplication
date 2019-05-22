package com.example.asus.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RateManager {

    private DBHelper dbHelper;
    private String TBNAME;

    public RateManager(Context context){
        dbHelper=new DBHelper(context);
        TBNAME=DBHelper.TB_NAME;
    }

    //新增一行数据
    public void add(RateItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();//可写数据库
        ContentValues values=new ContentValues();
        values.put("curname",item.getCurName());
        values.put("currate",item.getCurRate());
        db.insert(TBNAME,null,values);
        db.close();
    }
    //新增所有数据
    public void addAll(List<RateItem> list){
        SQLiteDatabase db=dbHelper.getWritableDatabase();//可写数据库
        for(RateItem item:list){
            ContentValues values=new ContentValues();
            values.put("curname",item.getCurName());
            values.put("currate",item.getCurRate());
            db.insert(TBNAME,null,values);
        }
        db.close();
    }

    //查询数据
    public RateItem findById(int id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(TBNAME,null,"ID=?",
                new String[]{String.valueOf(id)},null,null,null);//光标
        RateItem rateItem=null;
        //将数据装载到列表中
        if(cursor!=null && cursor.moveToFirst()){
            rateItem=new RateItem();
            rateItem.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            rateItem.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
            rateItem.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
            cursor.close();
        }
        db.close();
        return rateItem;
    }

    //查询所有数据
    public List<RateItem> listAll(){
        List<RateItem> rateList=null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();//只读数据库
        Cursor cursor=db.query(TBNAME,null,null,
                null,null,null,null);//光标
        //将数据装载到列表中
        if(cursor!=null){
            rateList=new ArrayList<RateItem>();
            while (cursor.moveToNext()){//下一行是否有数据
                RateItem item = new RateItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
                item.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
                rateList.add(item);
            }
            cursor.close();
        }
        db.close();
        return rateList;
    }

    //更新数据
    public void update(RateItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();//可写数据库
        ContentValues values=new ContentValues();
        values.put("curname",item.getCurName());
        values.put("currate",item.getCurRate());
        db.update(TBNAME,values,"ID=?",new String[]{String.valueOf(item.getId())});
        db.close();
    }

    //删除一行数据
    public void delete(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();//可写数据库
        db.delete(TBNAME,"ID=?",new String[]{String.valueOf(id)});
        db.close();
    }

    //删除所有数据
    public void deleteAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();//可写数据库
        db.delete(TBNAME,null,null);
        db.close();
    }

}
