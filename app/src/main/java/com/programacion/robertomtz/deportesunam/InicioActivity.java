package com.programacion.robertomtz.deportesunam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InicioActivity extends AppCompatActivity implements View.OnClickListener{

    // Inicio Activity views
    private Intent intent;
    private Button btnInicioSesion;
    private Button btnCrearCuenta;
    private View includeInicio,includeInicioSesion;
    private ActionBar actionBar;
    private boolean flag;

    // Login Activity views
    private LoginButton loginButtonFB;
    private EditText login_etCorreo, login_etPassword;
    private Button btnIngresar;
    private ProgressDialog progressDialog;
    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;
    // Facebook login
    private CallbackManager callbackManager;
    // Auxiliares
    private String correo, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        actionBar = getSupportActionBar();

        includeInicio = findViewById(R.id.fragment_inicio);
        includeInicio.setVisibility(View.VISIBLE);
        includeInicioSesion = findViewById(R.id.fragment_inicio_sesion);
        includeInicioSesion.setVisibility(View.INVISIBLE);
        flag = false;

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

                includeInicioSesion.setVisibility(View.VISIBLE);
                includeInicio.setVisibility(View.INVISIBLE);

                inicializaVariablesLogin();

                actionBar.setTitle("Iniciar Sesión");
                flag = true;

                break;
            case R.id.login_btn_ingresar:
                correo = login_etCorreo.getText().toString().trim();
                password = login_etPassword.getText().toString();

                login(correo, password);

            break;

            case R.id.inicio_btn_crear_cuenta:
                intent = new Intent(this, CrearCuentaActivity.class);
                startActivity(intent);
                break;

            default:
                return;
        }
    }

    private void login(String correo, String password) {
        if (correo.isEmpty() || password.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.error_campo_vacio), Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6){
            Toast.makeText(this, getResources().getString(R.string.error_password_longitud), Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage(getResources().getString(R.string.iniciando_sesion));
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    // La cuenta inicio con exito
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.exito_iniciar_sesion), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InicioActivity.this, PrincipalActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else
                    Toast.makeText(InicioActivity.this, getResources().getString(R.string.error_iniciar_sesion), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inicializaVariablesLogin() {
        // Views
        loginButtonFB = (LoginButton) findViewById(R.id.login_loginbtn_fb);
        login_etCorreo = (EditText) findViewById(R.id.login_et_correo);
        login_etCorreo.requestFocus();
        login_etPassword = (EditText) findViewById(R.id.login_et_password);
        btnIngresar = (Button) findViewById(R.id.login_btn_ingresar);
        btnIngresar.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Facebook
        callbackManager = CallbackManager.Factory.create();
        loginButtonFB = (LoginButton) findViewById(R.id.login_loginbtn_fb);
        loginButtonFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog.setMessage(getResources().getString(R.string.iniciando_sesion));
                progressDialog.show();

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(InicioActivity.this, getResources().getString(R.string.error_iniciar_sesion), Toast.LENGTH_SHORT).show();
            }
        });

        // Auxiliares
        correo = "";
        password = "";
    }

    /** Una vez que ingresamos a fecebook tenemos que ingresar a Firebase **/
    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(InicioActivity.this, PrincipalActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                }else
                    Toast.makeText(InicioActivity.this, getResources().getString(R.string.error_iniciar_sesion), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (flag){
            includeInicio.setVisibility(View.VISIBLE);
            includeInicioSesion.setVisibility(View.INVISIBLE);
            flag = false;
            actionBar.setTitle("DeportesUNAM");
        }else
            super.onBackPressed();
    }
}
