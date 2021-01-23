package com.example.findme;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean permission_sms=false;
    RecyclerView list_positions;
    MyRecyclerAdapterPosition adapter;
    static double lat;
    static double lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // traitement permission
        if((ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)){
            permission_sms=true;
        } else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.CALL_PHONE},1);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Ajout.class));
                //MainActivity.this.finish();
            }
        });
        PositionManager pm=new PositionManager(MainActivity.this,"mespositions.db",1);
        ArrayList<Position> data=pm.selectionnertout();
        list_positions=findViewById(R.id.list_positions);
        adapter=new MyRecyclerAdapterPosition(MainActivity.this,data);
        GridLayoutManager lm=new GridLayoutManager(this,1);
        list_positions.setLayoutManager(lm);
        list_positions.setAdapter(adapter);


        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, update text view
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        // Use the location object to get Latitute and Longitude and then update your text view.
                        lat=location.getLatitude();
                        lon=location.getLongitude();
                    }
                }
            }, null);
        }

       /* FusedLocationProviderClient mClient= LocationServices.getFusedLocationProviderClient(this);
        mClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                     lat=location.getLatitude();
                     lon=location.getLongitude();
                }
            }
        });*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        PositionManager pm=new PositionManager(MainActivity.this,"mespositions.db",1);
        ArrayList<Position> data=pm.selectionnertout();
        list_positions=findViewById(R.id.list_positions);
        adapter=new MyRecyclerAdapterPosition(MainActivity.this,data);
        GridLayoutManager manager = new GridLayoutManager(this, 1);

        list_positions.setLayoutManager(manager);
        list_positions.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {

            finishAndRemoveTask();

        }
        else if(id == R.id.action_sendmylocation){
            Intent i=new Intent(MainActivity.this,SendMyLocation.class);
            i.putExtra("sendto","none");
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                permission_sms=true;     }
                else{
                    permission_sms=false;
                }

        }
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("mychannel1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}