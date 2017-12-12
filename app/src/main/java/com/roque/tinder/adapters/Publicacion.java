package com.roque.tinder.adapters;

/**
 * Created by Roque on 10/12/2017.
 */

public class Publicacion {
    int id,id_usuario,likes;
    String publicacion,comentarios;

    public Publicacion() {
    }

    public Publicacion(int id, int id_usuario, int likes, String publicacion, String comentarios) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.likes = likes;
        this.publicacion = publicacion;
        this.comentarios = comentarios;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(String publicacion) {
        this.publicacion = publicacion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
