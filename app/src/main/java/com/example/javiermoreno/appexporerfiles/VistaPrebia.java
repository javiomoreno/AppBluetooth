package com.example.javiermoreno.appexporerfiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class VistaPrebia extends AppCompatActivity {
    Button btnBuscar;
    Button btnImprimir;
    TextView txtUbicacion;
    TextView txtNombre;
    TextView txtTexto;
    String ubicacion;
    String nombre;
    private Spinner numberCopias;

    public static final String PREFS_NAME_ARCHIVO = "ARCHIVO";
    SharedPreferences settings;

    public void irExplorador(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void irIniciarSesion(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void irImprimir(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("rutaArchivo", ubicacion);
        editor.putString("cantidadCopias", String.valueOf(numberCopias.getSelectedItem()));
        editor.putString("nombreArchivo", nombre);
        editor.commit();

        FragmentManager fm = getSupportFragmentManager();
        FragmentPrint fragmentPrint = FragmentPrint.newInstance("Imprimir");
        fragmentPrint.show(fm, "fragment_fragment_print");
    }

    private void abrirArchivo(){
        try {
            txtUbicacion.setText(ubicacion);
            txtNombre.setText(nombre);
            File f = new File(ubicacion);
            if(f == null){
                txtTexto.setText("Archivo no soportado");
            }
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String texto = br.readLine();
            String aux = "";
            while (texto != null){
                aux = aux + texto +"\n";
                texto = br.readLine();
            }
            txtTexto.setText(aux);
            br.close();
            btnImprimir.setVisibility(View.VISIBLE);
        }catch (Exception e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.feduro);

        settings = getSharedPreferences(PREFS_NAME_ARCHIVO, 0);

        numberCopias = (Spinner) findViewById(R.id.numeroCopias);

        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        numberCopias.setAdapter(dataAdapter);

        numberCopias.setSelection(1);

        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        btnImprimir = (Button) findViewById(R.id.btnImprimir);
        txtUbicacion = (TextView) findViewById(R.id.textUbicacion);
        txtNombre = (TextView) findViewById(R.id.textNombre);
        txtTexto = (TextView) findViewById(R.id.textTexto);

        btnBuscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                irExplorador();
            }
        });

        try {
            Intent i = getIntent();
            ubicacion = i.getExtras().getString("ubicacion");
            nombre = i.getExtras().getString("nombre");
        } catch (Exception e){

        }

        if (ubicacion != ""){
            abrirArchivo();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vista_prebia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_iniciarSesion) {
            irIniciarSesion();
        }

        if (id == R.id.action_buscarArchivo) {
            irExplorador();
        }

        if (id == R.id.action_imprimirArchivo) {
            irImprimir();
        }

        return super.onOptionsItemSelected(item);
    }

    public void Imprimir(View view){
        irImprimir();
    }
}
