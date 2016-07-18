package pe.edu.tecsup.androidavanzado2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TiendaSOAPClienteActivity extends AppCompatActivity {

    public String NAMESPACE = "http://condeleron.net/";
    //public String URL = "http://192.168.xx.xx:4202/Tienda.asmx?WSDL";
    public String URL = "http://192.168.14.158:4202/Tienda.asmx?WSDL";
    public static EditText txtIdProducto;
    public static TextView txtResultado;
    public static ListView lstProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda_soapcliente);

        txtIdProducto = (EditText)findViewById(R.id.txtIdProducto);
        txtResultado = (TextView) findViewById(R.id.txtResultado);
        lstProductos = (ListView) findViewById(R.id.lstProductos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    String cajaTexto = "";

    public void verificar(View v) {
        cajaTexto = txtIdProducto.getText().toString();
        new VerificarSOAPTask().execute();
    }

    public void listar(View v) {
        cajaTexto = txtIdProducto.getText().toString();
        new ListarSOAPListaTask().execute();
    }

    class VerificarSOAPTask extends AsyncTask<Void, Void, Void> {

        String METHOD_NAME = "VerificarProducto";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;

        protected Void doInBackground(Void... arg0) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


            request.addProperty("idProducto", cajaTexto);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);
            transporte.debug = true;
            try {
                transporte.call(SOAP_ACTION, envelope);
//          Log.i("REQUEST: ", "" + transporte.requestDump);
//          Log.i("RESPONSE: ", "" + transporte.responseDump);

                // Procesar el Response
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();
                String cadena = "Hay " + resultado_xml.getPropertyAsString("Stock")  + " items de '" + resultado_xml.getPropertyAsString("Nombre") + "'";

                final String[] resMatriz = {cadena};

                runOnUiThread(new Runnable() {
                    public void run() {
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                                TiendaSOAPClienteActivity.this,
                                android.R.layout.simple_list_item_1,
                                resMatriz);
                        lstProductos.setAdapter(adaptador);
                    }
                });

            } catch (Exception e) {
                Log.i("SOAPCliente: ", e.toString());
                runOnUiThread(new Runnable() {
                    public void run() {
                        txtResultado.setText("Error!");
                    }
                });
            }
            return null;

        }
    }

    class ListarSOAPListaTask extends AsyncTask<Void, Void, Void> {

        String METHOD_NAME = "ListarProductos";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;

        protected Void doInBackground(Void... arg0) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap = (SoapObject) envelope.getResponse();

                final String[] listaClientes = new String[resSoap.getPropertyCount()];

                for (int i = 0; i < listaClientes.length; i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);
                    listaClientes[i] = ic.getPropertyAsString(0).toString() + ") " + ic.getPropertyAsString(1).toString();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                                TiendaSOAPClienteActivity.this,
                                android.R.layout.simple_list_item_1,
                                listaClientes);
                        lstProductos.setAdapter(adaptador);
                    }
                });

            } catch (Exception e) {
                Log.i("SOAPCliente: ", e.toString());
                runOnUiThread(new Runnable() {
                    public void run() {
                        txtResultado.setText("Error!");
                    }
                });
            }
            return null;
        }

    }

}
