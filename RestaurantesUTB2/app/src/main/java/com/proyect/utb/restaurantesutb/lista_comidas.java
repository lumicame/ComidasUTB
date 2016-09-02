package com.proyect.utb.restaurantesutb;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class lista_comidas extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_comidas);

        String t1=getIntent().getStringExtra("restaurante");
        final ArrayList<comidas> items =new ArrayList();
        items.add(new comidas("Pizza",R.drawable.pizza_slice_128,"Pizza de jamon y queso","$3000"));
        items.add(new comidas("Pan",R.drawable.toast,"Tostadas con mantequilla","$2500"));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_comidas);
        mRecyclerView.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

// Crear un nuevo adaptador
        mAdapter = new Adapter_comidas(items);
        final Context context = getApplicationContext();
        mRecyclerView.setAdapter(mAdapter);
        // use a linear layout manager
        mRecyclerView.addOnItemTouchListener(new Recyclerviewitemclick(context, new Recyclerviewitemclick.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Intent i=new Intent(lista_comidas.this,descripcion.class);
                        i.putExtra("nombre",items.get(position).getNombre());
                        i.putExtra("imagen",items.get(position).getImagen());
                        i.putExtra("precio",items.get(position).getPrecio());
                        i.putExtra("descripcion",items.get(position).getDescripcion());
                        startActivity(i);

                    }
                })
        );
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
