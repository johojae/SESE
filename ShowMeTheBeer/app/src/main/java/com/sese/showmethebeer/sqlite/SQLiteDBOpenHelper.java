package com.sese.showmethebeer.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBOpenHelper extends SQLiteOpenHelper {
    // 나중에 데이터베이스를 변경하려면 버전을 증가시키면 됨.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "show_me_the_beer.db";

    public static final String TABLE_RATING = "rating_table";
    public static final String TABLE_INTERNAL_SETTING = "internal_setting_table";
    public static final String KEY_BEERID = "beerId";
    public static final String KEY_RATING = "rating";
    public static final String KEY_SERVER_IP = "serverIp";
    public static final String KEY_USER_GUIDE_SHOWN_BEFORE = "userGuideShownBefore";

    public SQLiteDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성
        db.execSQL("CREATE TABLE "+ TABLE_RATING + " (" + KEY_BEERID + " TEXT, " + KEY_RATING + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_INTERNAL_SETTING + " (" + KEY_SERVER_IP + " TEXT, " + KEY_USER_GUIDE_SHOWN_BEFORE + " INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 버전이 증가하면 해당 테이블을 삭제하고 다시 생성합니다.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERNAL_SETTING);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
