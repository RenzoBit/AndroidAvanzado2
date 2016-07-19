package pe.edu.tecsup.androidavanzado2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class AvisoBuscarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso_buscar);
    }

    private String criterioBusqueda = "";

    public void buscarAvisos(View view) {
        TextView txtTitulo = (TextView)findViewById(R.id.txtTitulo);
        criterioBusqueda = txtTitulo.getText().toString();
        new HttpREST().execute();
    }

    private class HttpREST extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.i("===>", "Dentro de doInBackground()");
            try {
                final ListView lstAvisos = (ListView)findViewById(R.id.lstAvisos);
                HttpRequest httpRequest = HttpRequest.get("http://renzovilela.tk/rest/avisos/" + criterioBusqueda);
                String respuesta = httpRequest.body().toString();
                Log.i("===>", respuesta);
                Gson gson = new Gson();
                Type stringStringMap = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
                final ArrayList<Map<String, Object>> retorno = gson.fromJson(respuesta, stringStringMap);
                final String[] matriz = new String[retorno.size()];
                int i = 0;
                for (Map<String, Object> x : retorno) {
                    matriz[i++] = (String) (x.get("titulo") + " - " + x.get("fechaInicio"));
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(AvisoBuscarActivity.this, android.R.layout.simple_list_item_1, matriz);
                        lstAvisos.setAdapter(adaptador);
                    }
                });

            } catch (Exception ex) {
                Log.e("===>", "Error: " + ex);
            }
            return null;
        }

    }

}