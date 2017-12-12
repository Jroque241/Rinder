package com.roque.tinder.adapters;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.roque.tinder.R;
import com.roque.tinder.conexion.ConexionWS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.graphics.Color.BLUE;

/**
 * Created by Roque on 10/12/2017.
 */

public class AdaptadorPublicaciones extends RecyclerView.Adapter<AdaptadorPublicaciones.MiViewHolder> {
    private ArrayList<?> list;
    private ArrayList lista2;
    private Context context;
    private int layoutResource;
    String m_Text="";
    String nuser;
    boolean click=false;
    public AdaptadorPublicaciones(ArrayList<?> list, Context context, int layoutResource,String nuser) {
        this.list = list;
        this.context = context;
        this.layoutResource = layoutResource;
        this.nuser=nuser;
    }
    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(layoutResource,null);
        MiViewHolder miViewHolder=new MiViewHolder(view);
        return miViewHolder;
    }

    @Override
    public void onBindViewHolder(final MiViewHolder holder, int position) {
        final Publicacion publicacion= (Publicacion) list.get(position);
        holder.tvRowPb.setText(publicacion.getPublicacion());
        holder.tvComentarios.setText(publicacion.getComentarios());
        holder.tvLikes.setText("     "+publicacion.getLikes()+":l     ");
        holder.tvComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarPublicacion(publicacion);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MiViewHolder extends RecyclerView.ViewHolder{
        private TextView tvRowPb,tvComentarios,tvLikes;
        public MiViewHolder(View itemView) {
            super(itemView);
            tvRowPb=(TextView)itemView.findViewById(R.id.tvPub);
            tvComentarios=(TextView)itemView.findViewById(R.id.tvComentarios);
            tvLikes=(TextView)itemView.findViewById(R.id.tvLikes);
        }
    }
    private void agregarPublicacion(final Publicacion publicacion) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Comentar....");

// Set up the input
        final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Comentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                hiloComentar(m_Text,publicacion.getId());

            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Like", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hiloLike(publicacion);
            }
        });

        builder.show();
    }

    private void hiloComentar(String m_text, int idp) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("comentarios",nuser +": " +m_text);
            jsonObject.put("id",idp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HiloComentar  hiloComentar=new HiloComentar();
        hiloComentar.execute(jsonObject.toString());
    }

    private void hiloLike(Publicacion publicacion) {
        JSONObject json=new JSONObject();
        try {
            json.put("id",publicacion.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HiloLike hiloLike=new HiloLike();
        hiloLike.execute(json.toString());
    }


    class HiloLike extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                return ConexionWS.consumirPut(context.getString(R.string.dirIpWs) + "dar_like/"
                        , objects[0].toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            notifyDataSetChanged();
            Toast.makeText(context,"Le diste like!",Toast.LENGTH_SHORT).show();
        }
    }


    class HiloComentar extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                return ConexionWS.consumirPut(context.getString(R.string.dirIpWs) + "comentar/"
                        , objects[0].toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            notifyDataSetChanged();

        }
    }



}
