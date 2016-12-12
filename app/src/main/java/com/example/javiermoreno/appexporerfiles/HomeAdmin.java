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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class HomeAdmin extends AppCompatActivity {

    EditText rutaDestino, rutaOrigen, claveActual, nuevaClave, confirmacionClave;
    Button cambiarRutas, cambiaClave, cambiarDispositivo, buscarOrigen, buscarDestino;
    TextView nombreDispositivo, direccionMac;
    public static final String PREFS_NAME = "rutas";
    public static final String PREFS_NAME_CLAVE = "clave";
    public static final String PREFS_NAME_MAC = "MAC";
    SharedPreferences settings, settingsClave, settingsMac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.feduro);

        settings = getSharedPreferences(PREFS_NAME, 0);
        settingsClave = getSharedPreferences(PREFS_NAME_CLAVE, 0);
        settingsMac = getSharedPreferences(PREFS_NAME_MAC, 0);

        rutaDestino = (EditText) findViewById(R.id.rutaDestino);
        rutaOrigen = (EditText) findViewById(R.id.rutaOrigen);
        claveActual = (EditText) findViewById(R.id.claveActual);
        nuevaClave = (EditText) findViewById(R.id.nuevaClave);
        confirmacionClave = (EditText) findViewById(R.id.confirmacionClave);
        nombreDispositivo = (TextView) findViewById(R.id.nombreDispositivo);
        direccionMac = (TextView) findViewById(R.id.direccionMac);

        if(!settings.getString("rutaDestino", "null").equals(settings.getString("rutaDestinoTemp", "null"))){

        }

        rutaDestino.setText(settings.getString("rutaDestino", "null"));
        rutaOrigen.setText(settings.getString("rutaOrigen", "null"));

        nombreDispositivo.setText(settingsMac.getString("nombreDispositivo", "null"));
        direccionMac.setText(settingsMac.getString("direccionMac", "null"));

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

        cambiarDispositivo = (Button) findViewById(R.id.btnCambiarDispositivo);
        cambiarDispositivo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                irBuscarDispositivos();
            }
        });

        buscarOrigen = (Button) findViewById(R.id.btnBuscarOrigen);
        buscarOrigen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                irBuscarRuta("origen");
            }
        });

        buscarDestino = (Button) findViewById(R.id.btnBuscarDestino);
        buscarDestino.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                irBuscarRuta("destino");
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

        if (id == R.id.action_cambiarDispositivo) {
            irBuscarDispositivos();
        }

        return super.onOptionsItemSelected(item);
    }

    public void irSalir(){
        this.finish();
        Intent i = new Intent(HomeAdmin.this, LoginActivity.class);
        startActivity(i);
    }

    public void irBuscarDispositivos(){
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }

    public void irBuscarRuta(String ruta){
        this.finish();
        Intent i = new Intent(this, BuscarRuta.class);
        i.putExtra("ruta", ruta);
        startActivity(i);
    }
}
