package com.example.javiermoreno.appexporerfiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class HomeAdmin extends AppCompatActivity {

    EditText rutaDestino, rutaOrigen, claveActual, nuevaClave, confirmacionClave;
    Button cambiarRutas, cambiaClave;
    public static final String PREFS_NAME = "rutas";
    public static final String PREFS_NAME_CLAVE = "clave";
    SharedPreferences settings, settingsClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = getSharedPreferences(PREFS_NAME, 0);
        settingsClave = getSharedPreferences(PREFS_NAME_CLAVE, 0);

        rutaDestino = (EditText) findViewById(R.id.rutaDestino);
        rutaOrigen = (EditText) findViewById(R.id.rutaOrigen);
        claveActual = (EditText) findViewById(R.id.claveActual);
        nuevaClave = (EditText) findViewById(R.id.nuevaClave);
        confirmacionClave = (EditText) findViewById(R.id.confirmacionClave);

        rutaDestino.setText(settings.getString("rutaDestino", "null"));
        rutaOrigen.setText(settings.getString("rutaOrigen", "null"));


        cambiarRutas = (Button) findViewById(R.id.cambiarRutas);
        cambiarRutas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!rutaOrigen.getText().toString().equals(settings.getString("rutaOrigen", "null"))){
                    File directorioActual = new File(rutaOrigen.getText().toString());
                    File[] listaArchivos = directorioActual.listFiles();
                    if (listaArchivos != null) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("rutaOrigen", rutaOrigen.getText().toString());
                        editor.commit();
                        Toast.makeText(HomeAdmin.this, "Ruta Origen cambiado con Éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        rutaOrigen.setText(settings.getString("rutaOrigen", "null"));
                        Toast.makeText(HomeAdmin.this, "Ruta Origen no Existe", Toast.LENGTH_SHORT).show();
                    }
                }
                if(!rutaDestino.getText().toString().equals(settings.getString("rutaDestino", "null"))){
                    File directorioActual = new File(rutaDestino.getText().toString());
                    File[] listaArchivos = directorioActual.listFiles();
                    if (listaArchivos != null) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("rutaDestino", rutaDestino.getText().toString());
                        editor.commit();
                        Toast.makeText(HomeAdmin.this, "Ruta Destino cambiado con Éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        rutaDestino.setText(settings.getString("rutaDestino", "null"));
                        Toast.makeText(HomeAdmin.this, "Ruta Destino no Existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cambiaClave = (Button) findViewById(R.id.btnCambiarClave);
        cambiaClave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(claveActual.getText().toString().equals("") || nuevaClave.getText().toString().equals("") || confirmacionClave.getText().toString().equals("")){
                    Toast.makeText(HomeAdmin.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }else {
                    if (claveActual.getText().toString().equals(settingsClave.getString("claveAdmin", "null"))) {
                        if (nuevaClave.getText().toString().equals(confirmacionClave.getText().toString())) {
                            SharedPreferences.Editor editor = settingsClave.edit();
                            editor.putString("claveAdmin", nuevaClave.getText().toString());
                            editor.commit();
                            claveActual.setText("");
                            nuevaClave.setText("");
                            confirmacionClave.setText("");
                            Toast.makeText(HomeAdmin.this, "Clave de Administrador cambiada con Éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeAdmin.this, "Confirmación de clave nueva Incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(HomeAdmin.this, "Clave Actual Incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_salir) {
            irSalir();
        }

        return super.onOptionsItemSelected(item);
    }

    public void irSalir(){
        this.finish();
        Intent i = new Intent(HomeAdmin.this, LoginActivity.class);
        startActivity(i);
    }
}
