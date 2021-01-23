

package com.example.findme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findme.Position;
import com.example.findme.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class MyRecyclerAdapterPosition extends RecyclerView.Adapter<MyRecyclerAdapterPosition.MyViewHolder>{
    Context con;
    ArrayList<Position> data;
    ProgressDialog progressDialog_delete;




    public MyRecyclerAdapterPosition(Context con, ArrayList<Position> data) {
        this.con = con;
        this.data = data;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creation de view
        LayoutInflater inf=LayoutInflater.from(con);
        //CardView element=(CardView)inf.inflate(R.layout.view_position,null);
        CardView element=(CardView)inf.inflate(R.layout.user_card,null);
        return new MyViewHolder(element);

    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Position p=data.get(position);
        holder.tv_id.setText(String.valueOf(p.id));
        holder.tv_num.setText(p.numero);
        holder.tv_lon.setText("Long: "+p.longitude);
        holder.tv_lat.setText("Lat: "+p.latitude);

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.menu,position);
            }
        });

    }

    private void showPopupMenu(View menu, int position) {

        progressDialog_delete=new ProgressDialog(con);
        progressDialog_delete.setMessage("Suppression...");
        progressDialog_delete.create();
        // inflate menu
        PopupMenu popup = new PopupMenu(menu.getContext(),menu);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_carduser, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();

                switch(id){
                    case R.id.delete_carduser:
                        //menu d'avancement, delete  w ba3d deleted
                        progressDialog_delete.show();
                        PositionManager pm=new PositionManager(con,"mespositions.db",1);
                        try{

                            pm.supprimernumero(data.get(position).numero);
                            data.remove(position);
                            notifyItemRemoved(position);
                            progressDialog_delete.dismiss();
                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }

                        //Toast.makeText(con,"delete pressed",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.update_carduser:
                        //yemchi l ajout
                       // Toast.makeText(con,"update pressed",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(con,Edit.class);
                        i.putExtra("pos",position);
                        con.startActivity(i);
                        //((Activity) con).startActivityForResult(i, UPDATE_CODE);

                        return true;
                    case R.id.sendlocto_carduser:
                        Intent intent=new Intent(con,SendMyLocation.class);
                        intent.putExtra("sendto",data.get(position).getNumero());
                        con.startActivity(intent);
                    default:
                        return false;
                }

            }
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
          TextView tv_id;
          TextView tv_num;
          TextView tv_lon;
          TextView tv_lat;
          Button btncall;
          Button btnmap;
          Button btnsms;
          ImageButton menu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             // recuperation des composantes
             tv_id=itemView.findViewById(R.id.tv_id_view2);
             tv_num=itemView.findViewById(R.id.tv_num_view2);
             tv_lon=itemView.findViewById(R.id.tv_lon_view2);
             tv_lat=itemView.findViewById(R.id.tv_lat_view2);
             btncall=itemView.findViewById(R.id.btn_call_view2);
             btnmap=itemView.findViewById(R.id.btn_map_view2);
             btnsms=itemView.findViewById(R.id.btn_sms_view2);
             menu=itemView.findViewById(R.id.menu_usercard);

             btnsms.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     SmsManager manager=SmsManager.getDefault();
                     int indice=getAdapterPosition();//envoi position de l element selectionnée
                     manager.sendTextMessage(
                             data.get(indice).numero ,null,"Findme#"+MainActivity.lat+"#"+MainActivity.lon,null,null
                     );
                     //data.get(indice).numero
                     //Toast.makeText(con,"SMS",Toast.LENGTH_LONG);

                 }
             });

            btncall.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     int indice=getAdapterPosition();//envoi position de l element selectionnée
                     Intent callIntent = new Intent(Intent.ACTION_CALL);
                     callIntent.setData(Uri.parse("tel:"+data.get(indice).numero));
                     con.startActivity(callIntent);
                     Toast.makeText(con,"appel de "+data.get(indice).numero,Toast.LENGTH_SHORT).show();
                 }
             });

            btnmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int indice=getAdapterPosition();//envoi position de l element selectionnée
                    Intent i=new Intent(con,MapsActivity.class);
                    i.putExtra("NUMERO",data.get(indice).getNumero());
                    i.putExtra("LONG",data.get(indice).getLongitude());
                    i.putExtra("LAT",data.get(indice).getLatitude());
                    con.startActivity(i);
                }
            });

        }
    }

}

