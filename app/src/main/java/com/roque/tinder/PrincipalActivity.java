package com.roque.tinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.roque.tinder.adapters.AdaptadorPerfiles;
import com.roque.tinder.adapters.AdaptadorPublicaciones;
import com.roque.tinder.adapters.Perfiles;
import com.roque.tinder.adapters.Publicacion;
import com.roque.tinder.conexion.ConexionWS;
import com.roque.tinder.conexion.GustosActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int id=0;
    volatile ArrayList<Perfiles> perfiles;
    volatile ArrayList<Publicacion> publicaciones;
    private String m_Text = "";
    FloatingActionButton fab;
    RecyclerView rcvPerfiles;
    LinearLayoutManager linearmanager;
    String nombreusr="";
    String fperfil;
    String misgustos="";
    boolean perfil=false;
    boolean match=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id=getIntent().getIntExtra("id",-1);
        iniciarHilo();
        iniciarHiloGustos();
        CargaPerfiles cargaPerfiles=new CargaPerfiles();
        cargaPerfiles.execute("");
         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Publicar en rinder", Snackbar.LENGTH_LONG)
                        .setAction("Publicalo!", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                agregarPublicacion();
                            }
                        }).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void agregarPublicacion() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Publicar..");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Publicar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text =nombreusr +": " +input.getText().toString();
                hiloPublicar(m_Text);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void hiloPublicar(String m_text) {
        JSONObject json=new JSONObject();
        try {
            json.put("id_usuario",id);
            json.put("publicacion",m_text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AgregaPublicacion agregaPublicacion=new AgregaPublicacion();
        agregaPublicacion.execute(json.toString());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int idm = item.getItemId();

        if (idm == R.id.perfil) {
            Intent intent=new Intent(PrincipalActivity.this,RegistroActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("opcion","cargar");
            startActivity(intent);
        }
        else if (idm == R.id.inbox) {
            fab.setVisibility(View.INVISIBLE);
            CargaPerfiles cargaPerfiles=new CargaPerfiles();
            cargaPerfiles.execute("");

        } else if (idm == R.id.pareja) {
            fab.setVisibility(View.INVISIBLE);
           CargaMatches cargaMatches=new CargaMatches();
           cargaMatches.execute("");



        } else if (idm == R.id.publica) {
            fab.setVisibility(View.VISIBLE);
            CargaPublicaciones cargaPublicaciones=new CargaPublicaciones();
            cargaPublicaciones.execute("");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void iniciarHilo(){
        JSONObject json=new JSONObject();
        try {
            json.put("id_usuario",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HiloExistePerfil hiloExistePerfil=new HiloExistePerfil();
        hiloExistePerfil.execute(json.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciarHilo();
        iniciarHiloGustos();
        CargaPerfiles cargaPerfiles=new CargaPerfiles();
        cargaPerfiles.execute("");
        rcvPerfiles=(RecyclerView)findViewById(R.id.rcvPrf);
        linearmanager=new LinearLayoutManager(this);
        linearmanager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvPerfiles.setLayoutManager(linearmanager);

    }

    public void iniciarHiloGustos(){
        JSONObject json=new JSONObject();
        try {
            json.put("id_usuario",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HiloExisteGustos hiloExisteGustos=new HiloExisteGustos();
        hiloExisteGustos.execute(json.toString());

    }

    /*Hilo para consumir servicio de registro*/
    class HiloExistePerfil extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                return ConexionWS.consumirPut(getString(R.string.dirIpWs)+"existe_perfil/"
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
                int ids= Integer.parseInt(o.toString());
                if (ids==-1 && perfil==false){
                  alerta();
                  perfil=true;
                }

            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(PrincipalActivity.this,"Error en la conexión!",Toast.LENGTH_SHORT).show();
            }

        }
    }



    /*Hilo para ver si existe registro de gustos*/
    class HiloExisteGustos extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                return ConexionWS.consumirPut(getString(R.string.dirIpWs)+"existe_gustos/"
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
                String gusto= o.toString();
                if (gusto.equalsIgnoreCase("") && match==false){
                    alertaMatch();
                    match=true;
                }
                else
                {
                    misgustos=gusto;
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(PrincipalActivity.this,"Error en la conexión!",Toast.LENGTH_SHORT).show();
            }

        }
    }




    public void alerta(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(PrincipalActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(PrincipalActivity.this);
        }
        builder.setTitle("Alerta..")
                .setMessage("Antes de continuar debes llenar tu perfil!!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(PrincipalActivity.this,RegistroActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("opcion","registrar");
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }



    public void alertaMatch(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(PrincipalActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(PrincipalActivity.this);
        }
        builder.setTitle("Alerta..")
                .setMessage("Antes de continuar debes ingresar tus gustos!!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(PrincipalActivity.this,GustosActivity.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    class CargaPerfiles extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String resultado= ConexionWS.consumirPut(getString(R.string.dirIpWs)+"mostrar_perfiles/"
                        , objects[0].toString());
                publishProgress(resultado);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            try {
                perfiles=new ArrayList<>();
                JSONArray jsonArray=new JSONArray(values[0].toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    if(jsonObject.getInt("id_usuario")!=id) {
                        Perfiles perfiless = new Perfiles(
                                jsonObject.getInt("id_usuario"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("foto"));
                        perfiles.add(perfiless);
                    }
                    else {
                        fperfil=jsonObject.getString("foto");
                        nombreusr=jsonObject.getString("nombre");

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(perfiles != null){
                if(perfiles.size() > 0 ){
                    AdaptadorPerfiles adaptadorPerfiles=new AdaptadorPerfiles(perfiles,PrincipalActivity.this,R.layout.row_perfil,
                            fperfil,id);
                    rcvPerfiles.setAdapter(adaptadorPerfiles);

                }
            }
            else {
                System.out.println("es nulo");
            }

        }
    }

    class  AgregaPublicacion extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                return ConexionWS.consumirPut(getString(R.string.dirIpWs)+ "agregar_publicacion/",objects[0].toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            CargaPublicaciones publicaciones=new CargaPublicaciones();
            publicaciones.execute("");


        }
    }

   public class CargaPublicaciones extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String resultado = ConexionWS.consumirPut(getString(R.string.dirIpWs) + "mostrar_publicaciones/"
                        , objects[0].toString());
                publishProgress(resultado);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            try {
                publicaciones = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(values[0].toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                      Publicacion publicacion = new Publicacion(
                                jsonObject.getInt("id"),
                                jsonObject.getInt("id_usuario"),
                                jsonObject.getInt("likes"),
                                jsonObject.getString("publicacion"),
                                jsonObject.getString("comentarios"));
                   publicaciones.add(publicacion);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (publicaciones != null) {
                if (publicaciones.size() > 0) {
                    AdaptadorPublicaciones adaptadorPublicaciones = new AdaptadorPublicaciones(publicaciones, PrincipalActivity.this
                            , R.layout.row_publicacion, nombreusr);
                    rcvPerfiles.setAdapter(adaptadorPublicaciones);

                }
                else {
                    Toast.makeText(PrincipalActivity.this,"No hay publicaciones! :(",Toast.LENGTH_SHORT).show();
                }
            } else {
                System.out.println("es nulo");
            }
        }


    }










    //Carga matches
    class CargaMatches extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String resultado= ConexionWS.consumirPut(getString(R.string.dirIpWs)+"mostrar_gustos/"
                        , objects[0].toString());
                publishProgress(resultado);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            try {
                perfiles=new ArrayList<>();
                JSONArray jsonArray=new JSONArray(values[0].toString());
                int coincide=jsonArray.length();
                int nohay=0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    if(jsonObject.getInt("id_usuario")!=id) {
                        String gustos=jsonObject.getString("gustos");
                        int matches=0;
                        int aux=0;
                        for (char c:gustos.toCharArray()) {
                            if(c == misgustos.charAt(aux) ){
                            matches+=20;
                            }
                            else{
                                matches+=0;
                                nohay++;
                            }
                            aux++;
                        }
                        switch (matches){
                            case 100:{
                                Perfiles perfiless = new Perfiles(
                                        jsonObject.getInt("id_usuario"),"El mejor para ti es: " +
                                        jsonObject.getString("nombre"),
                                        jsonObject.getString("foto"));
                                perfiles.add(perfiless);
                            }break;
                            case 80:
                            {
                                Perfiles perfiless = new Perfiles(
                                        jsonObject.getInt("id_usuario"),"Hablale a: " +
                                        jsonObject.getString("nombre") +" ,tal vez sale algo",
                                        jsonObject.getString("foto"));
                                perfiles.add(perfiless);
                            }break;
                            case 60:{
                                Perfiles perfiless = new Perfiles(
                                        jsonObject.getInt("id_usuario"),"No pierdes nada hablandole a: "+
                                        jsonObject.getString("nombre") +" ,intentalo",
                                        jsonObject.getString("foto"));
                                perfiles.add(perfiless);
                            }break;
                            case 40:
                            {
                                Perfiles perfiless = new Perfiles(
                                        jsonObject.getInt("id_usuario"),"No garantizo nada con: "+
                                        jsonObject.getString("nombre") +", igual hablale",
                                        jsonObject.getString("foto"));
                                perfiles.add(perfiless);

                            }break;
                            case 20:{
                                Perfiles perfiless = new Perfiles(
                                        jsonObject.getInt("id_usuario"),"Si no es: " +
                                        jsonObject.getString("nombre") +" ,ya comprate un perro",
                                        jsonObject.getString("foto"));
                                perfiles.add(perfiless);

                            }break;
                            case 0:{

                            } break;
                            default:break;
                        }
                    }
                    else {
                        fperfil=jsonObject.getString("foto");
                        nombreusr=jsonObject.getString("nombre");

                    }
                }
                if (coincide==nohay){
                    Toast.makeText(PrincipalActivity.this,"Lo sentimos, no hay nadie para ti",Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(perfiles != null){
                if(perfiles.size() > 0 ){
                    AdaptadorPerfiles adaptadorPerfiles=new AdaptadorPerfiles(perfiles,PrincipalActivity.this,R.layout.row_perfil,
                            fperfil,id);
                    rcvPerfiles.setAdapter(adaptadorPerfiles);

                }
                else{
                    Toast.makeText(PrincipalActivity.this,"Lo sentimos, no hay nadie para ti",Toast.LENGTH_SHORT).show();

                }
            }

            else {
                System.out.println("es nulo");
            }

        }
    }





}
