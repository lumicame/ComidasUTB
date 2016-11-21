package com.proyect.utb.restaurantesutb.clases;

/**
 * Created by jesus david on 1/09/2016.
 */
public class comidas {
    public comidas(){}
    public comidas(String nombre, String imagen, String descripcion, String precio,String email,String id) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.precio = precio;
        this.email=email;
        this.id=id;
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
    private String precio;

    public String getEmail() {
        return email;
    }

    public void setEmail(String id) {
        this.email = id;
    }

    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
