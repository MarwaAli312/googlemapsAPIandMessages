package com.example.findme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Edit extends AppCompatActivity {

    TextView numero;
    TextView longitude;
    TextView latitude;
    Button valider;
    PositionManager pm;
    Position user;

    ProgressDialog progressDialog_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        pm=new PositionManager(Edit.this,"mespositions.db",1);
        numero=findViewById(R.id.position_num_edit);
        longitude=findViewById(R.id.position_long_edit);
        latitude=findViewById(R.id.position_lat_edit);
        valider=findViewById(R.id.btnvalider_edit);

        Intent i=this.getIntent();
        Bundle b=i.getExtras();
        int position =b.getInt("pos");

        user =pm.selectionner(position);
        numero.setText(user.getNumero());
        longitude.setText(user.getLongitude());
        latitude.setText(user.latitude);

        progressDialog_edit=new ProgressDialog(Edit.this);
        progressDialog_edit.setMessage("Modification...");


        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog_edit.show();
                String num=numero.getText().toString();
                //Boolean valid=testNbValidity(num);
                Boolean valid=true;
                if (valid) {
                    String id=user.getId()+"";
                    String longi=longitude.getText().toString();
                    String lat=latitude.getText().toString();
                    try{
                        pm.editer(num,longi,lat,id);
                        progressDialog_edit.dismiss();
                        //return to main activity
                        Intent i=new Intent(Edit.this,MainActivity.class);
                        startActivity(i);
                    }
                   catch (Exception e){
                        //exception
                   }
                }
                else{
                    //number not valid
                    Toast.makeText(Edit.this,"Number not valid",Toast.LENGTH_LONG).show();
                }
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
}