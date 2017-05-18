package com.programacion.robertomtz.deportesunam;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private LinkedList<Evento> eventos;
    private ListView lvLista;
    // Auxiliar
    private String jsonDeportes = jsonDeportes();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Deportes UNAM");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        eventos = new LinkedList<>();

        AsyncTaskAuxiliar ata = new AsyncTaskAuxiliar();
        ata.execute();

    }

    private void cargaLista(){
        lvLista = (ListView) findViewById(R.id.principal_lv_lista);
        AdaptadorDeportes adaptadorDeportes = new AdaptadorDeportes(this, eventos);
        lvLista.setAdapter(adaptadorDeportes);

        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(PrincipalActivity.this, InfoEventoActivity.class);
                intent.putExtra("evento", eventos.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            moveTaskToBack(true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch (id){
            case R.id.nav_cerrar_sesion:

                new AlertDialog.Builder(this).setMessage(R.string.estas_seguro_de_cerrar_sesion)
                        .setPositiveButton(R.string.cerrar_sesion, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                LoginManager.getInstance().logOut();
                                firebaseAuth.signOut();

                                Intent intent = new Intent(PrincipalActivity.this, InicioActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no_cerrar_sesion, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).show();


                break;
            case R.id.nav_notificaciones:
                break;
            default:
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String jsonDeportes(){
        return "{\n" +
                "  \"Deporte\": [\n" +
                "    {\n" +
                "      \"week\": \"17 octubre - 22 octubre\",\n" +
                "      \"events\": [\n" +
                "        {\n" +
                "          \"name\": \"Burros Blancos vs Pumas UNAM\",\n" +
                "          \"category\": \"Deportes\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"video\": \"https://youtu.be/-SZFXx0_7CI\",\n" +
                "          \"dateUnix\": 1477137600,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/Pumas%20CU.JPG\",\n" +
                "          \"place\": \"Estadio Hidalgo, Pachuca Hidalgo.\",\n" +
                "          \"description\": \"Liga Mayor\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude\": 20.105176,\n" +
                "              \"longitude\": -98.75634\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Linces UVM vs Tigres CCH Sur\",\n" +
                "          \"category\": \"Deportes\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"video\": \"https://youtu.be/-OaPcMIJogo\",\n" +
                "          \"dateUnix\": 1477065600,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/_MG_0139.JPG\",\n" +
                "          \"place\": \"Estadio JOM\",\n" +
                "          \"description\": \"Intermedia juvenil otoÃ±o\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude\": 19.507226,\n" +
                "              \"longitude\": -99.2646\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Acondicionamiento fÃ\u00ADsico general\",\n" +
                "          \"category\": \"Cultura fÃ\u00ADsica\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476687600,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/pista.jpg\",\n" +
                "          \"place\": \"Pista de calentamiento\",\n" +
                "          \"description\": \"Lunes a viernes de 7:00 a 14:00 y 15:30 a 17:00 horas\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude\": 19.3272318,\n" +
                "              \"longitude\": -99.1938109\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Acondicionamiento fÃ\u00ADsico general\",\n" +
                "          \"category\": \"Cultura fÃ\u00ADsica\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476693000,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/fronton.jpg\",\n" +
                "          \"place\": \"Ã\u0081reas verdes del frontÃ³n cerrado\",\n" +
                "          \"description\": \"Lunes a viernes de 8:30 a 16:30 horas\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude\": 19.3248843,\n" +
                "              \"longitude\": -99.1859972\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Acondicionamiento fÃ\u00ADsico general\",\n" +
                "          \"category\": \"Cultura fÃ\u00ADsica\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476714600,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/Atletismo%20de%20la%20UNAM.jpeg\",\n" +
                "          \"place\": \"JardÃ\u00ADn ubicado junto a la Torre de IngenierÃ\u00ADa\",\n" +
                "          \"description\": \"Lunes a viernes de 14:00 a 16:00 horas\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude\": 19.3295835,\n" +
                "              \"longitude\": -99.1822589\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Acondicionamiento fÃ\u00ADsico general\",\n" +
                "          \"category\": \"Cultura fÃ\u00ADsica\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476717300,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/harp.jpg\",\n" +
                "          \"place\": \"Deportivo â€œ C.P. Alfredo Harp HelÃºâ€\u009D\",\n" +
                "          \"description\": \"Lunes a viernes de 15:15 a 16:15 horas\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude\": 19.3109029,\n" +
                "              \"longitude\": -99.174233\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Acondicionamiento rÃ\u00ADtmico aerÃ³bico\",\n" +
                "          \"category\": \"Cultura fÃ\u00ADsica\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476705600,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/Abraham.jpeg\",\n" +
                "          \"place\": \"Lobby FrontÃ³n Cerrado\",\n" +
                "          \"description\": \"Lunes a viernes de 12:00 a 16:00 horas\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude\": 19.3248843,\n" +
                "              \"longitude\": -99.1859972\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Deporteca\",\n" +
                "          \"category\": \"Cultura fÃ\u00ADsica\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476705600,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/pabellon.jpg\",\n" +
                "          \"place\": \"Antiguo PabellÃ³n de Rayos CÃ³smicos\",\n" +
                "          \"description\": \"Lunes a viernes de 12:00 a 18:00 horas\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude\": 19.333948,\n" +
                "              \"longitude\": -99.18118\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Corredor de activaciÃ³n fÃ\u00ADsica\",\n" +
                "          \"category\": \"Cultura fÃ\u00ADsica\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476964800,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/islas.jpg\",\n" +
                "          \"place\": \"Las Islas\",\n" +
                "          \"description\": \"Jueves y viernes de 12:00 a 16:00 horas\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude\": 19.3329456,\n" +
                "              \"longitude\": -99.1851108\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Recorrido en BicitrÃ©n\",\n" +
                "          \"category\": \"Cultura fÃ\u00ADsica\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476707400,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/mural.jpg\",\n" +
                "          \"place\": \"Frente al mural â€œDel Pueblo a la Universidad la Universidad al Puebloâ€\u009D\",\n" +
                "          \"description\": \"Lado sur torre de RectorÃ\u00ADa.\\n\\nLunes y miÃ©rcoles 12:30 y 13:30 horas\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude\": 19.3321207,\n" +
                "              \"longitude\": -99.1882354\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"ActivaciÃ³n fÃ\u00ADsica funcional\",\n" +
                "          \"category\": \"Cultura fÃ\u00ADsica\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476702000,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/alberca.jpg\",\n" +
                "          \"place\": \"Centro Puma FIT Lobby alberca OlÃ\u00ADmpica CU\",\n" +
                "          \"description\": \"Lunes y miÃ©rcoles de 11:00 a 14:00 y 17:00 a 19:00 horas\\n\\nMartes y jueves de 11:00 a 14:00 y 16:00 a 19:00 horas\\n\\nViernes de 11:00 a 14:00 horas\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude:\": 19.3302249,\n" +
                "              \"longitude:\": -99.1850165\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Liga CONNDE-FutbÃ³l AsociaciÃ³n Femenil\",\n" +
                "          \"category\": \"Deportes\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476889200,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/futbol_femenil.jpg\",\n" +
                "          \"place\": \"Campo 1, CU.\",\n" +
                "          \"description\": \"UNAM vs UIA\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude:\": 19.324525,\n" +
                "              \"longitude:\": -99.19423\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Liga CONNDE-FutbÃ³l AsociaciÃ³n Varonil\",\n" +
                "          \"category\": \"Deportes\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476882000,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/varonil.jpg\",\n" +
                "          \"place\": \"Campo 1, CU.\",\n" +
                "          \"description\": \"UNAM vs UIA\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude:\": 19.324525,\n" +
                "              \"longitude:\": -99.19423\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Liga CONDDE-Baloncesto varonil\",\n" +
                "          \"category\": \"Deportes\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476815400,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/basket.JPG\",\n" +
                "          \"place\": \"FrontÃ³n Cerrado, CU.\",\n" +
                "          \"description\": \"UNAM vs U. AnÃ¡huac Sur\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude:\": 19.324884,\n" +
                "              \"longitude:\": -99.185986\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Vendaje Neuromuscular nivel bÃ¡sico\",\n" +
                "          \"category\": \"Cursos y Talleres\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1477731600,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/cecesd.jpg\",\n" +
                "          \"place\": \"Centro de EducaciÃ³n Continua de Estudios Superiores del Deporte\",\n" +
                "          \"description\": \"Dirigido a: MÃ©dicos, fisioterapeutas, enfermeras, entrenadores, preparadores fÃ\u00ADsicos y profesionales a fines.\\n\\nTotal de horas: 10 horas.\\n\\nCierre de inscripciÃ³n: 29 de octubre.\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude:\": 19.329936,\n" +
                "              \"longitude:\": -99.192338\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Beisbol varonil - Ciencias vs ContadurÃ\u00ADa\",\n" +
                "          \"category\": \"Juegos Universitarios\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476709200,\n" +
                "          \"image\": \"http://mobile.unam.mx/app2016/deporte/fotos/El%20beisbol%20Puma%20surge%20en%20la%20dÃƒÂ©cada%20de%20los%2030.JPG\",\n" +
                "          \"place\": \"Parque de CU\",\n" +
                "          \"description\": \"\\\"\\\"\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude:\": 19.325243,\n" +
                "              \"longitude:\": -99.190395\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Beisbol varonil - SelecciÃ³n Juvenil vs EconomÃ\u00ADa\",\n" +
                "          \"category\": \"Juegos Universitarios\",\n" +
                "          \"public\": \"Abierto\",\n" +
                "          \"dateUnix\": 1476716400,\n" +
                "          \"image\": \"http://deporte.unam.mx/noticias/imagenes/2106_213086994.jpg\",\n" +
                "          \"place\": \"Parque de CU\",\n" +
                "          \"description\": \"\\\"\\\"\",\n" +
                "          \"location\": [\n" +
                "            {\n" +
                "              \"latitude:\": 19.325243,\n" +
                "              \"longitude:\": -99.190395\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    private class AsyncTaskAuxiliar extends AsyncTask<Void, Integer, Boolean>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PrincipalActivity.this);
            progressDialog.setMessage("Cargando lista...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                JSONObject object = new JSONObject(jsonDeportes);
                JSONArray deporte = object.optJSONArray("Deporte");
                JSONObject objetoEventos = deporte.getJSONObject(0);
                JSONArray arregloEventos = objetoEventos.getJSONArray("events");

                for (int i = 0; i < arregloEventos.length(); i++)
                    eventos.add(new Evento(arregloEventos.getJSONObject(i)));

            }catch(Exception e){
                return false;
            }

            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            if (aBoolean)
                cargaLista();
            else{
                Intent intent = new Intent(PrincipalActivity.this, InicioActivity.class);
                startActivity(intent);
            }

        }


    }

}
