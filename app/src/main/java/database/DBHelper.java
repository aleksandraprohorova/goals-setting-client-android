package database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context contex)
    {
        super(contex, "customDb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = on;");

        db.execSQL("create table if not exists sprints (" +
                "id integer primary key autoincrement," +
                "startDate text," +
                "endDate text" +
                ");");

        db.execSQL("create table if not exists goals ("
                + "id integer primary key autoincrement,"
                + "description text,"
                + "foreign key (id_sprint) references sprints(id)"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
