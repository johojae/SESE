package com.sese.showmethebeer.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.zxing.common.StringUtils;
import com.sese.showmethebeer.data.UserBeerInfo;

import java.util.ArrayList;

public class SQLiteManager extends SQLiteDBOpenHelper {

    private SQLiteDatabase db;

    public SQLiteManager(Context context) {
        super(context);
        // 데이터를 쓰고 읽기 위해서 db 열기
        db = getWritableDatabase();
    }


    public boolean saveRating(String beerId, int rate) {
        if (rate < 0) {
            db.execSQL("DELETE FROM " + TABLE_RATING + " WHERE " +  KEY_BEERID + "='" + beerId +"'");
        } else {
            Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_RATING + " WHERE " + KEY_BEERID + "='" + beerId +"'", null);
            if (mCursor.moveToFirst()) { //이미 기존에 존재 하는 경우
                db.execSQL("UPDATE " + TABLE_RATING + " SET " + KEY_RATING + "=" + rate + " WHERE " + KEY_BEERID + "='" + beerId +"'");
            } else {
                db.execSQL("INSERT INTO " + TABLE_RATING + " VALUES('" + beerId + "'," + rate + ")");
            }
            mCursor.close();
        }

        return true;
    }
    
    //invalid한 beerId가 넘어오는 경우 rating값을 -1로 return함
    public int getRating(String beerId) {
        if (beerId == null || beerId.length() == 0) {
            return -1;
        }

        int rating = - 1;

        Cursor mCursor = db.rawQuery("SELECT " + KEY_RATING + " FROM " + TABLE_RATING + " WHERE " + KEY_BEERID + "='" + beerId +"'", null);
        if (mCursor.moveToFirst()) { //기존에 존재하는 경우
            rating =  mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_RATING));
        }
        mCursor.close();

        System.out.println("getRating : " + rating);
        return rating;
    }

    public ArrayList<UserBeerInfo> getUserBeerList() {
        // 테이블의 모든 데이터 선택
        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_RATING, null);
        ArrayList<UserBeerInfo> list = new ArrayList<>();
        if(mCursor.moveToFirst()) {
            do {
                list.add(new UserBeerInfo(mCursor.getString(0), mCursor.getInt(1)));
            } while(mCursor.moveToNext());
        }
        System.out.println("getUserBeerList------------");
        for (int i = 0 ; i < list.size(); i++) {
            UserBeerInfo userBeerInfo = list.get(i);
            System.out.println(userBeerInfo.getBeerId() + " , " + userBeerInfo.getRating());
        }

        mCursor.close();
        return list;
    }

    public boolean checkUserGuideRead() {
        boolean userGuideRead = false;

        Cursor mCursor = db.rawQuery("SELECT " + KEY_USER_GUIDE_SHOWN_BEFORE + " FROM " + TABLE_INTERNAL_SETTING, null);
        if (mCursor.moveToFirst()) { //기존에 존재하는 경우
            userGuideRead =  (mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_USER_GUIDE_SHOWN_BEFORE)) == 1? true : false);
        }
        mCursor.close();

        return userGuideRead;
    }


    public boolean updateUserGuideRead(boolean userGuideRead) {
        int userGuideShownBefore = (userGuideRead? 1 : 0);

        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_INTERNAL_SETTING, null);
        if (mCursor.moveToFirst()) { //이미 기존에 존재하는 경우
            db.execSQL("UPDATE " + TABLE_INTERNAL_SETTING + " SET " + KEY_USER_GUIDE_SHOWN_BEFORE + "=" + userGuideShownBefore);
        } else {
            db.execSQL("INSERT INTO " + TABLE_INTERNAL_SETTING + " VALUES('" + "" + "'," + userGuideShownBefore + ")");
        }
        mCursor.close();
        return true;
    }

/*

    public void insert(int column1, String column2, double column3, String column4) {
        // 데이터 쓰기
        //db.execSQL("INSERT INTO mytable VALUES(" + id + ",'" + column1 + "','" + column2 + "','" + column3 + "','" + column4 + "')");
    }

    public void update(int column1, String column2, double column3, String column4) {
        // 조건에 일치하는 행의 데이터 변경
        //db.execSQL("UPDATE mytable SET column2='" + column2 + "',column3='" + column3 + "',column4='" + column4 + "' WHERE column1=" + column1);
    }

    public void delete(int column1) {
        // 조건에 일치하는 행을 삭제
        //db.execSQL("DELETE FROM mytable WHERE column1=" + column1);
    }
*/
}
