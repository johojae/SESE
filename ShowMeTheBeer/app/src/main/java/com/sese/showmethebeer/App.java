package com.sese.showmethebeer;

import android.app.Application;

import com.sese.showmethebeer.serverConn.ServerManager;
import com.sese.showmethebeer.sqlite.SQLiteManager;

public class App extends Application {
    ServerManager objServerMngr;
    SQLiteManager objSQLiteMngr;

    public App() {
        super();
    }


    public void	onCreate() {
        super.onCreate();
        System.out.println("onCreate");
        objSQLiteMngr = new SQLiteManager(this, getApplicationContext());
        objServerMngr = new ServerManager(this, getApplicationContext());
    }


    public ServerManager getServerMngr() {
        return objServerMngr;
    }
    public SQLiteManager getSQLiteManager() {
        return objSQLiteMngr;
    }
}
