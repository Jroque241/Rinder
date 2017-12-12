package com.roque.tinder.adapters;

/**
 * Created by Roque on 08/12/2017.
 */

public class Perfiles {
    private int id_usuario;
    private String nombre;
    private String foto;

    public Perfiles() {
    }

    public Perfiles(int id_usuario, String nombre, String foto) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.foto = foto;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
