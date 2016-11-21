package com.proyect.utb.restaurantesutb.clases;

/**
 * Created by luis mi on 23/10/2016.
 */

public class clientes {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public clientes(){}
    public clientes(String email, String nombre, String pass,String imagen) {
        this.email = email;
        this.nombre = nombre;
        this.pass = pass;
        this.imagen=imagen;
    }

    private String email;
    private String nombre;
    private String pass;

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    String imagen;
}
