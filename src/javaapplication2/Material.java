package javaapplication2;

/**
 *
 * @author franc
 */
public class Material {
    int precio=0;
    String nombre="";
    int cantidad=1;
    int valorTotal=0;
    
    public Material() {

    }
    public Material(String name, Integer price) {
        this.nombre=name;
        this.precio=price;
    }
    public void setValores(){
       this.valorTotal=this.precio*this.cantidad;
    }
    public int getCantidad() {
        return cantidad;
    }
    public Integer getValorTotal(){
        return this.valorTotal;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public int getPrecio() {
        return precio;
    }
    public void setPrecio(int precio) {
        this.precio = precio;
    }
    public String getdatos() {
        return "Nombre: "+nombre+" Cantidad: "+this.cantidad+" Precio: "+this.valorTotal;
    }
    public String getNombre() {
        return nombre;
    }
    public String getNombreTabla() {
        return this.cantidad+" "+this.nombre+": $"+this.getValorTotal();
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
    
}
