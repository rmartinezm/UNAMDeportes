package com.programacion.robertomtz.deportesunam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class InicioActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;
    private Button btnInicioSesion;
    private Button btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btnInicioSesion = (Button) findViewById(R.id.inicio_btn_iniciar_sesion);
        btnCrearCuenta = (Button) findViewById(R.id.inicio_btn_crear_cuenta);

        btnInicioSesion.setOnClickListener(this);
        btnCrearCuenta.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

            case R.id.inicio_btn_iniciar_sesion:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.inicio_btn_crear_cuenta:
                intent = new Intent(this, CrearCuentaActivity.class);
                startActivity(intent);
                break;

            default:
                return;
        }
    }

}
