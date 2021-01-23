package com.example.findme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class Ajout extends AppCompatActivity {

    TextView numero;
    TextView longitude;
    TextView latitude;
    Button valider;
    Button quitter;
    PositionManager pm;
    boolean permission_gps=false;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout);
        pm=new PositionManager(Ajout.this,"mespositions.db",1);
        progressDialog=new ProgressDialog(Ajout.this);
        progressDialog.setMessage("Ajout Position...");
        progressDialog.create();

        Bundle b=this.getIntent().getExtras();


        numero=findViewById(R.id.position_num);
        longitude=findViewById(R.id.position_long);
        latitude=findViewById(R.id.position_lat);

        valider=findViewById(R.id.btnvalider_ajout);
        //quitter=findViewById(R.id.btnquitter_ajout);
        if (b!=null){
            //if location from sms
            String body=b.getString("CONTENU");
            String t[]=body.split("#");
            String number=b.getString("NUMERO");

            numero.setText(number);
            longitude.setText(t[1]);
            latitude.setText(t[2]);

        }
        else
        {
            //if location locale
            if((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) ){
                permission_gps=true;
            } else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},2);
            }
            //get user phone number and location
            TelephonyManager tMgr = (TelephonyManager)  this.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            numero.setText(mPhoneNumber);
            longitude.setText(MainActivity.lon+"");
            latitude.setText(MainActivity.lat+"");

        }

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * valider les donnees
                 */
                String num=numero.getText().toString();
                //Boolean valid=testNbValidity(num);
                Boolean valid=true;
                if (valid) {
                    progressDialog.show();
                    String longi=longitude.getText().toString();
                    String lat=latitude.getText().toString();
                    pm.inserer(num,longi,lat);

                    //reset fields
                    numero.setText("");
                    longitude.setText("");
                    latitude.setText("");
                    progressDialog.dismiss();
                    //return to main activity
                    Intent i=new Intent(Ajout.this,MainActivity.class);
                    startActivity(i);
                    Ajout.this.finish();
                }
                else{
                    //number not valid
                    Toast.makeText(Ajout.this,"Number not valid",Toast.LENGTH_LONG).show();
                }
                //int _numero = Integer.parseInt(num);

            }
        });

    }

    private boolean testNbValidity(String num) {
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(num);
        if(matcher.matches()){
            return true;
        }
        else{
            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==2){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                permission_gps=true;     }
            else{
                permission_gps=false;
            }

        }
    }


}