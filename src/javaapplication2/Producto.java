package javaapplication2;
/**
 *
 * @author franc
 */
public final class Producto {
    
    String nombre="";
    int precio=0;

    public void setPrecio(int total) {
        this.precio = total;
    }
    public Producto(){
    }
    public int getPrecio() {
        return precio;
    }
    public String getNombreTabla(){
        return this.nombre;
    }  
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
