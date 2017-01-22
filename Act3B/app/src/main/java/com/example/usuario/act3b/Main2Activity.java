package com.example.usuario.act3b;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    TextView tv;
    public static final String PREFS = "My preferences";
    String resulSexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tv=(TextView)findViewById(R.id.textView);

        recogerPreferences();
    }

    public void recogerPreferences(){
        // Recuperamos el objeto de preferencias compartidas
        SharedPreferences mySharedPreferences = getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        // Recuperamos los valores guardados
        String nombre = mySharedPreferences.getString("txtNombre","");
        String DNI = mySharedPreferences.getString("txtDNI","");
        String fecha = mySharedPreferences.getString("txtFecha","");
        boolean rHchecked = mySharedPreferences.getBoolean("rH",false);
        // Dependendo de si el boolean Hombre es true o false añadimos un sexo u otro
        if (rHchecked == true)
            resulSexo="Hombre";
        else
            resulSexo="Mujer";
        // Introducimos los valores recogidos anteriormente en un TextView
        tv.setText("- Nombre ... "+nombre+"\n"+
                   "- DNI .......... "+DNI+"\n"+
                   "- Fecha ...... "+fecha+"\n"+
                   "- Sexo ........ "+resulSexo);

    }
}
