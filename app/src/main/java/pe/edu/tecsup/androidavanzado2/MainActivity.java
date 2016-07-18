package pe.edu.tecsup.androidavanzado2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegistrarProducto;
    private Button btnBuscarProducto;
    private Button btnRegistrarAviso;
    private Button btnBuscarAviso;
    private Button btnSOAP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegistrarProducto = (Button) findViewById(R.id.btnRegistrarProducto);
        btnBuscarProducto = (Button) findViewById(R.id.btnBuscarProducto);
        btnRegistrarAviso = (Button) findViewById(R.id.btnRegistrarAviso);
        btnBuscarAviso = (Button) findViewById(R.id.btnBuscarAviso);
        btnSOAP = (Button) findViewById(R.id.btnSOAP);

        btnRegistrarProducto.setOnClickListener(this);
        btnBuscarProducto.setOnClickListener(this);
        btnRegistrarAviso.setOnClickListener(this);
        btnBuscarAviso.setOnClickListener(this);
        btnSOAP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btnRegistrarProducto:
                intent = new Intent(MainActivity.this, ProductoRegistrarActivity.class);
                break;
            case R.id.btnBuscarProducto:
                intent = new Intent(MainActivity.this, ProductosBuscarActivity.class);
                break;
            case R.id.btnRegistrarAviso:
                intent = new Intent(MainActivity.this, AvisoRegistrarActivity.class);
                break;
            case R.id.btnBuscarAviso:
                intent = new Intent(MainActivity.this, AvisoBuscarActivity.class);
                break;
            case R.id.btnSOAP:
                intent = new Intent(MainActivity.this, TiendaSOAPClienteActivity.class);
                break;
        }
        startActivity(intent);
    }
}
