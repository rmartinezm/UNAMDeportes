package com.programacion.robertomtz.deportesunam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.LinkedList;

/**
 * Created by rmartinezm on 30/04/2017.
 */

public class AdaptadorDeportes extends BaseAdapter {

    private Context context;
    private LinkedList<Evento> eventos;

    public AdaptadorDeportes(Context context, LinkedList<Evento> eventos){
        this.context = context;
        this.eventos = eventos;
    }

    @Override
    public int getCount() {
        return eventos.size();
    }

    @Override
    public Object getItem(int i) {
        return eventos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.adapter_eventos, null);

        Evento evento = eventos.get(position);

        ImageView imagen = (ImageView) v.findViewById(R.id.adapter_imagen);
        TextView nombre = (TextView) v.findViewById(R.id.adapter_tv_nombre);

        nombre.setText(evento.getName());

        if (!evento.getImagen().isEmpty())
            Glide.with(context)
                    .load(evento.getImagen())
                    .error(R.drawable.image_not_found)
                    .into(imagen);

        return v;
    }

}
