package pe.edu.tecsup.androidavanzado2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class ProductoRegistrarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_registrar);

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

    private Map<String, String> data;

    public void enviar(View view) {
        data = new HashMap<String, String>();
        TextView txtIdCategoria = (TextView) findViewById(R.id.txtIdCategoria);
        TextView txtNombre = (TextView) findViewById(R.id.txtNombre);
        TextView txtPrecio = (TextView) findViewById(R.id.txtPrecio);
        data.put("idCategoria", txtIdCategoria.getText().toString());
        data.put("nombre", txtNombre.getText().toString());
        data.put("precio", txtPrecio.getText().toString());
        new HttpREST().execute();
    }


    private class HttpREST extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.i("===>", "Dentro de doInBackground()");
            try {
                HttpRequest httpRequest = HttpRequest.post("http://renzovilela.tk/rest/index.php/productos");
                httpRequest.form(data);
                String respuesta = httpRequest.body().toString();
                Gson gson = new Gson();
                Type stringStringMap = new TypeToken<Map<String, Object>>() {
                }.getType();
                final Map<String, Object> retorno = gson.fromJson(respuesta, stringStringMap);

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "" + retorno.get("mensaje"), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception ex) {
                Log.e("===>", "Error: " + ex);
            }
            return null;
        }
    }

}
