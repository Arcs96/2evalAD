package com.example.usuario.act3a;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAñadir;
    Button btnMostrar;
    EditText edMensaje;
    TextView tvMensaje;

    String FILE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FILE_NAME = "fichero.txt";

        btnAñadir = (Button)findViewById(R.id.btnAñadir);
        btnAñadir.setOnClickListener(this);
        btnMostrar = (Button)findViewById(R.id.btnMostrar);
        btnMostrar.setOnClickListener(this);

        edMensaje = (EditText)findViewById(R.id.edMensaje);
        tvMensaje = (TextView)findViewById(R.id.tvMensaje);

    }

    // Segun el boton en el que hagamos clic nos inicia un motodo u otro
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnAñadir){
            escribirFichero();
        }else if(view.getId()==R.id.btnMostrar){
            leerFichero();
        }
    }

    public void escribirFichero(){
        try
        {
            // Aplicamos las diferentes coverturas de datos
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_APPEND);
            DataOutputStream dos = new DataOutputStream(fos);
            // Escribimos en el fichero lo introducido en el EditText
            dos.writeBytes(edMensaje.getText().toString()+"\n");
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void leerFichero(){
        try
        {
            // De la misma forma aplicamos la cobertura de datos
            FileInputStream fis = openFileInput(FILE_NAME);
            DataInputStream dis = new DataInputStream(fis);
            // Le introducimos la cantidad de bytes a leer
            byte[] buff = new byte[1000];
            // Leemos desde el fichero la cantida de bytes introducida y posteriormente
            // lo introducimos en el TextView
            dis.read(buff);
            tvMensaje.setText(new String(buff));
            fis.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
