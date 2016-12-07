package com.example.javiermoreno.appexporerfiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public void irExplorador(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void irBuscarDispositivos(){
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }

    public void irIniciarSesion(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }


    private List<String> listaNombresArchivos;
    private List<String> listaRutasArchivos;
    private ArrayAdapter<String> adaptador;
    private String directorioRaiz;
    private TextView carpetaActual;
    private ListView listView;
    public static final String PREFS_NAME = "rutas";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        carpetaActual = (TextView) findViewById(R.id.rutaActual);
        listView = (ListView) findViewById(R.id.listFiles);



        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if(settings.getString("rutaOrigen", "null").equals("null")){
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("rutaOrigen", Environment.getExternalStorageDirectory().getPath());
            editor.commit();
            directorioRaiz = settings.getString("rutaOrigen", "null");
        }
        else{
            directorioRaiz = settings.getString("rutaOrigen", "null");
        }

        if(settings.getString("rutaDestino", "null").equals("null")){
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("rutaDestino", Environment.getExternalStorageDirectory().getPath());
            editor.commit();
        }
        verArchivosDirectorio(directorioRaiz);
    }

    private void verArchivosDirectorio(String rutaDirectorio) {
        carpetaActual.setText("Estas en: " + rutaDirectorio);
        listaNombresArchivos = new ArrayList<String>();
        listaRutasArchivos = new ArrayList<String>();
        File directorioActual = new File(rutaDirectorio);
        File[] listaArchivos = directorioActual.listFiles();

        int x = 0;

        if (!rutaDirectorio.equals(directorioRaiz)) {
            listaNombresArchivos.add("../");
            listaRutasArchivos.add(directorioActual.getParent());
            x = 1;
        }

        for (File archivo : listaArchivos) {
            listaRutasArchivos.add(archivo.getPath());
        }

        Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);

        for (int i = x; i < listaRutasArchivos.size(); i++) {
            File archivo = new File(listaRutasArchivos.get(i));
            if (archivo.isFile()) {
                listaNombresArchivos.add(archivo.getName());
            } else {
                listaNombresArchivos.add("/" + archivo.getName());
            }
        }

        if (listaArchivos.length < 1) {
            listaNombresArchivos.add("No hay ningun archivo");
            listaRutasArchivos.add(rutaDirectorio);
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaNombresArchivos);
        listView.setAdapter(adaptador);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                File archivo = new File(listaRutasArchivos.get(position));

                if(archivo.isFile()) {
                    if(listaRutasArchivos.get(position).toLowerCase(Locale.US).endsWith(".prm")) {
                        String ubicacion = archivo.getAbsolutePath();
                        String nombre = archivo.getName();
                        Intent i = new Intent(MainActivity.this, VistaPrebia.class);
                        i.putExtra("ubicacion", ubicacion);
                        i.putExtra("nombre", nombre);
                        startActivity(i);
                    }else {
                        Toast.makeText(MainActivity.this, "Formato Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //verArchivosDirectorio(listaRutasArchivos.get(position));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_iniciarSesion) {
            irIniciarSesion();
        }

        if (id == R.id.action_buscarDispositivos) {
            irBuscarDispositivos();
        }

        if (id == R.id.action_buscarArchivo) {
            irExplorador();
        }

        if (id == R.id.action_imprimirArchivo) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart() {
        super.onRestart();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if(settings.getString("rutaOrigen", "null").equals("null")){
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("rutaOrigen", Environment.getExternalStorageDirectory().getPath());
            editor.commit();
            directorioRaiz = settings.getString("rutaOrigen", "null");
        }
        else{
            directorioRaiz = settings.getString("rutaOrigen", "null");
        }

        verArchivosDirectorio(directorioRaiz);
    }
}
