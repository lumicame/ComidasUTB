package com.proyect.utb.restaurantesutb.como_restaurante.pedidos;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.snowdream.android.widget.SmartImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyect.utb.restaurantesutb.R;
import com.proyect.utb.restaurantesutb.clases.comidas;
import com.proyect.utb.restaurantesutb.clases.pedidos;
import com.proyect.utb.restaurantesutb.como_restaurante.comidas_res.Adapter_comidas_res;

import java.util.List;

/**
 * Created by jesus david on 31/08/2016.
 */
public class MyAdapter_pedidos extends RecyclerView.Adapter<MyAdapter_pedidos.ViewHolder> implements View.OnClickListener {

    private List<pedidos> items;
    private View.OnClickListener listener;
    private Context context;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public SmartImageView imagen;
        public TextView nombre;
        public TextView descripcion;
        public TextView email;
        public TextView fecha;
        public View v;

        public ViewHolder(View v) {
            super(v);
            imagen = (SmartImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            descripcion = (TextView) v.findViewById(R.id.visitas);
            email= (TextView) v.findViewById(R.id.para);
            fecha= (TextView) v.findViewById(R.id.fecha);
            this.v=v;
        }

    }

    public void setOnClickListene(View.OnClickListener listener) {
        this.listener = listener;
    }

    public MyAdapter_pedidos(){}
    public MyAdapter_pedidos(List<pedidos> items, Context context) {
        this.items = items;
        this.context=context;
    }
    @Override
    public MyAdapter_pedidos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_pedidos, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);

    }
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    public void onBindViewHolder(final MyAdapter_pedidos.ViewHolder holder, final int i) {

        root.child("comidas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot n:dataSnapshot.getChildren()){
                    comidas comidas1=n.getValue(comidas.class);
                    String email1=comidas1.getEmail();
                    if(email1.equals(items.get(i).getEmail_res())){
                        Rect rect=new Rect(holder.imagen.getLeft(),holder.imagen.getTop(),holder.imagen.getRight(),holder.imagen.getBottom());
                        holder.imagen.setImageUrl("https://myservidor.000webhostapp.com/comidas/fotos_com/"+comidas1.getImagen(),rect);
                        holder.nombre.setText(comidas1.getNombre());
                        holder.descripcion.setText(comidas1.getDescripcion());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.email.setText(items.get(i).getEmail());
        holder.fecha.setText(items.get(i).getFecha());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "click en "+items.get(i).getId(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "long click "+items.get(i).getId(), Toast.LENGTH_SHORT).show();
                return true;
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
    public void wap(List<pedidos> list){
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
