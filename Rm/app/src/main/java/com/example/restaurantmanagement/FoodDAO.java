package com.example.restaurantmanagement;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FoodDAO {
    SQLiteDatabase db;

    public void foodInsert(FoodHelper helper, FoodVO vo) {
        db=helper.getWritableDatabase();
        String sql="insert into tbl_food(name,tel,address,image,latitude,longitude,keep,description) values("; sql += "'" + vo.getName() + "',";
        sql += "'" + vo.getTel() + "',";
        sql += "'" + vo.getAddress() + "',";
        sql += "'" + vo.getImage() + "',";
        sql += vo.getLatitude() + ",";
        sql += vo.getLongitude() + ","; sql += "0,";
        sql += "'" + vo.getDescription() + "')";
        db.execSQL(sql);
    }

    public List<FoodVO> list(FoodHelper helper, Boolean isKeep) {
        db=helper.getReadableDatabase();
        List<FoodVO> array=new ArrayList<>();
        String sql="select _id,name,tel,address,image,latitude,longitude,keep,description from tbl_food";
        if(isKeep) sql+=" where keep=1";
        sql += " order by _id desc";
        Cursor cursor=db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            FoodVO vo=new FoodVO();
            vo.setId(cursor.getInt(0));
            vo.setName(cursor.getString(1));
            vo.setTel(cursor.getString(2));
            vo.setAddress(cursor.getString(3));
            vo.setImage(cursor.getString(4));
            vo.setLatitude(cursor.getDouble(5));
            vo.setLongitude(cursor.getDouble(6));
            vo.setKeep(cursor.getInt(7));
            vo.setDescription(cursor.getString(8));
            array.add(vo);
        }
        return array;
    }
    public FoodVO foodRead(FoodHelper helper, int id) {
        db=helper.getReadableDatabase();
        FoodVO vo = new FoodVO();
        String sql="select _id,name,tel,address,image,latitude,longitude,keep,description from tbl_food where _id=" + id;
        Cursor cursor=db.rawQuery(sql, null);
        if(cursor.moveToNext()) {
            vo.setId(cursor.getInt(0)); vo.setName(cursor.getString(1));
            vo.setTel(cursor.getString(2));
            vo.setAddress(cursor.getString(3));
            vo.setImage(cursor.getString(4));
            vo.setLatitude(cursor.getDouble(5));
            vo.setLongitude(cursor.getDouble(6)); vo.setKeep(cursor.getInt(7));
            vo.setDescription(cursor.getString(8));
        }
        return vo;
    }

    public void keepUpdate(FoodHelper helper, int id, int i) {

    }
}
