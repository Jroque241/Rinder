package com.roque.tinder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.roque.tinder.conexion.ConexionWS;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistroUsrActivity extends AppCompatActivity {
EditText edtRgUsr,edtRgPass,edtRgPassAg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edtRgUsr=(EditText)findViewById(R.id.edtRgUs);
        edtRgPass=(EditText)findViewById(R.id.edtRgPass);
        edtRgPassAg=(EditText)findViewById(R.id.edtRgPassAg);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Registrarme en Rinder", Snackbar.LENGTH_LONG)
                        .setAction("Guardar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               GuardarRegistro();
                            }
                        }).show();
            }
        });
    }
    public boolean coinciden(){
        if(edtRgPass.getText().toString().equalsIgnoreCase(edtRgPassAg.getText().toString())){
            return true;
        }
        else {
            return false;
        }
    }

    public void GuardarRegistro(){

            if (coinciden()) {
                JSONObject json=new JSONObject();
                try {
                    json.put("usr",edtRgUsr.getText().toString());
                    json.put("pass",edtRgPass.getText().toString());
                    json.put("fecha_hora",fecha());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                iniciarHilo(json.toString());

            } else {
                edtRgPass.setError("Las contrasenas no coinciden");
                edtRgPassAg.setError("Las contrasenas no coinciden");
            }


    }
    public String fecha(){
        DateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date=new Date();
        String fecha_hora= df.format(date);
        return fecha_hora;
    }

    public void iniciarHilo(String json){
    HiloRegistro hiloRegistro=new HiloRegistro();
    hiloRegistro.execute(json);

    }


    /*Hilo para consumir servicio de registro*/
    class HiloRegistro extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                return ConexionWS.consumirPut(getString(R.string.dirIpWs)+"registro_usuario/"
                        , objects[0].toString());
            }
            catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                int id= Integer.parseInt(o.toString());
                if (id!=-1){
                    Toast.makeText(RegistroUsrActivity.this,"Usuario Registrado Correctamente",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(RegistroUsrActivity.this,"Ya hay un usuario registrado con ese nombre,seleccion otro",Toast.LENGTH_SHORT).show();
                    throw new Exception("Datos incorrectos!");
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(RegistroUsrActivity.this,"Error en la conexión!" +ex.toString(),Toast.LENGTH_SHORT).show();
            }

        }
    }






}
