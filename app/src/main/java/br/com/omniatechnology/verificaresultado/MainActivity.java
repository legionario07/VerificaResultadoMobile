package br.com.omniatechnology.verificaresultado;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileStore;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int READ_REQUEST_CODE = 42;
    private static final int WRITE_REQUEST_CODE = 43;
    private String numerosSorteados;
    private Integer totalValoresPremiados;
    private String value;
    private TextView txtResultado;

    private Button btnIniciar;
    private Button btnSaveToFile;
    private List<Resultado> resultados;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnIniciar = findViewById(R.id.btnIniciar);
        btnSaveToFile = findViewById(R.id.btnSaveToFile);
        txtResultado = findViewById(R.id.txtResultado);

        btnIniciar.setOnClickListener(this);
        btnSaveToFile.setOnClickListener(this);
        FloatingActionButton fab = findViewById(R.id.fabRenew);
        fab.setOnClickListener(this);

    }

    private void refatorarLayout(float svValue, float llValue){

        LinearLayout ll = findViewById(R.id.llButton);
        ScrollView sv = findViewById(R.id.svText);

        LinearLayout.LayoutParams paramSV = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                svValue
        );

        sv.setLayoutParams(paramSV);

        LinearLayout.LayoutParams paramLL = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                llValue
        );
        ll.setLayoutParams(paramLL);
    }

    private void showDialog(String title, final Integer id) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                value = input.getText().toString().trim();
                if (value == null || value.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Valor não pode ser vazio", Toast.LENGTH_LONG).show();
                    return;
                }

                switch (id) {
                    case R.id.btnIniciar:
                        numerosSorteados = value;

                        showDialog("Acerto mínimo para premiar: Ex(Mega = 4, LotoFácil = 11)", 2);

                        break;

                    case 2:

                        totalValoresPremiados = Integer.valueOf(value);

                        Toast toast = Toast.makeText(getApplicationContext(), "Selecione o arquivo txt com os jogos", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("text/plain");
                        startActivityForResult(intent, READ_REQUEST_CODE);

                        break;
                }


                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));

        resultados = VerificaJogo.verificar(reader, numerosSorteados, totalValoresPremiados);

        if(resultados==null){
            txtResultado.setText("Erro ao Verificar Resultados");
            return "";
        }

        inputStream.close();

        String retorno = VerificaJogo.gerarString(resultados);

        if (retorno != null) {
            txtResultado.setText(retorno);
            btnIniciar.setVisibility(View.GONE);
            btnSaveToFile.setVisibility(View.VISIBLE);

            refatorarLayout(9, 1);

        } else {
            txtResultado.setText("Erro ao Verificar Resultados");
            return "";
        }

        return retorno;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                try {
                    readTextFromUri(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }
            try {
                ParcelFileDescriptor pfd = this.getContentResolver().
                        openFileDescriptor(uri, "w");
                FileOutputStream fileOutputStream =
                        new FileOutputStream(pfd.getFileDescriptor());

                fileOutputStream.write((txtResultado.getText().toString()).getBytes());
                // Let the document provider know you're done by closing the stream.
                fileOutputStream.close();
                pfd.close();

                Toast toast = Toast.makeText(getApplicationContext(), "Arquivo salvo com Sucesso", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


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
        if (id == R.id.action_sair) {
           finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {


        int id = v.getId();

        value = "";

        switch (id) {

            case R.id.btnIniciar:

                showDialog("Digite os números Sorteados separados por vírgulas", id);

                break;

            case R.id.fabRenew:

                txtResultado.setText("");
                btnSaveToFile.setVisibility(View.GONE);
                btnIniciar.setVisibility(View.VISIBLE);
                refatorarLayout(4,6);

                break;

            case R.id.btnSaveToFile:

                createFile("text/plain", "resultados.txt");

                break;

        }


    }


    private void createFile(String mimeType, String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested MIME type.
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }
}
