package com.roque.tinder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.roque.tinder.adapters.Mensaje;
import com.roque.tinder.adapters.MiAdaptador;
import com.roque.tinder.conexion.ConexionWS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InboxActivity extends AppCompatActivity {
EditText edtMsg;
RecyclerView rcvMsg;
int id=1;
int id_destino;
String foto_origen,foto_destino;
volatile ArrayList<Mensaje> mensajes;
RecibeMensajesTask recibeMensajesTask;
LinearLayoutManager linearmanager;
volatile Boolean isRunning=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtMsg=(EditText)findViewById(R.id.edtEnviarMensaje);
        rcvMsg=(RecyclerView)findViewById(R.id.rcvMsg);
        linearmanager=new LinearLayoutManager(this);
        linearmanager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvMsg.setLayoutManager(linearmanager);
        id=getIntent().getIntExtra("id",0);
        id_destino=getIntent().getIntExtra("id_destino",0);
        foto_origen=getIntent().getStringExtra("foto_origen");
        foto_destino=getIntent().getStringExtra("foto_destino");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStop();
                JSONObject reg=new JSONObject();
                String mensaje=edtMsg.getText().toString();
                DateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date=new Date();
                String fecha_hora= df.format(date);
                try {
                    reg.put("mensaje",mensaje);
                    reg.put("fecha_hora",fecha_hora);
                    reg.put("id_usuario_origen",id);
                    reg.put("id_usuario_destino",id_destino);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

               EnviaMensajesTask enviaMensajesTask=new EnviaMensajesTask();
                  enviaMensajesTask.execute(reg.toString());
                  edtMsg.setText("");




            }
        });

        iniciaHilo();
    }
    public void iniciaHilo(){

        recibeMensajesTask=new RecibeMensajesTask(id,id_destino);
        isRunning=true;
        recibeMensajesTask.execute("");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(!recibeMensajesTask.isCancelled()){
            isRunning=false;
            recibeMensajesTask.cancel(true);
            recibeMensajesTask=null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //Hilo para enviar vensajes
    class EnviaMensajesTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
            return ConexionWS.consumirPut(getString(R.string.dirIpWs)+"agregar_mensaje/"
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
                    iniciaHilo();
                }
                else {
                    throw new Exception("Datos incorrectos!");
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(InboxActivity.this,"",Toast.LENGTH_SHORT).show();
            }

        }
    }

    //Hilo para consumir mensajes
    class RecibeMensajesTask extends AsyncTask{
        int idUsuarioOrigen;
        int idUsuarioDestion;

        public RecibeMensajesTask(int idUsuarioOrigen, int idUsuarioDestion) {
            this.idUsuarioOrigen = idUsuarioOrigen;
            this.idUsuarioDestion = idUsuarioDestion;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String [] keys={"id_usuario_origen","id_usuario_destino"};
            String [] values={"" +idUsuarioOrigen,"" +idUsuarioDestion};
            while(isRunning){
                try {
                    String resultado= ConexionWS.consumirPut(
                            getString(R.string.dirIpWs) +"mostrar_mensajes", keys,values);
                    publishProgress(resultado);
                    Thread.sleep(3000);

                }
                catch (Exception ex){
                    Log.w("HiloRecibeMsg","Error: " +ex);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            try {
                mensajes=new ArrayList<>();
                JSONArray jsonArray=new JSONArray(values[0].toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Mensaje msg=new Mensaje(
                            jsonObject.getInt("id"),
                            jsonObject.getInt("id_usuario_origen"),
                            jsonObject.getInt("id_usuario_destino"),
                            jsonObject.getString("mensaje"),
                            jsonObject.getString("fecha_hora"));
                    mensajes.add(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(mensajes != null){
                if(mensajes.size() > 0 ){
                    MiAdaptador miAdaptador=new MiAdaptador(mensajes,InboxActivity.this,R.layout.row,id,foto_origen,foto_destino);
                    rcvMsg.setAdapter(miAdaptador);
                }
            }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }
}
