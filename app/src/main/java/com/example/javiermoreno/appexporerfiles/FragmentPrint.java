package com.example.javiermoreno.appexporerfiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import datamaxoneil.connection.ConnectionBase;
import datamaxoneil.connection.Connection_Bluetooth;
import datamaxoneil.printer.DocumentDPL;
import datamaxoneil.printer.DocumentEZ;
import datamaxoneil.printer.DocumentLP;
import datamaxoneil.printer.ParametersDPL;
import datamaxoneil.printer.ParametersEZ;

public class FragmentPrint extends DialogFragment {

    public static final String PREFS_NAME_MAC = "MAC";
    SharedPreferences settings;

    public static final String PREFS_NAME_ARCHIVO = "ARCHIVO";
    SharedPreferences settings2;

    private Spinner lenguajePrinter;
    private Button btnPrint;
    private TextView etMAC;
    private TextView nombreDispositivo;
    private TextView nombreArchivo;
    private TextView cantidadCopias;
    private View general;

    //Document and Parameter Objects
    private DocumentEZ docEZ = new DocumentEZ("MF204");
    private DocumentLP docLP = new DocumentLP("!");
    private DocumentDPL docDPL = new DocumentDPL();

    private ParametersEZ paramEZ = new ParametersEZ();
    private ParametersDPL paramDPL = new ParametersDPL();
    byte[] printData = {0};

    ConnectionBase conn = null;
    private String m_printerAddress = "Unknown";

