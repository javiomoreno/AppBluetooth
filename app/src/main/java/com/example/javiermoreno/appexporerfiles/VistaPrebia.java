package com.example.javiermoreno.appexporerfiles;

import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

        import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileReader;

public class VistaPrebia extends AppCompatActivity {
    Button btnBuscar;
    Button btnImprimir;
    TextView txtUbicacion;
    TextView txtNombre;
    TextView txtTexto;
    String ubicacion;
    String nombre;
    private NumberPicker numberPicker;

    public void irExplorador(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void irBuscarDispositivos(){
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }

    @SuppressWarnings("unused")
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

        numberPicker = (NumberPicker) findViewById(R.id.numeroCopias);

        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
            }
        });

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
}
