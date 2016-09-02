package com.proyect.utb.restaurantesutb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class descripcion extends AppCompatActivity {

    private TextView descripcion,nombre,precio;
    private ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);


        descripcion=(TextView) findViewById(R.id.textView_descripcion_descripcion);
        nombre=(TextView) findViewById(R.id.textView_descripcion_nombre);
        precio=(TextView) findViewById(R.id.textView_descripcion_precio);
        imagen=(ImageView) findViewById(R.id.imageView_descripcion);

        descripcion.setText(getIntent().getStringExtra("descripcion"));
        nombre.setText(getIntent().getStringExtra("nombre"));
        precio.setText(getIntent().getStringExtra("precio"));
        imagen.setImageResource(getIntent().getIntExtra("imagen",0));


        Toolbar toolbarCard = (Toolbar) findViewById(R.id.toolbarCard);
        toolbarCard.setTitle(getIntent().getStringExtra("nombre"));
        toolbarCard.setSubtitle(getIntent().getStringExtra("descripcion"));
        toolbarCard.inflateMenu(R.menu.card_menu);
        toolbarCard.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_option1:
                        Toast.makeText(descripcion.this, R.string.option1, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_option2:
                        Toast.makeText(descripcion.this, R.string.option2, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_option3:
                        Toast.makeText(descripcion.this, R.string.option3, Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


    }
}
