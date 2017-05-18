package com.programacion.robertomtz.deportesunam;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class InfoEventoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView nombre, categoria, publico, dateunix, lugar, descripcion;
    private ImageView imagen, ytLogo;
    private Evento evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_evento);

        Bundle bundle = getIntent().getExtras();
        evento = (Evento) bundle.get("evento");

        categoria = (TextView) findViewById(R.id.info_tv_categoria);
        publico = (TextView) findViewById(R.id.info_tv_publico);
        dateunix = (TextView) findViewById(R.id.info_tv_dateunix);
        lugar = (TextView) findViewById(R.id.info_tv_lugar);
        descripcion = (TextView) findViewById(R.id.info_tv_descripcion);
        imagen = (ImageView) findViewById(R.id.info_iv_imagen);
        nombre = (TextView) findViewById(R.id.info_nombre);
        ytLogo = (ImageView) findViewById(R.id.info_iv_yt_logo);

        String cat,pub,dat,lug;
        cat = "Categoría: " + evento.getCategoria();
        pub = "Público: " + evento.getPublico();
        dat = "DateUnix: " + evento.getDateUnix();
        lug = "Lugar: " + evento.getLugar();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Deportes UNAM");

        nombre.setText(evento.getName());
        categoria.setText(cat);
        publico.setText(pub);
        dateunix.setText(dat);
        lugar.setText(lug);
        descripcion.setText(evento.getDescripcion());

        ytLogo.setVisibility(evento.getVideo().isEmpty()? View.INVISIBLE: View.VISIBLE);
        ytLogo.setOnClickListener(this);

        Glide.with(this)
                .load(evento.getImagen())
                .error(R.drawable.image_not_found)
                .into(imagen);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.info_iv_yt_logo:
                if (ytLogo.getVisibility() == View.VISIBLE)
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(evento.getVideo())));

                break;
        }
    }

}
