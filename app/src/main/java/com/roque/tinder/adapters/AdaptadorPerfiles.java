package com.roque.tinder.adapters;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roque.tinder.InboxActivity;
import com.roque.tinder.R;

import java.util.ArrayList;

/**
 * Created by Roque on 08/12/2017.
 */

public class AdaptadorPerfiles extends RecyclerView.Adapter<AdaptadorPerfiles.MiViewHolder>  {
    private ArrayList<?> list;
    private Context context;
    private int layoutResource;
    private String fperfil;
private int id_mio;
    public AdaptadorPerfiles(ArrayList<?> list, Context context, int layoutResource,String fperfil,int id_mio) {
        this.list = list;
        this.context = context;
        this.layoutResource = layoutResource;
        this.fperfil=fperfil;
        this.id_mio=id_mio;
    }
    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(layoutResource,null);
        MiViewHolder miViewHolder=new MiViewHolder(view);
        return miViewHolder;
    }

    @Override
    public void onBindViewHolder(MiViewHolder holder, int position) {
        final Perfiles perfiles= (Perfiles) list.get(position);
            Drawable drawable=new BitmapDrawable(context.getResources(),asignarFoto(perfiles.getFoto()));
            holder.tvRow.setCompoundDrawablesWithIntrinsicBounds(resize(drawable),null, null,null);
            holder.tvRow.setText("   " +perfiles.getNombre() +"       ");
            holder.tvRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, InboxActivity.class);
                    intent.putExtra("id",id_mio);
                    intent.putExtra("id_destino",perfiles.getId_usuario());
                    intent.putExtra("foto_origen",fperfil);
                    intent.putExtra("foto_destino",perfiles.getFoto());
                    context.startActivity(intent);
                }
            });

    }
    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 96, 96, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }
    public Bitmap asignarFoto(String f){
        try {
            byte[] encodeByte= Base64.decode(f,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
            return bitmap;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MiViewHolder extends RecyclerView.ViewHolder{
        private TextView tvRow;
        public MiViewHolder(View itemView) {
            super(itemView);
            tvRow=(TextView)itemView.findViewById(R.id.tvRowPerf);
        }
    }



}
