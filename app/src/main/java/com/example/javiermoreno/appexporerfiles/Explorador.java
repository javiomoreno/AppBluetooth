package com.example.javiermoreno.appexporerfiles;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Explorador extends ListActivity {

    private List<String> listaNombresArchivos;
    private List<String> listaRutasArchivos;
    private ArrayAdapter<String> adaptador;
    private String directorioRaiz;
    private TextView carpetaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorador);

        carpetaActual = (TextView) findViewById(R.id.rutaActual);
        directorioRaiz = Environment.getExternalStorageDirectory().getPath();
        verArchivosDirectorio(directorioRaiz);
    }

    private void verArchivosDirectorio(String rutaDirectorio){
        carpetaActual.setText("Estas en: "+rutaDirectorio);
        listaNombresArchivos = new ArrayList<String>();
        listaRutasArchivos = new ArrayList<String>();
        File directorioActual = new File(rutaDirectorio);
        File[] listaArchivos = directorioActual.listFiles();

        int x = 0;

        if (!rutaDirectorio.equals(directorioRaiz)){
            listaNombresArchivos.add("../");
            listaRutasArchivos.add(directorioActual.getParent());
            x = 1;
        }

        for (File archivo : listaArchivos){
            listaRutasArchivos.add(archivo.getPath());
        }

        Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);

        for (int i = x; i < listaRutasArchivos.size(); i ++){
            File archivo = new File(listaRutasArchivos.get(i));
            if(archivo.isFile()){
                listaNombresArchivos.add(archivo.getName());
            }else{
                listaNombresArchivos.add("/"+archivo.getName());
            }
        }

        if(listaArchivos.length < 1){
            listaNombresArchivos.add("No hay ningun archivo");
            listaRutasArchivos.add(rutaDirectorio);
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaNombresArchivos);
        setListAdapter(adaptador);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        File archivo = new File(listaRutasArchivos.get(position));

        if(archivo.isFile()) {
            String ubicacion = archivo.getAbsolutePath();
            String nombre = archivo.getName();
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("ubicacion", ubicacion);
            i.putExtra("nombre", nombre);
            startActivity(i);
        }else{
            verArchivosDirectorio(listaRutasArchivos.get(position));
        }
    }

}
