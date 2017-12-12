package com.roque.tinder.conexion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.roque.tinder.GlobalesRinder;
import com.roque.tinder.R;

import org.json.JSONException;
import org.json.JSONObject;

public class GustosActivity extends AppCompatActivity {
RadioButton radioRom0,radioRom1,radioGus0,radioGus1,radioPel0,radioPel1,radioMus0,radioMus1,radioLib0,radioLib1;
String gustos="";
int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gustos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        radioLib0=(RadioButton)findViewById(R.id.radioLib0);
        radioLib1=(RadioButton)findViewById(R.id.radioLib1);
        radioMus1=(RadioButton)findViewById(R.id.radioMus1);
        radioMus0=(RadioButton)findViewById(R.id.radioMus0);
        radioPel1=(RadioButton)findViewById(R.id.radioPel1);
        radioPel0=(RadioButton)findViewById(R.id.radioPel0);
        radioGus1=(RadioButton)findViewById(R.id.radioGus1);
        radioGus0=(RadioButton)findViewById(R.id.radioGus0);
        radioRom1=(RadioButton)findViewById(R.id.radioRom1);
        radioRom0=(RadioButton)findViewById(R.id.radioRom0);
        id=getIntent().getIntExtra("id",-1);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Todo fine?", Snackbar.LENGTH_LONG)
                        .setAction("Guardar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                guardarGustos();
                            }
                        }).show();
            }
        });
    }

    private void guardarGustos() {
        gustos="";
        if(radioLib0.isChecked()) gustos+="0";
        else if(radioLib1.isChecked())gustos+="1";
        if(radioMus0.isChecked()) gustos+="0";
        else if(radioMus1.isChecked())gustos+="1";
        if(radioPel0.isChecked()) gustos+="0";
        else if(radioPel1.isChecked())gustos+="1";
        if(radioGus0.isChecked()) gustos+="0";
        else if(radioGus1.isChecked())gustos+="1";
        if(radioRom0.isChecked()) gustos+="0";
        else if(radioRom1.isChecked())gustos+="1";
        Toast.makeText(this,gustos +"\n" +"id: " +id,Toast.LENGTH_SHORT).show();

        JSONObject json = new JSONObject();
        try {
            json.put("id_usuario",id);
            json.put("gustos",gustos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HiloGustos hiloGustos=new HiloGustos();
        hiloGustos.execute(json.toString());

    }
    class HiloGustos extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                return ConexionWS.consumirPut(getString(R.string.dirIpWs)+"agregar_gustos/"
                        , objects[0].toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            finish();
        }
    }
}