    public FragmentPrint() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static FragmentPrint newInstance(String title) {
        FragmentPrint frag = new FragmentPrint();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        general =  inflater.inflate(R.layout.fragment_fragment_print, container);

        settings = this.getActivity().getSharedPreferences(PREFS_NAME_MAC, 0);
        settings2 = this.getActivity().getSharedPreferences(PREFS_NAME_ARCHIVO, 0);

        lenguajePrinter = (Spinner) general.findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("EZ");
        list.add("LP");
        list.add("DPL");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (general.getContext(), android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        lenguajePrinter.setAdapter(dataAdapter);

        etMAC = (TextView) general.findViewById(R.id.direccionMac);
        etMAC.setText(settings.getString("direccionMac", "null"));

        nombreDispositivo = (TextView) general.findViewById(R.id.nombreDispositivo);
        nombreDispositivo.setText(settings.getString("nombreDispositivo", "null"));

        nombreArchivo = (TextView) general.findViewById(R.id.nombreArchivo);
        nombreArchivo.setText(settings2.getString("rutaArchivo", "null"));

        cantidadCopias = (TextView) general.findViewById(R.id.cantidadCopias);
        cantidadCopias.setText(settings2.getString("cantidadCopias", "null"));

        addListenerOnButton();

        return general;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString("title", "Imprimir");
        getDialog().setTitle(title);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void addListenerOnButton() {

        //lenguajePrinter = (Spinner)getView().findViewById(R.id.spinner);
        //etMAC = (TextView) getView().findViewById(R.id.etMAC);
        btnPrint = (Button) general.findViewById(R.id.button);

        btnPrint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                //guardamos la mac en mprinteraddress
                m_printerAddress = etMAC.getText().toString();

                //  sms("Clic a boton imprimir: " +
                //         "\n" + String.valueOf(lenguajePrinter.getSelectedItem()) + "\n" + "MAC: "+etMAC.getText());

                if (String.valueOf(lenguajePrinter.getSelectedItem()).equals("EZ")) {
                    //sms("le dio a imprimir y EZ");
                    docEZ.clear();

                    //=============ESTO ES UNA PAGINA DE PRUEBA====================================//
                    docEZ.writeText("For Delivery", 1, 200);
                    docEZ.writeText("Customer Code: 00146", 50, 1);
                    docEZ.writeText("Address: Manila", 75, 1);
                    docEZ.writeText("Tin No.: 27987641", 100, 1);
                    docEZ.writeText("Area Code: PN1-0004", 125, 1);
                    docEZ.writeText("Business Style: SUPERMARKET A", 150, 1);

                    docEZ.writeText("PRODUCT CODE  PRODUCT DESCRIPTION         QTY.  Delivr.", 205, 1);
                    docEZ.writeText("------------  --------------------------  ----  -------", 230, 1);
                    docEZ.writeText("    111       Wht Bread Classic 400g       51      51  ", 255, 1);
                    docEZ.writeText("    112       Clsc Wht Bread 600g          77      77  ", 280, 1);
                    docEZ.writeText("    113       Wht Bread Clsc 600g          153     25  ", 305, 1);
                    docEZ.writeText("    121       H Fiber Wheat Bread 600g     144     77  ", 330, 1);
                    docEZ.writeText("    122       H Fiber Wheat Bread 400g     112     36  ", 355, 1);
                    docEZ.writeText("    123       H Calcium Loaf 400g          81      44  ", 380, 1);
                    docEZ.writeText("    211       California Raisin Loaf       107     44  ", 405, 1);
                    docEZ.writeText("    212       Chocolate Chip Loaf          159     102 ", 430, 1);
                    docEZ.writeText("    213       Dbl Delights(Ube & Chse)     99      80  ", 455, 1);
                    docEZ.writeText("    214       Dbl Delights(Choco & Mocha)  167     130 ", 480, 1);
                    docEZ.writeText("    215       Mini Wonder Ube Cheese       171     179 ", 505, 1);
                    docEZ.writeText("    216       Mini Wonder Ube Mocha        179     100 ", 530, 1);
                    docEZ.writeText("  ", 580, 1);
                    printData = docEZ.getDocumentData();
                    //======================================================================//

                    /* IMPIRMIR EL ARCHIVO SELECCIONADO
                    * */
                /*
                    String selectedItem = "RUTAAAAAAAAAAAAAAAAAAAAAAA ARCHIVOOOOOOOOOOOOOOOOOOOOOOOOOOOO";
                    Bitmap anImage = null;
                    //Check if item is an image
                    String[] okFileExtensions = new String[]{".jpg", ".png", ".gif", ".jpeg", ".bmp", ".tif", ".tiff", ".pcx"};
                    for (String extension : okFileExtensions) {
                        if (selectedItem.toLowerCase(Locale.US).endsWith(extension)) {
                            anImage = BitmapFactory.decodeFile(selectedItem);
                            break;
                        }
                    }
                    //selected item is not an image file
                    if (anImage == null) {
                        File file = new File(selectedItem);
                        byte[] readBuffer = new byte[(int) file.length()];
                        InputStream inputStream = null;
                        try {
                            inputStream = new BufferedInputStream(new FileInputStream(file));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            inputStream.read(readBuffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        printData = readBuffer;
                    } else {
                        sms("Processing image...");
                        docLP.clear();
                        docLP.writeImage(anImage, 576);
                        printData = docLP.getDocumentData();
                    }
                    */

                } else if (String.valueOf(lenguajePrinter.getSelectedItem()).equals("LP")) {
                    //sms("le dio a imprimir y LP");
                    docLP.clear();

                    docLP.writeText("                   For Delivery");
                    docLP.writeText(" ");
                    docLP.writeText("Customer Code: 00146");
                    docLP.writeText("Address: Manila");
                    docLP.writeText("Tin No.: 27987641");
                    docLP.writeText("Area Code: PN1-0004");
                    docLP.writeText("Business Style: SUPERMARKET A");
                    docLP.writeText(" ");
                    docLP.writeText("PRODUCT CODE   PRODUCT DESCRIPTION          QTY.  Delivr.");
                    docLP.writeText("------------   --------------------------   ----  -------");
                    docLP.writeText("    111        Wht Bread Classic 400g        51     51   ");
                    docLP.writeText("    112        Clsc Wht Bread 600g           77     77   ");
                    docLP.writeText("    113        Wht Bread Clsc 600g           153    25   ");
                    docLP.writeText("    121        H Fiber Wheat Bread 600g      144    77   ");
                    docLP.writeText("    122        H Fiber Wheat Bread 400g      112    36   ");
                    docLP.writeText("    123        H Calcium Loaf 400g           81     44   ");
                    docLP.writeText("    211        California Raisin Loaf        107    44   ");
                    docLP.writeText("    212        Chocolate Chip Loaf           159    102  ");
                    docLP.writeText("    213        Dbl Delights(Ube & Chse)      99     80   ");
                    docLP.writeText("    214        Dbl Delights(Choco & Mocha)   167    130  ");
                    docLP.writeText("    215        Mini Wonder Ube Cheese        171    79   ");
                    docLP.writeText("    216        Mini Wonder Ube Mocha         179    100  ");
                    docLP.writeText("  ");
                    docLP.writeText("  ");
                    printData = docLP.getDocumentData();

                    /* IMPIRMIR EL ARCHIVO SELECCIONADO
                    * */
                    /*
                    String selectedItem = "RUTAAAAAAAAAAAAAAAAAAAAAAA ARCHIVOOOOOOOOOOOOOOOOOOOOOOOOOOOO";
                    Bitmap anImage = null;
                    //Check if item is an image
                    String[] okFileExtensions =  new String[] {".jpg", ".png", ".gif",".jpeg",".bmp", ".tif", ".tiff",".pcx"};
                    for (String extension : okFileExtensions) {
                        if(selectedItem.toLowerCase().endsWith(extension))
                        {
                            anImage = BitmapFactory.decodeFile(selectedItem);
                            break;
                        }
                    }
                    //selected item is not an image file
                    if (anImage == null)
                    {
                        File file = new File(selectedItem);
                        byte[] readBuffer = new byte[(int)file.length()];
                        InputStream inputStream= new BufferedInputStream(new FileInputStream(file));
                        try {
                            inputStream.read(readBuffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        printData = readBuffer;
                    }
                    else
                    {
                        sms("Processing image..");
                        docLP.writeImage(anImage, 576);
                        printData = docLP.getDocumentData();
                    }
                    */

                } else if (String.valueOf(lenguajePrinter.getSelectedItem()).equals("DPL")) {
                    //sms("le dio a imprimir y DPL");
                    docDPL.clear();

                    docDPL.writeTextInternalBitmapped("Hello World", 1, 5, 5);
                    //write normal ASCII Text Scalable

                    docDPL.writeTextScalable("Hello World", "00", 25, 5);

                    //Test print korean font
                    paramDPL.setIsUnicode(true);
                    paramDPL.setDBSymbolSet(ParametersDPL.DoubleByteSymbolSet.Unicode);
                    paramDPL.setFontHeight(12);
                    paramDPL.setFontWidth(12);
                    docDPL.writeTextScalable("AC00AE370000", "50", 75, 5, paramDPL);
                    printData = docDPL.getDocumentData();



                    /* IMPIRMIR EL ARCHIVO SELECCIONADO
                    * */
                    /*
                    boolean isImage = false;
                    String selectedItem = "RUTAAAAAAAAAAAAAAAAAAAAAAA ARCHIVOOOOOOOOOOOOOOOOOOOOOOOOOOOO";
                    //Check if item is an image
                    String[] okFileExtensions =  new String[] {".jpg", ".png", ".gif",".jpeg",".bmp", ".tif", ".tiff",".pcx"};
                    for (String extension : okFileExtensions) {
                        if(selectedItem.toLowerCase(Locale.US).endsWith(extension))
                        {
                            isImage = true;
                            break;
                        }
                    }
                    //selected item is not an image file
                    if (!isImage)
                    {
                        File file = new File(selectedItem);
                        byte[] readBuffer = new byte[(int)file.length()];
                        InputStream inputStream= new BufferedInputStream(new FileInputStream(file));
                        try {
                            inputStream.read(readBuffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        printData = readBuffer;

                    }
                    else
                    {
                        sms("Processing image..");
                        DocumentDPL.ImageType imgType = DocumentDPL.ImageType.Other;
                        if(selectedItem.endsWith(".pcx")|| selectedItem.endsWith(".PCX"))
                        {
                            imgType = DocumentDPL.ImageType.PCXFlipped_8Bit;
                        }
                        else
                        {
                            imgType = DocumentDPL.ImageType.Other;
                        }
                        docDPL.clear();
                        try {
                            docDPL.writeImage(selectedItem, imgType, 0, 0, paramDPL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        printData = docDPL.getDocumentImageData();
                    }//end else

                    */

                } else if (String.valueOf(lenguajePrinter.getSelectedItem()).equals("ExPCL Line Print")) {
                    sms("no esta soportado");
                } else if (String.valueOf(lenguajePrinter.getSelectedItem()).equals("ExPCL Page Print")) {
                    sms("no esta soportado");
                }

                Hilo objH = new Hilo();
                objH.run();

            }

        });

    }

    public void sms(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }



    class Hilo implements Runnable {


        @Override
        public void run() {
            // TODO Auto-generated method stub
            //Connection
            try {
                //EnableControls(false); //esto es para desactivar el boton de imprimir
                //Reset connection object
                conn = null;
                //====FOR BLUETOOTH CONNECTIONS========//


                //Looper.prepare();
                conn = Connection_Bluetooth.createClient(m_printerAddress);


                sms("Establishing connection..");
                //Open bluetooth socket
                if (!conn.getIsOpen())
                    conn.open();

                //Sends data to printer
                sms("Sending data to printer..");
                conn.write(printData);
                Thread.sleep(1500);
                //signals to close connection
                conn.close();
                sms("Print success.");
                // EnableControls(true);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                //signals to close connection
                if (conn != null)
                    conn.close();
                e.printStackTrace();
                sms("ERROOOOOOOOOOR: " + e.getMessage());
                //sms(e.getMessage(),"Application Error");
                // EnableControls(true);
            }
        }   // run()

    }



}

class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {

        Toast.makeText(parent.getContext(),
                "Selecciono : \n" + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


}
