package com.proyect.utb.restaurantesutb.clases;

/**
 * Created by Luis on 31/08/2016.
 */
public class restaurante {
    public restaurante(){}
    public restaurante(String nombre, String imagen, String descripcion, String email,String pass) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.email = email;
        this.pass=pass;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    private String nombre;
    private String descripcion;
    private String imagen;
    private String email;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    String pass;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
