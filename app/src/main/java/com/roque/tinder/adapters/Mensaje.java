package com.roque.tinder.adapters;

/**
 * Created by Roque on 28/11/2017.
 */

public class Mensaje {
    private int id, id_usuario_origen, id_usuario_destino;
    private String mensaje, fecha_hora;

    public Mensaje() {
    }

    public Mensaje(int id, int id_usuario_origen, int id_usuario_destino, String mensaje, String fecha_hora) {
        this.id = id;
        this.id_usuario_origen = id_usuario_origen;
        this.id_usuario_destino = id_usuario_destino;
        this.mensaje = mensaje;
        this.fecha_hora = fecha_hora;
    }

    public Mensaje(int id_usuario_origen, int id_usuario_destino, String mensaje, String fecha_hora) {
        this.id_usuario_origen = id_usuario_origen;
        this.id_usuario_destino = id_usuario_destino;
        this.mensaje = mensaje;
        this.fecha_hora = fecha_hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario_origen() {
        return id_usuario_origen;
    }

    public void setId_usuario_origen(int id_usuario_origen) {
        this.id_usuario_origen = id_usuario_origen;
    }

    public int getId_usuario_destino() {
        return id_usuario_destino;
    }

    public void setId_usuario_destino(int id_usuario_destino) {
        this.id_usuario_destino = id_usuario_destino;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }
}
