package com.proyect.utb.restaurantesutb.como_restaurante.comidas_res;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;
import com.proyect.utb.restaurantesutb.R;
import com.proyect.utb.restaurantesutb.clases.comidas;

import java.util.List;

/**
 * Created by luis mi on 1/09/2016.
 */
public class Adapter_comidas_res extends RecyclerView.Adapter<Adapter_comidas_res.ViewHolder>  {

    private List<comidas> items;
    public Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public SmartImageView imagen;
        public TextView nombre;
        public TextView descripcion;
        public TextView precio;

        public ViewHolder(View v) {
            super(v);
            imagen = (SmartImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            descripcion = (TextView) v.findViewById(R.id.descripcion);
            precio=(TextView) v.findViewById(R.id.precio);
        }

    }

public Adapter_comidas_res(){}
    public Adapter_comidas_res(List<comidas> items,Context context) {
        this.items = items;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_comidas, parent, false);
        return new ViewHolder(v);

    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        Rect rect=new Rect(holder.imagen.getLeft(),holder.imagen.getTop(),holder.imagen.getRight(),holder.imagen.getBottom());
        holder.imagen.setImageUrl("https://myservidor.000webhostapp.com/comidas/fotos_com/"+items.get(i).getImagen(),rect);
        holder.nombre.setText(items.get(i).getNombre());
        holder.descripcion.setText(String.valueOf(items.get(i).getDescripcion()));
        holder.precio.setText(items.get(i).getPrecio());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void wap(List<comidas> list){
        if (items != null) {
            items.clear();
            items.addAll(list);
        }
        else {
            items = list;
        }
        notifyDataSetChanged();
    }

}
