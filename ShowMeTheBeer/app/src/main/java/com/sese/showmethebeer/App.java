package com.sese.showmethebeer;

import android.app.Application;

import com.sese.showmethebeer.sqlite.SQLiteManager;

public class App extends Application {
    SQLiteManager objSQLiteMngr;

    public App() {
        super();
    }


    public void	onCreate() {
        super.onCreate();
        System.out.println("onCreate");
        objSQLiteMngr = new SQLiteManager(getApplicationContext());
    }


    public SQLiteManager getSQLiteManager(){
        return objSQLiteMngr;
    }
}
