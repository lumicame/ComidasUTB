package com.proyect.utb.restaurantesutb.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.proyect.utb.restaurantesutb.R;
import com.proyect.utb.restaurantesutb.clases.clientes;
import com.proyect.utb.restaurantesutb.clases.restaurante;
import com.proyect.utb.restaurantesutb.registrar.registrar;
import com.proyect.utb.restaurantesutb.registrar.registrar_cliente;
import com.proyect.utb.restaurantesutb.como_cliente.restaurantes.principal_clientes;
import com.proyect.utb.restaurantesutb.tipo_cuenta;

import java.util.List;

public class login extends AppCompatActivity implements Validator.ValidationListener{
    @Email(message= "Escriba un email valido" )
    private EditText email;
    @NotEmpty(message = "Contraseña no puede ser vacio" )
    private EditText pass;

    private TextView registrarse;
    private TextView invitado;
    private Button iniciar;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //variables
    registrarse=(TextView) findViewById(R.id.registrar);
    invitado= (TextView) findViewById(R.id.Entrar_invitado);
        iniciar= (Button) findViewById(R.id.button_iniciar);
        email= (EditText) findViewById(R.id.editText_email);
        pass= (EditText) findViewById(R.id.editText_contraseña);
        validator = new Validator(this);
        validator.setValidationListener(this);
        //
        if(getIntent().getStringExtra("tipo").equals("cli")){
            iniciar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validator.validate();
                }
            });
            invitado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(login.this, principal_clientes.class);
                    startActivity(intent);

                }
            });
            registrarse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it=new Intent(login.this,registrar_cliente.class);
                    startActivity(it);
                }
            });
            registrarse.setText("Registrarse como cliente");


        }
        if(getIntent().getStringExtra("tipo").equals("res")) {

            iniciar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validator.validate();
                }
            });
            invitado.setVisibility(View.INVISIBLE);
            registrarse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(login.this,registrar.class);
                    startActivity(i);
                }
            });
            registrarse.setText("Registrarse como Restaurante");

        }

    }

    private void login_restaurante() {
        final ProgressDialog progressDialog=new ProgressDialog(login.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        root.child("restaurantes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot n : dataSnapshot.getChildren()){
                    restaurante restaurante1=n.getValue(restaurante.class);
                    String email1=restaurante1.getEmail();
                    String pass1=restaurante1.getPass();
                    Log.i("email",email1+" "+email.getText().toString().trim());
                   if(email1.equals(email.getText().toString().trim()) && pass1.equals(pass.getText().toString().trim())) {
                       Toast.makeText(login.this, restaurante1.getNombre(), Toast.LENGTH_SHORT).show();
                       SharedPreferences prefs =
                               getSharedPreferences("cuenta",Context.MODE_PRIVATE);

                       SharedPreferences.Editor editor = prefs.edit();
                       editor.putString("email", email1);
                       editor.putString("pass",pass1);
                       editor.putString("tipo","res");
                       editor.commit();
                       progressDialog.dismiss();
                       Intent intent=new Intent(login.this,tipo_cuenta.class);
                       startActivity(intent);
                       finish();
                   }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void login_cliente() {
        final ProgressDialog progressDialog=new ProgressDialog(login.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        root.child("clientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot n : dataSnapshot.getChildren()){
                    clientes clientes1=n.getValue(clientes.class);
                    String email1=clientes1.getEmail();
                    String pass1=clientes1.getPass();
                    Log.i("email",email1+" "+email.getText().toString().trim());
                    if(email1.equals(email.getText().toString().trim()) && pass1.equals(pass.getText().toString().trim())) {
                        Toast.makeText(login.this, clientes1.getNombre(), Toast.LENGTH_SHORT).show();
                        SharedPreferences prefs =
                                getSharedPreferences("cuenta",Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("email", email1);
                        editor.putString("pass",pass1);
                        editor.putString("tipo","cli");
                        editor.commit();
                        progressDialog.dismiss();
                        Intent intent=new Intent(login.this,tipo_cuenta.class);
                        startActivity(intent);
                        finish();
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    @Override
    public void onValidationSucceeded() {
        if(getIntent().getStringExtra("tipo").equals("cli")){
            login_cliente();
        }else {
            login_restaurante();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
            else
            {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
