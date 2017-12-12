package com.roque.tinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.roque.tinder.conexion.ConexionWS;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
EditText usr,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usr=(EditText)findViewById(R.id.edtUsr);
        pass=(EditText)findViewById(R.id.edtPass);


    }

    public void evtOnClickRegistro(View view) {
        Intent intent=new Intent(this,RegistroUsrActivity.class);
        startActivity(intent);
    }

    public void evtOnClickLogear(View view) {
    if(usr.getText()!= null && pass.getText() != null){
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("usr",usr.getText().toString());
            jsonObject.put("pass",pass.getText().toString());
            //Se crea instancia de LoginTask
            LoginTask hiloLogin=new LoginTask();
            hiloLogin.execute(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this,"Datos incorrectos",Toast.LENGTH_SHORT).show();
        }
    }
    else{
        usr.setError("El usuario no puede ir vacío!");
        pass.setError("La contraseña no puede ir vacía!");
    }


    }
    /*Hilo para consumir servicio de login*/
    class LoginTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                return ConexionWS.consumirGet(getString(R.string.dirIpWs)+"login/"
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
                    Intent intent=new Intent(LoginActivity.this,PrincipalActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                    finish();
                }
                else {
                    throw new Exception("Datos incorrectos!");
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(LoginActivity.this,"Verifique sus datos!",Toast.LENGTH_SHORT).show();
            }

        }
    }

}
