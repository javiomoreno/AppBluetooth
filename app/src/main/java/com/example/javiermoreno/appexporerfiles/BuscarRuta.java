package com.example.javiermoreno.appexporerfiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BuscarRuta extends AppCompatActivity {

    private TextView tvl;
    private ListView lvl;


    private File file, file2, file3;
    private List<String> myList;
    ArrayAdapter<String> adapter;
    String rutaTmp1;
    String rutaTmp2;
    String cadenaAtras = "";

    String rutaBuscar;
    public static final String PREFS_NAME = "rutas";
    SharedPreferences settings;
    String ruta;
    int con = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_ruta);

        rutaBuscar = getIntent().getStringExtra("ruta");
        settings = getSharedPreferences(PREFS_NAME, 0);
        tvl=(TextView)findViewById(R.id.tv1);

        if(rutaBuscar.equals("origen")){
            rutaTmp1 = settings.getString("rutaOrigen", "null");
            rutaTmp2 = settings.getString("rutaOrigen", "null");
            tvl.setText("Ruta Origen:"+rutaTmp2);

        }
        else if(rutaBuscar.equals("destino")){
            rutaTmp1 = settings.getString("rutaDestino", "null");
            rutaTmp2 = settings.getString("rutaDestino", "null");
            tvl.setText("Ruta Destino:"+rutaTmp2);

        }

        lvl =(ListView)findViewById(R.id.listView);

        file3 = null;

        myList = new ArrayList<String>();

        file = new File( rutaTmp1 ) ;
        File list[] = file.listFiles();

        for( int i=0; i< list.length; i++)
        {
            myList.add( list[i].getName() );
        }
        adapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1 , myList);
        lvl.setAdapter(adapter);

        lvl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File temp_file = new File( file, myList.get( position ) );
                Toast.makeText(BuscarRuta.this, myList.get(position),Toast.LENGTH_LONG);

                if(!cadenaAtras.equals("")){
                    rutaTmp2 = cadenaAtras;
                }

                tvl.setText("Ruta:"+myList.get(position));
                Log.i("AQUI RUTA1 antes",rutaTmp1);
                rutaTmp1 = rutaTmp2;

                Log.i("AQUI RUTA2",rutaTmp2);
                if(rutaTmp1.equals("/")){
                    rutaTmp2 = rutaTmp2+myList.get(position);
                }else{
                    rutaTmp2 = rutaTmp2+"/"+myList.get(position);
                }

                tvl.setText("Ruta:"+rutaTmp2);

                Log.i("ERROR",rutaTmp2);
                Log.i("myList.get(position)",myList.get(position));
                //Log.i("ERROR",rutaTmp2);
                file2 = null;

                file2 = new File( rutaTmp2 ) ;
                File list2[] = file2.listFiles();
                myList.clear();


                for( int i=0; i< list2.length; i++)
                {
                    if (list2[i].isDirectory()) {
                        myList.add( list2[i].getName() );
                    }

                }

                adapter = new ArrayAdapter<String> (BuscarRuta.this, android.R.layout.simple_list_item_1 , myList);
                lvl.setAdapter(adapter);

            }
        });

    }

    public void carpeta_seleccionada(View view){

        SharedPreferences.Editor editor = settings.edit();
        if(rutaBuscar.equals("origen")){
            editor.putString("rutaOrigen", rutaTmp2);
        }
        else if(rutaBuscar.equals("destino")){
            editor.putString("rutaDestino", rutaTmp2);
        }
        editor.commit();

        Toast.makeText(this, "Ruta Seleccionada: "+rutaTmp2, Toast.LENGTH_LONG).show();
        this.finish();
        Intent i = new Intent(this, HomeAdmin.class);
        startActivity(i);
    }

    public void cancelar(View view){
        this.finish();
        Intent i = new Intent(this, HomeAdmin.class);
        startActivity(i);
    }

    public String devuelveCadena(String ruta){
        StringBuffer cadena = new StringBuffer();
        int aco2 = 0;
        int position2 = 0;
        boolean bandera = false;
        Vector myVec = new Vector();
        char[] sCharsNuevo;
        char[] sChars = ruta.toCharArray();
        for(int i = ruta.length()-1; i >=0 ; i--) {
            if(bandera){
                myVec.add(sChars[aco2]);
                aco2++;
            }
            if(sChars[i] == '/'){
                position2 = i;
                break;
            }
        }
        for (int i = 0; i<position2  ; i++ ) {
            myVec.add(sChars[i]);
            cadena =cadena.append(myVec.get(i));
            //sChars[i] = ' ';
        }

        return cadena.toString();
    }

    @Override
    public void onBackPressed() {

        cadenaAtras = devuelveCadena(rutaTmp2);
        rutaTmp2 = cadenaAtras;
        //rutaTmp1 = devuelveCadena(rutaTmp2);

        if(cadenaAtras.equals("")){
            cadenaAtras = "/";
            rutaTmp1 = "/";
        }
        Log.i("ERROR cadenaAtras",cadenaAtras);
        tvl.setText("Ruta:"+cadenaAtras);

        file3 = null;


        file3 = new File( cadenaAtras ) ;
        File list3[] = file3.listFiles();
        myList.clear();

        if(cadenaAtras.equals("/"))
            cadenaAtras = "";

        for( int i=0; i< list3.length; i++)
        {
            if (list3[i].isDirectory()) {
                myList.add( list3[i].getName() );
            }

        }

        adapter = new ArrayAdapter<String> (BuscarRuta.this, android.R.layout.simple_list_item_1 , myList);
        lvl.setAdapter(adapter);

    }


}
