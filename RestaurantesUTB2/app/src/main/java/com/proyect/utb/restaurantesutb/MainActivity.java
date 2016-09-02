package com.proyect.utb.restaurantesutb;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<restaurante> items =new ArrayList();
        items.add(new restaurante("Alcatraz",R.mipmap.ic_launcher,"restaurante de todo tipo de comidas"));
        items.add(new restaurante("otros",R.mipmap.ic_launcher,"alguna descripcion"));
        items.add(new restaurante("solo cafe",R.mipmap.ic_launcher,"Tenemos variedad de cafes, conocelos"));
        items.add(new restaurante("otros",R.mipmap.ic_launcher,"Tenemos variedad de cafes, conocelos"));



        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_principal);
        mRecyclerView.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

// Crear un nuevo adaptador
        mAdapter = new MyAdapter(items);
        final Context context = getApplicationContext();
        mRecyclerView.setAdapter(mAdapter);
        // use a linear layout manager
        mRecyclerView.addOnItemTouchListener(new Recyclerviewitemclick(context, new Recyclerviewitemclick.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent i=new Intent(MainActivity.this,lista_comidas.class);
                        i.putExtra("restaurante",items.get(position).getNombre());
                        startActivity(i);

                    }
                })
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public void onClick(View view) {

    }
}
