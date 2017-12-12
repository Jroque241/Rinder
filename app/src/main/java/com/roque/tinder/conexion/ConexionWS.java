package com.roque.tinder.conexion;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Roque on 21/11/2017.
 */

public class ConexionWS {

    public static String consumirGet(String strURL,String json)throws Exception{
        json=json.replace(" ","%20");
        json=json.replace("{","%7B");
        json=json.replace("}","%7D");

        URL url=new URL(strURL +json);
        HttpURLConnection conexion= (HttpURLConnection) url.openConnection();
        conexion.connect();

        BufferedReader br=new BufferedReader(
                new InputStreamReader(conexion.getInputStream())
        );
        String resultado="";
        String lecture=null;
        while ((lecture=br.readLine()) !=null){
            resultado+=lecture;
        }
        br.close();
        conexion.disconnect();
        return resultado;

    }
    public static String consumirPut(String strURL,String json)throws Exception{
        Uri.Builder builder=new Uri.Builder();

        builder.appendQueryParameter("json",json);

        String queryParam=builder.build().getEncodedQuery();

        URL url=new URL(strURL +"?" +queryParam);

        HttpURLConnection conexion= (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("PUT");
        conexion.connect();
        String resultado=null;
        if(conexion.getResponseCode()==HttpURLConnection.HTTP_OK || conexion.getResponseCode()==HttpURLConnection.HTTP_ACCEPTED){
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(conexion.getInputStream())
            );
            resultado="";
            String lecture=null;
            while ((lecture=br.readLine()) !=null){
                resultado+=lecture;
            }
            br.close();

        }

        conexion.disconnect();
        return resultado;

    }


    public static String consumirPut(String strURL,String[] keys, String[] values)throws Exception{
        Uri.Builder builder=new Uri.Builder();
        for (int i = 0; i < keys.length; i++) {
            builder.appendQueryParameter(keys[i],values[i]);
        }

        String queryParam=builder.build().getEncodedQuery();

        URL url=new URL(strURL +"?" +queryParam);

        HttpURLConnection conexion= (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("PUT");
        conexion.connect();
        String resultado=null;
        if(conexion.getResponseCode()==HttpURLConnection.HTTP_OK || conexion.getResponseCode()==HttpURLConnection.HTTP_ACCEPTED){
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(conexion.getInputStream())
            );
            resultado="";
            String lecture=null;
            while ((lecture=br.readLine()) !=null){
                resultado+=lecture;
            }
            br.close();

        }

        conexion.disconnect();
        return resultado;

    }



}
