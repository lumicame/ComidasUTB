package com.proyect.utb.restaurantesutb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.proyect.utb.restaurantesutb.como_restaurante.pedidos.res_pedidos;
import com.proyect.utb.restaurantesutb.login.login;
import com.proyect.utb.restaurantesutb.como_cliente.restaurantes.principal_clientes;

public class tipo_cuenta extends AppCompatActivity {

    private Button res,cli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_cuenta);
        res= (Button) findViewById(R.id.res);
        cli= (Button) findViewById(R.id.cli);
        verificar();
        //boton para iniciar como restaurante
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(tipo_cuenta.this,login.class);
                i.putExtra("tipo","res");
                startActivity(i);
            }
        });
        //boton para iniciar como cliente
        cli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(tipo_cuenta.this,login.class);
                i.putExtra("tipo","cli");
                startActivity(i);
            }
        });
    }

    private void verificar() {
        SharedPreferences prefs =
                getSharedPreferences("cuenta", Context.MODE_PRIVATE);
        String correo = prefs.getString("email", "1");
        String pas=prefs.getString("pass","1");
        String tipo=prefs.getString("tipo","1");
        if(!correo.equals("1")&&tipo.equals("cli")){
            Intent intent=new Intent(tipo_cuenta.this, principal_clientes.class);
            startActivity(intent);
            finish();
        }
        else if(!correo.equals("1")&&tipo.equals("res")){
            Intent intent=new Intent(tipo_cuenta.this, res_pedidos.class);
            startActivity(intent);
            finish();
        }else {}
    }

}
