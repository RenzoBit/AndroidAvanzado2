package pe.edu.tecsup.androidavanzado2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AvisoRegistrarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso_registrar);
    }

    private Map<String, String> data;

    public void enviar(View view) {
        data = new HashMap<String, String>();
        TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        TextView txtFechaInicio = (TextView) findViewById(R.id.txtFechaInicio);
        TextView txtFechaFin = (TextView) findViewById(R.id.txtFechaFin);
        data.put("titulo", txtTitulo.getText().toString());
        data.put("fechaInicio", txtFechaInicio.getText().toString());
        data.put("fechaFin", txtFechaFin.getText().toString());
        new HttpREST().execute();
    }


    private class HttpREST extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.i("===>", "Dentro de doInBackground()");
            try {
                HttpRequest httpRequest = HttpRequest.post("http://renzovilela.tk/rest/index.php/avisos");
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
