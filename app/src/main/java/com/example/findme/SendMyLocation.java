package com.example.findme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMyLocation extends AppCompatActivity {
    EditText ednum,edlong,edlat;
    Button btnsend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_my_location);
        ednum=findViewById(R.id.ed_num_sendmyloc);
        edlong=findViewById(R.id.edlong_sendmyloc);
        edlat=findViewById(R.id.edlat_sendmyloc);
        btnsend=findViewById(R.id.btnvalider_sendmyloc);

        Intent i=this.getIntent();
        Bundle b=i.getExtras();
        String nb=b.getString("sendto");
        //System.out.println(nb);
        if(!nb.equals("none")){
            ednum.setText(nb);
        }
        edlat.setText(MainActivity.lat+"");
        edlong.setText(MainActivity.lon+"");

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=ednum.getText().toString().trim();
                String lon=edlong.getText().toString().trim();
                String lat=edlat.getText().toString().trim();
                if(number.equals("")||lon.equals("")||lat.equals("")){
                    Toast.makeText(SendMyLocation.this,"Empty fields detected",Toast.LENGTH_LONG).show();
                }
                else {
                    SmsManager manager=SmsManager.getDefault();
                    manager.sendTextMessage( number ,
                            null,
                            "Findme#"+MainActivity.lat+"#"+MainActivity.lon,null,null);
                    Toast.makeText(SendMyLocation.this,"Message Sent",Toast.LENGTH_LONG).show();
                }

            }
        });




    }
}