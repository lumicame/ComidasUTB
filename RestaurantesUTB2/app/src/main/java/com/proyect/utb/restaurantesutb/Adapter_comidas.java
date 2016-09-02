package com.proyect.utb.restaurantesutb;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jesus david on 1/09/2016.
 */
public class Adapter_comidas extends RecyclerView.Adapter<Adapter_comidas.ViewHolder>  {

    private List<comidas> items;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView descripcion;
        public TextView precio;

        public ViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            descripcion = (TextView) v.findViewById(R.id.descripcion);
            precio=(TextView) v.findViewById(R.id.precio);
        }

    }


    public Adapter_comidas(List<comidas> items) {
        this.items = items;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_comidas, parent, false);
        return new ViewHolder(v);

    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.imagen.setImageResource(items.get(i).getImagen());
        holder.nombre.setText(items.get(i).getNombre());
        holder.descripcion.setText(String.valueOf(items.get(i).getDescripcion()));
        holder.precio.setText(items.get(i).getPrecio());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
