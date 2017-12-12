package com.roque.tinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.roque.tinder.conexion.ConexionWS;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RegistroActivity extends AppCompatActivity {
    int id;
EditText edtNombreUsr,edtMailUser;
DatePicker dtp;
//dtDay,dtMonth,dtYear;
Button btnNuevo;
RadioButton radioHombre,radioMujer;
ImageView imageFotoRegistro;
String imgBase64="";
String opcion="";
    private int PICK_IMAGE_REQUEST = 1;

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        edtNombreUsr=(EditText)findViewById(R.id.edtNombreUsr);
        btnNuevo=(Button)findViewById(R.id.btnNuevo);
        imageFotoRegistro=(ImageView)findViewById(R.id.imvRegistro);
        edtMailUser=(EditText)findViewById(R.id.edtMailUser);
        radioHombre=(RadioButton)findViewById(R.id.radioHombre);
        radioMujer=(RadioButton)findViewById(R.id.radioMujer);
        dtp=(DatePicker)findViewById(R.id.dtP);
        id=getIntent().getIntExtra("id",1);
        opcion=getIntent().getStringExtra("opcion");

        switch (opcion){
            case "cargar": {
                hiloCargar();
                btnNuevo.setActivated(false);
                btnNuevo.setVisibility(View.INVISIBLE);
            }
                break;
            default:break;
        }
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject reg=new JSONObject();
                try {
                    reg.put("nombre",edtNombreUsr.getText().toString());
                    reg.put("correo",edtMailUser.getText().toString());
                    reg.put("fecha_nacimiento",fecha());
                    reg.put("genero",genero());
                    reg.put("id_usuario",id);
                    reg.put("foto",getImgBase64());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            HiloRegistroPerfil hiloRegistroPerfil=new HiloRegistroPerfil();
                hiloRegistroPerfil.execute(reg);
            }
        });
    }
    private String fecha(){
        String fecha="";
        String month="";
        if((dtp.getMonth()+1)<10){
            month="0" +(dtp.getMonth()+1);
        }else{
            month="" +(dtp.getMonth()+1);
        }
        fecha+=dtp.getYear()+"-" +month +"-" +dtp.getDayOfMonth();
        return fecha;
    }
    private String genero(){
        String gender="";
        if(radioHombre.isChecked()){
            gender="M";

        }else if(radioMujer.isChecked()){
            gender="F";
        }
        return gender;
    }
    public void hiloCargar(){
        JSONObject json=new JSONObject();
        try {
            json.put("id_usuario",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HiloCargaPerfil hiloCargaPerfil=new HiloCargaPerfil();
        hiloCargaPerfil.execute(json.toString());
    }

    /*Hilo para consumir servicio de registro*/
    class HiloRegistroPerfil extends AsyncTask {
        private ProgressDialog progress=null;
        protected void onError(Exception ex) {

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            //start the progress dialog
            progress = ProgressDialog.show(RegistroActivity.this, null, "Registrando...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                return ConexionWS.consumirPut(getString(R.string.dirIpWs)+"registro_perfil"
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

                    finish();
                }
                else {
                    throw new Exception("Datos incorrectos!");
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(RegistroActivity.this,"Error en la conexión!" +ex.toString(),Toast.LENGTH_LONG).show();
            }

        }
    }


    /*Hilo para cargar Perfil*/
    class HiloCargaPerfil extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                return ConexionWS.consumirPut(getString(R.string.dirIpWs)+"obtener_perfil"
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
            if(o != null){
                try {
                    JSONObject json=new JSONObject(o.toString());
                    edtNombreUsr.setText(json.getString("nombre").toString());
                    edtMailUser.setText(json.getString("correo"));
                    if(json.getString("genero").equals("M")){
                        radioHombre.setChecked(true);
                        radioMujer.setChecked(false);
                    }
                    else{
                        radioMujer.setChecked(true);
                        radioHombre.setChecked(false);
                    }
                    dtp.setVisibility(View.INVISIBLE);
                   imageFotoRegistro.setImageBitmap(asignarFoto(json.getString("foto")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
            else {
                Toast.makeText(RegistroActivity.this,"hay error",Toast.LENGTH_SHORT).show();
            }

        }

        public Bitmap asignarFoto(String f){
            try {
                byte[] encodeByte=Base64.decode(f,Base64.DEFAULT);
                Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
                return bitmap;
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }







    //Seleccion de imagen
    public void evtOnClickRegistroFoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona la imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 1, stream);
                byte[] b=stream.toByteArray();
                imageFotoRegistro.setImageBitmap(bitmap);
                setImgBase64(Base64.encodeToString(b,Base64.DEFAULT));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this,"No seleccionaste imagen!",Toast.LENGTH_SHORT).show();
        }
    }
public String bitToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b=baos.toByteArray();
        String temp=Base64.encodeToString(b,Base64.DEFAULT);
        return temp;
}




}

