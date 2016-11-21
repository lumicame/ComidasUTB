package com.proyect.utb.restaurantesutb.clases;

/**
 * Created by luis mi on 20/11/2016.
 */

public class pedidos {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public pedidos(){}
    public pedidos(String email, String fecha, String id, String id_pedido,String email_res) {
        this.email = email;
        this.fecha = fecha;
        this.id = id;
        this.id_pedido=id_pedido;
        this.email_res=email_res;
    }

    String email;
    String id;
    String fecha;

    public String getEmail_res() {
        return email_res;
    }

    public void setEmail_res(String email_res) {
        this.email_res = email_res;
    }

    String email_res;

    public String getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }

    String id_pedido;

}
