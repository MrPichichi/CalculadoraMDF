
package javaapplication2;

/**
 *
 * @author franc
 */
public final class MDF {
    
    String nombre="";
    int precio=0;
    int valor=0;
    int alto=0;
    int ancho=0;
    
   
    public MDF(){
    
    }

    public MDF(String n,Integer p, Integer vala, Integer alto,Integer ancho) {
        this.nombre=n;
        this.precio=p;
        this.valor=vala;
        this.alto=alto;
        this.ancho=ancho;    }

    public String getNombre() {
        return nombre;
    }
   public void setNombre(String nombre) {
        this.nombre = nombre;
    }
   
    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getValArea() {
        return valor;
    }

    public void setValArea(int valArea) {
        this.valor = valArea;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public void setNombreCorte() {
        this.nombre="Corte: "+this.alto+" X "+this.ancho;
    }
    public void setNombreGrabado() {
        this.nombre="Grabado: "+this.alto+" X "+this.ancho;
    }
   
    public void setDatos(){
        
    }
    
    
}
