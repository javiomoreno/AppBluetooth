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

public class LoginActivity extends AppCompatActivity {

    public static final String PREFS_NAME_CLAVE = "clave";
    Button btnLogin;
    EditText textClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLogin = (Button) findViewById(R.id.btn_login);
        textClave = (EditText) findViewById(R.id.textClave);

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME_CLAVE, 0);
        if(settings.getString("claveAdmin", "null").equals("null")){
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("claveAdmin", "admin123");
            editor.commit();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(textClave.getText().toString().equals(settings.getString("claveAdmin", "null"))) {
                    LoginActivity.this.finish();
                    Intent i = new Intent(LoginActivity.this, HomeAdmin.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Clave Incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_buscarArchivo) {
            irExplorador();
        }

        if (id == R.id.action_buscarDispositivos) {
            irBuscarDispositivos();
        }

        return super.onOptionsItemSelected(item);
    }

    public void irExplorador(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void irBuscarDispositivos(){
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }
}
