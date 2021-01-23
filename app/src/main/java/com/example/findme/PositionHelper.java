package com.example.findme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PositionHelper extends SQLiteOpenHelper {
    public static final String table_pos="Positions";
    public static final String col_id="ID";
    public static final String col_num="Numero";
    public static final String col_longitude="Longitude";
    public static final String col_latitude="Latitude";

    public PositionHelper(@Nullable Context context, @Nullable String name /* file name *.db*/, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version /* version de la base */);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creation des tables lors de creation du fichier
        db.execSQL("create table "+table_pos+" ( "+col_id+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                " "+col_num+" TEXT not null," +
                " "+col_longitude+" TEXT not null," +
                " "+col_latitude+" TEXT not null )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //MAJ lors de la nouvelle version
        db.execSQL("Drop table "+table_pos);
        onCreate(db);
    }
}
