package com.example.scrollex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.scrollex.model.Subject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ucsm on 10/21/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBName = "Dictionary.db";
    public static final String DBLocatoin = "/data/data/com.example.scrollex/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DBName, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBName).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }


    public Boolean checkWord(String word){
        Boolean check=false;
        openDatabase();
        String query="SELECT * FROM entries where word="+"\'"+word+"\';";

        Log.i("query+++++++++++++",query+"");

        Cursor cursor=mDatabase.rawQuery(query,null);

        Log.i("CURSOR===========",cursor.getCount()+"");

        if (cursor.getCount()>0){
            check=true;
            Log.i("Word-----------------","It is OK");
        }
        else {
            check=false;
            Log.i("Word ------------------", "It is not OK");
        }
        cursor.close();
        closeDatabase();

        return check;
    }
    public List<Subject> getWord(char c){
        String word=c+"";
        Subject subject=null;
        List<Subject> subjectList=new ArrayList<>();
        openDatabase();
        String query="SELECT * FROM entries where word Like "+"\'"+word+"%\';";

        Log.i("query+++++++++++++",query+"");

        Cursor cursor=mDatabase.rawQuery(query,null);

        Log.i("CURSOR===========",cursor.getCount()+"");

        if (cursor.getCount()>0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                subject=new Subject(cursor.getString(0));
                Log.i("Database",cursor.getString(0));
                subjectList.add(subject);
                cursor.moveToNext();
            }
            Log.i("Word-----------------","It is OK");
        }
        else {
            Log.i("Word ------------------", "It is not OK");
        }
        cursor.close();
        closeDatabase();

        return subjectList;
    }

}

