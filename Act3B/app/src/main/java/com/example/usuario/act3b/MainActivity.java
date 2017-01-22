package com.example.usuario.act3b;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    EditText edNombre;
    EditText edDNI;
    EditText edFecha;
    RadioGroup rGroup;
    RadioButton rHombre, rMujer;
    Button btnEnviar;

    public static final String PREFS = "My preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edNombre = (EditText)findViewById(R.id.edNombre);
        edDNI = (EditText)findViewById(R.id.edDNI);
        edFecha = (EditText)findViewById(R.id.edFecha);
        rGroup = (RadioGroup)findViewById(R.id.radioGroup);
        rHombre = (RadioButton)findViewById(R.id.rbMascle);
        rMujer = (RadioButton)findViewById(R.id.rbFemella);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);

                // Creamos o recuperamos el objeto de preferencias compartidas
                SharedPreferences mySharedPreferences = getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
                // Obtenemos un editor para modificar las preferencias
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                // Guardamos nuevos valores
                editor.putString("txtNombre",edNombre.getText().toString());
                editor.putString("txtDNI",edDNI.getText().toString());
                editor.putString("txtFecha",edFecha.getText().toString());
                //Solo nos hace falta saber si uno de los radioButton esta seleccionado
                editor.putBoolean("rH",rHombre.isChecked());
                // Guardamos los cambios
                editor.commit();
                // Lanzamos la activity
                startActivity(intent);
            }
        });


    }
}
