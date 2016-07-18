package pe.edu.tecsup.androidavanzado2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
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


public class ProductosBuscarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_buscar);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    private String criterioBusqueda = "";

    public void buscarProductos(View view) {
        TextView txtProducto = (TextView)findViewById(R.id.txtProducto);
        criterioBusqueda = txtProducto.getText().toString();
        new HttpREST().execute();
    }

    private class HttpREST extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            Log.i("===>", "Dentro de doInBackground()");
            try {

                final ListView lstProductos = (ListView)findViewById(R.id.lstProductos);

                HttpRequest httpRequest = HttpRequest.get("http://renzovilela.tk/rest/index.php/productos/" + criterioBusqueda);

                String respuesta = httpRequest.body().toString();

                Log.i("===>", respuesta);

                Gson gson = new Gson();
                Type stringStringMap = new TypeToken<ArrayList<Map<String, Object>>>() {
                }.getType();
                final ArrayList<Map<String, Object>> retorno = gson.fromJson(respuesta, stringStringMap);

                final String[] matriz = new String[retorno.size()];
                int i = 0;

                for (Map<String, Object> x : retorno) {
                    matriz[i++] = (String) (x.get("nombre") + " - S/. " + x.get("precio"));
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                                ProductosBuscarActivity.this,
                                android.R.layout.simple_list_item_1,
                                matriz);
                        lstProductos.setAdapter(adaptador);
                    }
                });

            } catch (Exception ex) {
                Log.e("===>", "Error: " + ex);
            }
            return null;
        }
    }

}
