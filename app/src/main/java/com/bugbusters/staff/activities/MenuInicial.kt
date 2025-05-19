package com.bugbusters.staff.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.bugbusters.staff.R;

public class MenuInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);

        Button btnPedidosActivos = findViewById(R.id.btnPedidosActivos);
        Button btnGenerarQR = findViewById(R.id.btnGenerarQR);
        Button btnGenerarCuentas = findViewById(R.id.btnGenerarCuentas);

        btnPedidosActivos.setOnClickListener(v -> {
            Intent intent = new Intent(this, PedidosActivosActivity.class);
            startActivity(intent);
        });

        btnGenerarQR.setOnClickListener(v -> {
            Intent intent = new Intent(this, GenerarQRActivity.class);
            startActivity(intent);
        });

        btnGenerarCuentas.setOnClickListener(v -> {
            Intent intent = new Intent(this, GenerarCuentaActivity.class);
            startActivity(intent);
        });
    }
}
