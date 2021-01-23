package com.example.findme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;

public class MonadapterPosition extends BaseAdapter {

    Context con;
    ArrayList<Position> data;


    public MonadapterPosition(Context con,ArrayList<Position> data){
        this.con=con;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //creation de view
        LayoutInflater inf=LayoutInflater.from(con);
        CardView element=(CardView)inf.inflate(R.layout.view_position,null);
        //recuperation des composants
        TextView tv_id=element.findViewById(R.id.tv_id_view);
        TextView tv_num=element.findViewById(R.id.tv_num_view);
        TextView tv_long=element.findViewById(R.id.tv_lon_view);
        TextView tv_lat=element.findViewById(R.id.tv_lat_view);
        Button btncall=element.findViewById(R.id.btn_call_view);
        Button btnmap=element.findViewById(R.id.btn_map_view);
        // affectation des contenus
        Position p =data.get(position);
        tv_id.setText(p.id);
        tv_num.setText(p.numero);
        tv_lat.setText(p.latitude);
        tv_long.setText(p.longitude);


        return element;
    }
}
