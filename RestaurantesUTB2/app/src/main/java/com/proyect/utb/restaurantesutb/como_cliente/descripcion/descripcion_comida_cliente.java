package com.proyect.utb.restaurantesutb.como_cliente.descripcion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.github.snowdream.android.widget.SmartImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyect.utb.restaurantesutb.MySingleton;
import com.proyect.utb.restaurantesutb.R;
import com.proyect.utb.restaurantesutb.clases.pedidos;
import com.proyect.utb.restaurantesutb.como_cliente.comidas.lista_comidas_clientes;
import com.proyect.utb.restaurantesutb.como_cliente.restaurantes.principal_clientes;
import com.proyect.utb.restaurantesutb.tipo_cuenta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class descripcion_comida_cliente extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView descripcion,precio;
    private SmartImageView imagen;
    TextView nav_nombre,nav_email;
    CircleImageView nav_imagen;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    private static final int DURATION = 250;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_comida_cliente);
        datos_cliente();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("nombre"));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs =
                        getSharedPreferences("cuenta", Context.MODE_PRIVATE);
                 String correo = prefs.getString("email", "1");
                if(!correo.equals("1")){
                    add_a_carrito();
                }else {
                    Snackbar.make(view, "Tienes que iniciar sesion para poder hacer pedidos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        descripcion=(TextView) findViewById(R.id.textView_descripcion_descripcion);
        precio=(TextView) findViewById(R.id.textView_descripcion_precio);
        imagen=(SmartImageView) findViewById(R.id.imageView_descripcion);

        descripcion.setText(getIntent().getStringExtra("descripcion"));
        precio.setText("$"+getIntent().getStringExtra("precio"));
        Rect rect=new Rect(imagen.getLeft(),imagen.getTop(),imagen.getRight(),imagen.getBottom());

        imagen.setImageUrl(getIntent().getStringExtra("imagen"),rect);

    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    private void add_a_carrito() {
        final ProgressDialog progressDialog=new ProgressDialog(descripcion_comida_cliente.this);
        progressDialog.setMessage("Realizando pedido...");
        progressDialog.show();
        final String fecha=getDateTime();
        //
                        pedidos pe=new pedidos(nav_email.getText().toString(),fecha,getIntent().getStringExtra("id"),root.child("pedido").push().getKey(),getIntent().getStringExtra("email"));

                        root.child("pedidos").child(root.child("pedido").push().getKey()).setValue(pe);
                        progressDialog.dismiss();
                        Toast.makeText(descripcion_comida_cliente.this, "Pedido exitosa", Toast.LENGTH_SHORT).show();
                        finish();
        //
        //

    }

    private void datos_cliente(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        nav_imagen= (CircleImageView) headerView.findViewById(R.id.activity_principal_clientes_header_imagen);
        nav_nombre = (TextView) headerView.findViewById(R.id.activity_principal_clientes_header_nombre);
        nav_email= (TextView) headerView.findViewById(R.id.activity_principal_clientes_header_email);

        SharedPreferences prefs =
                getSharedPreferences("cuenta", Context.MODE_PRIVATE);
        final String correo = prefs.getString("email", "1");
        root.child("clientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot n : dataSnapshot.getChildren()){

                    String email1=n.child("email").getValue().toString().trim();
                    if(email1.equals(correo.trim())) {

                        nav_nombre.setText(n.child("nombre").getValue().toString());
                        nav_email.setText(email1);
                        Rect rect=new Rect(nav_imagen.getLeft(),nav_imagen.getTop(),nav_imagen.getRight(),nav_imagen.getBottom());
                        ImageRequest request = new ImageRequest("https://myservidor.000webhostapp.com/comidas/fotos_cli/"+n.child("imagen").getValue().toString(),
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        nav_imagen.setImageBitmap(bitmap);
                                    }
                                }, 0, 0, null,
                                new Response.ErrorListener() {
                                    public void onErrorResponse(VolleyError error) {
                                    }
                                });
// Access the RequestQueue through your singleton class.
                        request.setShouldCache(false);

                        MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(request);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            principal_clientes principal_clientes=new principal_clientes();
            principal_clientes.activity.finish();

            lista_comidas_clientes lista_comidas_clientes=new lista_comidas_clientes();
            lista_comidas_clientes.activity.finish();

            Intent intent=new Intent(descripcion_comida_cliente.this,principal_clientes.class);
            startActivity(intent);

            finish();
            // Handle the camera action
        } else if (id == R.id.nav_pedidos) {

        } else if (id == R.id.nav_logout) {
            principal_clientes principal_clientes=new principal_clientes();
            principal_clientes.activity.finish();
            lista_comidas_clientes lista_comidas_clientes=new lista_comidas_clientes();
            lista_comidas_clientes.activity.finish();
            SharedPreferences settings = getApplicationContext().getSharedPreferences("cuenta", Context.MODE_PRIVATE);
            settings.edit().clear().commit();
            Intent intent=new Intent(descripcion_comida_cliente.this,tipo_cuenta.class);
            startActivity(intent);

            finish();

        }else if (id == R.id.nav_perfil) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
