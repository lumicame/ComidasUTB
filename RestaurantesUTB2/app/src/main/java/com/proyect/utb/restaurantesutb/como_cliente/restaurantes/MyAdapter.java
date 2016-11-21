package com.proyect.utb.restaurantesutb.como_cliente.restaurantes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;
import com.proyect.utb.restaurantesutb.R;
import com.proyect.utb.restaurantesutb.clases.restaurante;
import com.proyect.utb.restaurantesutb.como_cliente.comidas.lista_comidas_clientes;

import java.util.List;

/**
 * Created by jesus david on 31/08/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {

    private List<restaurante> items;
    private View.OnClickListener listener;
    private Context context;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public SmartImageView imagen;
        public TextView nombre;
        public TextView descripcion;
        public View v;

        public ViewHolder(View v) {
            super(v);
            imagen = (SmartImageView) v.findViewById(R.id.imagen1);
            nombre = (TextView) v.findViewById(R.id.nombre);
            descripcion = (TextView) v.findViewById(R.id.visitas);
            this.v=v;
        }

    }

    public void setOnClickListene(View.OnClickListener listener) {
        this.listener = listener;
    }

    public MyAdapter(){}
    public MyAdapter(List<restaurante> items,Context context) {
        this.items = items;
        this.context=context;
    }
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int i) {
        Rect rect=new Rect(holder.imagen.getLeft(),holder.imagen.getTop(),holder.imagen.getRight(),holder.imagen.getBottom());
        holder.imagen.setImageUrl("https://myservidor.000webhostapp.com/comidas/fotos_res/"+items.get(i).getImagen(),rect);
        holder.nombre.setText(items.get(i).getNombre());
        holder.descripcion.setText(String.valueOf(items.get(i).getDescripcion()));
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(v.getContext(),lista_comidas_clientes.class);
                in.putExtra("email",items.get(i).getEmail());
                in.putExtra("imagen","https://myservidor.000webhostapp.com/comidas/fotos_res/"+items.get(i).getImagen());
                in.putExtra("nombre",items.get(i).getNombre());
                v.getContext().startActivity(in);
            }
        });


    }
    private void opciones(Context context) {
        final CharSequence[] option={"Tomar foto","Elegir de galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Elige una opcion");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        break;
                    case 1:

                        break;
                    default:
                        dialog.dismiss();
                        break;

                }
            }


        });
        builder.show();
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void wap(List<restaurante> list){
        if (items != null) {
            items.clear();
            items.addAll(list);
        }
        else {
            items = list;
        }
        notifyDataSetChanged();
    }
    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }
}
