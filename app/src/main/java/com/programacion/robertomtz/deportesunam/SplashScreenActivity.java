package com.programacion.robertomtz.deportesunam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    // 3.5 segundos
    private static int SPLASH_TIME_OUT = 3000;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Si el usuario esta logeado no necesitamos que vea de nuevo el SplashScreen
        // y lo mandamos directamente a PrincipalActivity
        firebaseAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                ImageView imageView = (ImageView) findViewById(R.id.splash_imagen);
                Glide.with(SplashScreenActivity.this).load(R.drawable.puma_logo).into(imageView);

                new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent;
                            intent = (user != null)? new Intent(SplashScreenActivity.this, PrincipalActivity.class) :
                                    new Intent(SplashScreenActivity.this, InicioActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
                }
            };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(listener);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
