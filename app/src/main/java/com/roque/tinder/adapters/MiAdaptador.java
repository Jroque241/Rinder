package com.roque.tinder.adapters;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Context;
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

import com.roque.tinder.R;

import java.util.ArrayList;

/**
 * Created by Roque on 28/11/2017.
 */

public class MiAdaptador extends RecyclerView.Adapter<MiAdaptador.MiViewHolder>{
    private ArrayList<?> list;
    private Context context;
    private int layoutResource;
    private int origen;
    private String foto_origen,foto_destino;

    public MiAdaptador(ArrayList<?> list, Context context, int layoutResource, int origen,String foto_origen,String foto_destino) {
        this.list = list;
        this.context = context;
        this.layoutResource = layoutResource;
        this.origen = origen;
        this.foto_origen=foto_origen;
        this.foto_destino=foto_destino;
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
        Mensaje mensaje= (Mensaje)list.get(position);
        holder.tvRow.setText(mensaje.getFecha_hora() +": \n" +mensaje.getMensaje());
        if(origen != mensaje.getId_usuario_origen()){
            holder.cardRow.setCardBackgroundColor(Color.parseColor("#FFC0AA"));
            holder.linearRow.setGravity(Gravity.RIGHT);
            //Cambiar de lado la imagen
            Drawable drawable=new BitmapDrawable(context.getResources(),asignarFoto(foto_destino));

           holder.tvRow.setCompoundDrawablesWithIntrinsicBounds(null,null, resize(drawable),null);
        }
        else {
            Drawable drawable=new BitmapDrawable(context.getResources(),asignarFoto(foto_origen));
            holder.tvRow.setCompoundDrawablesWithIntrinsicBounds(resize(drawable),null, null,null);
            holder.cardRow.setCardBackgroundColor(Color.parseColor("#D4856A"));

        }
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 52, 52, true);
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
    private LinearLayout linearRow;
    private CardView cardRow;
    private TextView tvRow;
        public MiViewHolder(View itemView) {
            super(itemView);
            linearRow=(LinearLayout)itemView.findViewById(R.id.linearRow);
            cardRow=(CardView)itemView.findViewById(R.id.cardRow);
            tvRow=(TextView)itemView.findViewById(R.id.tvRow);
        }
    }
}
