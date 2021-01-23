package com.example.findme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Selection;

import java.util.ArrayList;

import static com.example.findme.PositionHelper.*;

public class PositionManager {

    Context con;
    private SQLiteDatabase mabase;

    public PositionManager (Context con,String fichier,int v){
        this.con=con;
        ouvrir(fichier,v);
    }

    public void ouvrir(String nomfichier, int version )
    {
        PositionHelper helper=new PositionHelper(con,nomfichier,null,version);
        mabase=helper.getWritableDatabase();
    }

    public long inserer(String num,String longi,String lat){
        long a=-1;
        ArrayList<Position> alldata=selectionnertout();
        int id=-1;
        for (int i=0;i<alldata.size();i++){
            if(alldata.get(i).getNumero().equals(num)){
                id=alldata.get(i).getId();
                break;
            }

        }

        if(id!=-1){
            String s=id+"";
            editer(num,longi,lat,s);
        }
        else{
            ContentValues v=new ContentValues();
            v.put(col_num,num);
            v.put(col_latitude,lat);
            v.put(col_longitude,longi);
            a=mabase.insert(PositionHelper.table_pos,null,v);
        }
        return a;
    }

    public ArrayList<Position> selectionnertout()
    {
        ArrayList<Position> data=new ArrayList<Position>();
        Cursor c=mabase.query(table_pos,new String[]{col_id,col_num,col_longitude,col_latitude},
                 null,
                 null,
                  col_num,
                 null,
                  col_id);
        c.moveToFirst();
        while(!c.isAfterLast()){
        int id=c.getInt(0);
        String num=c.getString(1);
        String longi=c.getString(2);
        String lat=c.getString(3);
        Position p=new Position(id,num,longi,lat);
        data.add(p);
        c.moveToNext();
        }
        return data;
    }


    public Position selectionner(int position){
        ArrayList<Position> allusers=new ArrayList<Position>();
        allusers=selectionnertout();
        return allusers.get(position);
    }


    public long editer(String num,String longi, String lat, String id){
        long a=-1;
        ContentValues v=new ContentValues();
        v.put(col_num,num);
        v.put(col_latitude,lat);
        v.put(col_longitude,longi);
        a=mabase.update(PositionHelper.table_pos, v,col_id+" = ? ",new String[]{id});
        return a;
    }


    public int supprimernumero(String num){
        int a=-1;
        a=mabase.delete(table_pos, col_num +" = ? ",new String[]{num});
        System.out.println(a);
        return a;
    }
}
