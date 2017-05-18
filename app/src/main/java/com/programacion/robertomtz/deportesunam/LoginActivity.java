package com.programacion.robertomtz.deportesunam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    // Views
    private LoginButton loginButtonFB;
    private EditText etCorreo, etPassword;
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
        setContentView(R.layout.activity_login);

        inicializaVariables();
    }

    private void inicializaVariables() {
        // Views
        loginButtonFB = (LoginButton) findViewById(R.id.login_loginbtn_fb);
        etCorreo = (EditText) findViewById(R.id.login_et_correo);
        etCorreo.requestFocus();
        etPassword = (EditText) findViewById(R.id.login_et_password);
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
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_iniciar_sesion), Toast.LENGTH_SHORT).show();
            }
        });

        // Auxiliares
        correo = "";
        password = "";

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        correo = etCorreo.getText().toString().trim();
        password = etPassword.getText().toString();

        switch (id){

            case R.id.login_btn_ingresar:
                if (correo.isEmpty() || password.isEmpty()){
                    Toast.makeText(this, getResources().getString(R.string.error_campo_vacio), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6){
                    Toast.makeText(this, getResources().getString(R.string.error_password_longitud), Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage(getResources().getString(R.string.iniciando_sesion));
                firebaseAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            // La cuenta inicio con exito
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.exito_iniciar_sesion), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }else
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_iniciar_sesion), Toast.LENGTH_SHORT).show();
                    }
                });

                break;

            default:
                return;
        }
    }

    /** Una vez que ingresamos a fecebook tenemos que ingresar a Firebase **/
    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                }else
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_iniciar_sesion), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

