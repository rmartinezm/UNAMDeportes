package com.programacion.robertomtz.deportesunam;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by rmartinezm on 30/04/2017.
 */

public class Evento implements Serializable{

    private String name;
    private String categoria;
    private String publico;
    private String video;
    private long dateUnix;
    private String imagen;
    private String lugar;
    private String descripcion;

    private double[] ubicacion;

    public Evento(JSONObject jsonObject) {
        try {

            if (jsonObject.has("video"))
                video = jsonObject.getString("video");
            else
                video = "";

            name = jsonObject.getString("name");
            categoria = jsonObject.getString("category");
            publico = jsonObject.getString("public");
            dateUnix = jsonObject.getLong("dateUnix");
            imagen = jsonObject.getString("image");
            lugar = jsonObject.getString("place");
            descripcion = jsonObject.getString("description");

            ubicacion = new double[2];
            JSONArray aux = jsonObject.getJSONArray("location");
            ubicacion[0] = aux.getJSONObject(0).getDouble("latitude");
            ubicacion[1] = aux.getJSONObject(0).getDouble("longitude");

        } catch (Exception e) {}

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPublico() {
        return publico;
    }

    public void setPublico(String publico) {
        this.publico = publico;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public long getDateUnix() {
        return dateUnix;
    }

    public void setDateUnix(long dateUnix) {
        this.dateUnix = dateUnix;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double[] getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(double[] ubicacion) {
        this.ubicacion = ubicacion;
    }
}
