package com.programacion.robertomtz.deportesunam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CrearCuentaActivity extends AppCompatActivity implements View.OnClickListener {

    // Views
    private EditText etCorreo, etPassword, etRepitePassword;
    private Button btnCrearCuenta;
    private ProgressDialog progressDialog;
    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    // Auxiliares
    private String correo, password, repitePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        inicializaVariables();

    }

    private void inicializaVariables() {
        // Views
        etCorreo = (EditText) findViewById(R.id.crear_cuenta_et_correo);
        etCorreo.requestFocus();
        etPassword = (EditText) findViewById(R.id.crear_cuenta_et_password);
        etRepitePassword = (EditText) findViewById(R.id.crear_cuenta_et_repite_password);
        btnCrearCuenta = (Button) findViewById(R.id.crear_cuenta_btn_crear);
        btnCrearCuenta.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        // Auxiliares
        correo = password = repitePassword = "";
        // Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        correo = etCorreo.getText().toString().trim();
        password = etPassword.getText().toString();
        repitePassword = etRepitePassword.getText().toString();

        switch (id){

            case R.id.crear_cuenta_btn_crear:
                // Verificamos los campos
                if (correo.isEmpty() || password.isEmpty() || repitePassword.isEmpty()){
                    Toast.makeText(this, getResources().getString(R.string.error_campo_vacio), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6){
                    Toast.makeText(this, getResources().getString(R.string.error_password_longitud), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(repitePassword)){
                    Toast.makeText(this, getResources().getString(R.string.error_password_no_coinciden), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Los campos son validos, intentamos crear la cuenta
                crearCuenta(correo, password);
                break;

            default:
                return;

        }
    }

    private void crearCuenta(final String correo, final String password) {
        // Mostramos el dialogo de creando cuenta
        progressDialog.setMessage(getResources().getString(R.string.creando_cuenta));
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // Quitamos el dialog, ya que termino la tarea
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    // Cuenta creada exitosamente
                    //Ingresamos con la nueva cuenta.
                    Toast.makeText(CrearCuentaActivity.this, getResources().getString(R.string.exito_crear_cuenta), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CrearCuentaActivity.this, PrincipalActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else
                    Toast.makeText(CrearCuentaActivity.this, getResources().getString(R.string.error_crear_cuenta), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
