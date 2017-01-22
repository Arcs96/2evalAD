package com.example.usuario.act3c;

import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private final static String LOGTAG = "android-drive";
    protected static final int REQ_OPEN_FILE = 1002;

    private GoogleApiClient apiClient;

    private Button btnCrearFichero;
    private Button btnLeerFichero;
    private Button btnLeerFicheroAct;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .build();

        btnCrearFichero = (Button)findViewById(R.id.btnCrearFichero);
        btnLeerFichero = (Button)findViewById(R.id.btnLeerFichero);
        tvResultado = (TextView)findViewById(R.id.tvResultado);

        btnCrearFichero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        createFile("prueba1.txt");
                    }
                }.start();
            }
        });

        btnLeerFichero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileWithActivity();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion!", Toast.LENGTH_SHORT).show();
        Log.e(LOGTAG, "OnConnectionFailed: " + connectionResult);
    }

    private void createFile(final String filename) {

        Drive.DriveApi.newDriveContents(apiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        if (result.getStatus().isSuccess()) {

                            writeSampleText(result.getDriveContents());

                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle(filename)
                                    .setMimeType("text/plain")
                                    .build();

                            DriveFolder folder = Drive.DriveApi.getRootFolder(apiClient);

                            folder.createFile(apiClient, changeSet, result.getDriveContents())
                                    .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                        @Override
                                        public void onResult(DriveFolder.DriveFileResult result) {
                                            if (result.getStatus().isSuccess()) {
                                                Log.i(LOGTAG, "Fichero creado con ID = " + result.getDriveFile().getDriveId());
                                            } else {
                                                Log.e(LOGTAG, "Error al crear el fichero");
                                            }
                                        }
                                    });
                        } else {
                            Log.e(LOGTAG, "Error al crear DriveContents");
                        }
                    }
                });
    }

    private void writeSampleText(DriveContents driveContents) {
        OutputStream outputStream = driveContents.getOutputStream();
        Writer writer = new OutputStreamWriter(outputStream);

        try {
            writer.write("Esto es un texto de prueba!");
            writer.close();
        } catch (IOException e) {
            Log.e(LOGTAG, "Error al escribir en el fichero: " + e.getMessage());
        }
    }

    private void readFile(DriveId fileDriveId) {

        DriveFile file = fileDriveId.asDriveFile();

        file.open(apiClient, DriveFile.MODE_READ_ONLY, null)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Log.e(LOGTAG,"Error al abrir fichero (readFile)");
                            return;
                        }

                        DriveContents contents = result.getDriveContents();

                        BufferedReader reader =
                                new BufferedReader(
                                        new InputStreamReader(contents.getInputStream()));

                        StringBuilder builder = new StringBuilder();

                        try {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                builder.append(line);
                            }
                        } catch (IOException e) {
                            Log.e(LOGTAG,"Error al leer fichero");
                        }

                        contents.discard(apiClient);
                        tvResultado.setText(builder.toString());
                        Log.i(LOGTAG, "Fichero leido: " + builder.toString());
                    }
                });
    }

    private void openFileWithActivity() {

        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[] { "text/plain" })
                .build(apiClient);

        try {
            startIntentSenderForResult(
                    intentSender, REQ_OPEN_FILE, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.e(LOGTAG, "Error al iniciar actividad: Open File", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //...
            case REQ_OPEN_FILE:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    Log.i(LOGTAG, "Fichero seleccionado ID = " + driveId);

                    readFile(driveId);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

}
