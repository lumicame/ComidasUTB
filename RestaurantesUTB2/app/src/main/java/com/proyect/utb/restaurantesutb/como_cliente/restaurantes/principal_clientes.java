package com.proyect.utb.restaurantesutb.como_cliente.restaurantes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyect.utb.restaurantesutb.MySingleton;
import com.proyect.utb.restaurantesutb.R;
import com.proyect.utb.restaurantesutb.clases.clientes;
import com.proyect.utb.restaurantesutb.clases.restaurante;
import com.proyect.utb.restaurantesutb.como_cliente.pedidos.clientes_pedidos;
import com.proyect.utb.restaurantesutb.tipo_cuenta;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class principal_clientes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static Activity activity;
    TextView nav_nombre,nav_email;
    CircleImageView nav_imagen;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();


    final ArrayList<restaurante> items =new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_clientes);
        activity=this;
        datos_cliente();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        mRecyclerView= (RecyclerView) findViewById(R.id.recycler_view_principal);




        mRecyclerView.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

// Crear un nuevo adaptador
        mAdapter = new MyAdapter(items,getApplication().getApplicationContext());
        final Context context = getApplicationContext();
        mRecyclerView.setAdapter(mAdapter);

        traerdatos();
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
                    clientes cliente=n.getValue(clientes.class);
                    String email1=cliente.getEmail();
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
    private void traerdatos() {

        root.child("restaurantes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot n:dataSnapshot.getChildren()){
                    restaurante restaurante1=n.getValue(restaurante.class);
                    items.add(restaurante1);

                }
                new MyAdapter().wap(items);
                mAdapter.notifyDataSetChanged();
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
            // Handle the camera action
        } else if (id == R.id.nav_pedidos) {
            Intent intent=new Intent(principal_clientes.this,clientes_pedidos.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            SharedPreferences settings = getApplicationContext().getSharedPreferences("cuenta", Context.MODE_PRIVATE);
            settings.edit().clear().commit();
            Intent intent=new Intent(principal_clientes.this,tipo_cuenta.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.nav_perfil){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
