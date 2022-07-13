
package javaapplication2;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public final class Interfaz extends javax.swing.JFrame {
    Material mate;
    Producto prod;
    MDF mdf;
    int costo=0;
    int totalMDF=0;
    int total=0;
    int luz=25;
    int consumoLuz=0;
    int cantidad=0;
    int ganancia=0;
    int valorMateriales=0;
    int mdfValor=9000;
    int mdfAlto=149; 
    int mdfAncho=250;
    int mdfPorcentaje=10000;
    int valorMdfGanancia=0;
    int val=0;
    int inversionCorte=0;
    
    boolean update=false;
    boolean load=false;
    boolean editMDF=false;
    boolean editMAT=false;
    
    String selected="";
    
    HashMap<String, Producto> hashProductos = new HashMap<>();//matriz de productos
    HashMap<String, Object> hashUtilizados = new HashMap<>();//Matriz de materiales usados
    HashMap<String, Material> hashMAT = new HashMap<>();//matriz materiales  
    HashMap<String, MDF> hashMDF = new HashMap<>();//matriz Mdf
    
    Integer[] datos= new Integer[3];
    Integer[] dificultad= new Integer[3];
     
    String[] listaVisualCompleta= new String[100];
    String[] ListaVisualCompletaPrecio= new String[100];
    String[] visualUsado= new String[100];
    String[] visualUsadoPrecio= new String[100];
    
    ArrayList<String> listaMateriales= new ArrayList<>();
    ArrayList<String> listaMDF= new ArrayList<>();
    ArrayList<String> trozo= new ArrayList<>();
    ArrayList<String> listaCompleta= new ArrayList<>();
    ArrayList<String> listaCompletaPrecio= new ArrayList<>();
    ArrayList<String> listaUsado= new ArrayList<>();
    ArrayList<String> listaUsadoPrecio= new ArrayList<>();
    ArrayList<Producto> listaProductos= new ArrayList<>();
    
    
    public Interfaz() throws IOException {
        initComponents();
        this.setVisible(true);
        this.setTitle("DC LASER V1.0");
        Image logo = new ImageIcon(getClass().getResource("1.jpg")).getImage();
        this.setIconImage(logo);
        this.setResizable(false);
        
        File mat = new File("Materiales");
        mat.mkdirs();
        File carpetaProductos = new File("Productos");
        carpetaProductos.mkdirs();
        
        File carpetaMDF = new File("MDF");
        carpetaMDF.mkdirs();
        
        this.dificultad[0]=9000;
        this.dificultad[1]=10000;
        this.dificultad[2]=12000;
        
        this.porcentajeGanancia.setText("50");
        
        this.importarMateriales();
        this.importarMDF();
   
    }
    
    public void setTablaMDF(){
        this.visualMDFPrecio.setText(Integer.toString(this.mdfValor));
        this.visualMDFALto.setText(Integer.toString(this.mdfAlto));
        this.visualMDFancho.setText(Integer.toString(this.mdfAncho));
        this.visualMDFLuz.setText(Integer.toString(this.luz));
    }
    
    public void segmentarMDF(){
        this.mdfValor=Integer.parseInt(this.visualMDFPrecio.getText());
        this.mdfAlto=Integer.parseInt(this.visualMDFPrecio.getText());
        this.mdfAncho=Integer.parseInt(this.visualMDFPrecio.getText());
        this.mdfPorcentaje=Integer.parseInt(this.visualMDFPrecio.getText());
        
    }
      
        //importa datos del txt o lo crea
    public void importarMateriales(){
        System.out.println("Operando material");
        final File mat = new File("Materiales/Materiales.txt");
        if(mat.exists()){
            System.out.println("Importand!");
            this.importarDatosMatariales(mat);
            this.updateListas();
            this.setTablasMateriales(); 
            this.setTablaMDF();
            System.out.println("Importado ok");
        }else{
            this.inicializarTXTMateriales();
            System.out.println("Creando y cargando");
            this.importarMateriales();
        }
    }
    public void importarDatosMatariales(File f){
        
        BufferedReader entrada = null; 
                try { 
                    entrada = new BufferedReader( new FileReader( f ) ); 
                    String linea;
                    String valor;
                    this.hashMAT=new HashMap<>();
                    while(entrada.ready()){ 
                        linea = entrada.readLine();
                        if(linea.equals("*")){
                                //sET lUZ
                                linea=entrada.readLine();
                                this.luz=Integer.parseInt(linea);
                                //SET tablas luz
                                this.visualMDFLuz.setText(linea);
                                System.out.println("Luz: "+linea );
                                linea=entrada.readLine();
                                this.mdfValor=Integer.parseInt(linea);
                                System.out.println("VALOR: "+linea );
                                linea=entrada.readLine();
                                this.mdfAncho=Integer.parseInt(linea);
                                System.out.println("ancho: "+linea );
                                linea=entrada.readLine();
                                this.mdfAlto=Integer.parseInt(linea);
                                System.out.println("altoo: "+linea );
                                linea=entrada.readLine();
                                this.mdfPorcentaje=Integer.parseInt(linea);
                                this.dificultad[0]=this.mdfPorcentaje-2000;
                                this.dificultad[1]=this.mdfPorcentaje;
                                this.dificultad[2]=this.mdfPorcentaje+2000;
                                System.out.println("%: "+linea ); 
                                linea=entrada.readLine();
                                System.out.println("*: "+linea );
                            }
                        if(linea.equals("-") && entrada.ready()){
                                linea = entrada.readLine();
                                valor =entrada.readLine();
                                System.out.println("-> : "+linea+"  "+valor);
                                this.mate=new Material(linea,Integer.parseInt(valor));
                                this.hashMAT.put(linea,this.mate);
                        }
                    }
                    
                   
                }catch (IOException e) { 
                    } 
                    finally{ 
                        try{ 
                            entrada.close(); 
                        }
                        catch(IOException e1){} 
                    } 
                this.updateListas();
    }
    
    public void inicializarTXTMateriales(){
          try {
            System.out.println("Iniciando txt MATERIALES");
            File file = new File("Materiales/Materiales.txt");
            file.delete();
            // Si el archivo no existe es creado
              FileWriter fw = new FileWriter(file);
              try (BufferedWriter bw = new BufferedWriter(fw)) {
                        bw.write("*");
                        bw.newLine();
                        bw.write(String.valueOf(this.luz));
                        bw.newLine();
                        bw.write(String.valueOf(this.mdfValor));
                        bw.newLine();
                        bw.write(String.valueOf(this.mdfAlto));
                        bw.newLine();
                        bw.write(String.valueOf(this.mdfAncho));
                        bw.newLine();
                        bw.write(String.valueOf(this.mdfPorcentaje));
                        bw.newLine();
                        bw.write("*");
                        bw.close();
              }
            } catch (IOException e) {
            }
    }
    
  
    
    public void crearTXTMateriales(){
        this.updateListas();
          try {
            File aux = new File("Materiales/Materiales.txt");
            aux.delete();
            File file = new File("Materiales/Materiales.txt");
            FileWriter fw = new FileWriter(file);
            System.out.println("ACTUALIZANDO TXT");
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                        bw.write("*");
                        bw.newLine();
                        bw.write(String.valueOf(this.luz));
                        bw.newLine();
                        bw.write(String.valueOf(this.mdfValor));
                        bw.newLine();
                        bw.write(String.valueOf(this.mdfAlto));
                        bw.newLine();
                        bw.write(String.valueOf(this.mdfAncho));
                        bw.newLine();
                        bw.write(String.valueOf(this.mdfPorcentaje));
                        bw.newLine();
                        bw.write("*");
                        bw.newLine();
                        bw.write("/");
                        for(int l=0;l<this.hashMAT.size();l++){
                                bw.newLine();
                                bw.write("-");
                                bw.newLine();
                                Material auxMAT=this.hashMAT.get(this.listaMateriales.get(l));
                                bw.write(auxMAT.getNombre());//nombre
                                bw.newLine();
                                bw.write(String.valueOf(auxMAT.getPrecio()));//valor
                            }
                        bw.close();
              }
            } catch (IOException e) {
            }
          System.out.println("TXT ACTUALIZADO");
          this.importarMDF();
    }
    

    public void importarMDF(){
        System.out.println("Operando MDF");
        final File emedf = new File("MDF/MDF.txt");
        if(emedf.exists()){
            System.out.println("Importand!");
            this.importarDatosMDF(emedf);
            this.setTablasMateriales(); 
            this.setTablaMDF();
            System.out.println("Importado ok");
        }else{
            System.out.println("NAda que cargar");
        }
    }
    public void crearTXTMDf(){
          this.updateListas();
          try {
            File aux = new File("MDF/MDF.txt");
            aux.delete();
            File file = new File("MDF/MDF.txt");
            FileWriter fw = new FileWriter(file);
            System.out.println("creando TXT MDF");
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                        for(int l=0;l<this.hashMDF.size();l++){
                                bw.write("-");
                                bw.newLine();
                                this.mdf=this.hashMDF.get(this.listaMDF.get(l));
                                bw.write(this.mdf.getNombre());//nombre
                                bw.newLine();
                                bw.write(String.valueOf(this.mdf.getPrecio()));//precio
                                bw.newLine();
                                bw.write(String.valueOf(this.mdf.getValArea()));//val area
                                bw.newLine();
                                bw.write(String.valueOf(this.mdf.getAlto()));//alto
                                bw.newLine();
                                bw.write(String.valueOf(this.mdf.getAncho()));//valor
                                bw.newLine();
                            }
                        bw.close();
              }
            } catch (IOException e) {
            }
          this.importarMDF();
          System.out.println("TXT MDF CREADO");
    }
    
    public void importarDatosMDF(File f){
        BufferedReader entrada = null; 
                try { 
                    entrada = new BufferedReader( new FileReader( f ) ); 
                    String linea;
                    this.hashMDF=new HashMap<>();
                    this.mdf=new MDF();
                    while(entrada.ready()){ 
                        linea = entrada.readLine();
                        if(linea.equals("-")){
                                //nombre precio val alto ancho
                                linea=entrada.readLine();
                                this.mdf.setNombre(linea);
                                linea=entrada.readLine();
                                this.mdf.setPrecio(Integer.parseInt(linea));
                                linea=entrada.readLine();
                                this.mdf.setValArea(Integer.parseInt(linea));
                                linea=entrada.readLine();
                                this.mdf.setAlto(Integer.parseInt(linea));
                                linea=entrada.readLine();
                                this.mdf.setAncho(Integer.parseInt(linea));
                                this.hashMDF.put(this.mdf.nombre,this.mdf);
                            }
                    }
                   
                }catch (IOException e) { 
                    } 
                    finally{ 
                        try{ 
                            entrada.close(); 
                        }
                        catch(IOException e1){} 
                    } 
                this.updateListas();
    }
    
    public void reescribirMateriales() {
        this.crearTXTMateriales();
        this.setTablasMateriales();
        System.out.println("UPDATE Materiales COMPLETE");
    }
    
    public void reescribirMDF() {
        //this.reescribirTXTMDF();
        this.setTablasMateriales();
        System.out.println("UPDATE MDF COMPLETE");
    }
    public void calcularTotal(){
        if(!"".equals(this.porcentajeGanancia.getText())){
            //System.out.println("TOTAL 1: "+this.total);
            int g=Integer.parseInt(this.porcentajeGanancia.getText());
            this.datos = new Integer[3];
            this.totalMDF=0;
            this.total=0;
            this.valorMateriales=0;
            int gananciatotal=0;
            System.out.println("MDF LISTA: "+this.listaMDF);
            System.out.println("MAT LISTA: "+this.listaMateriales);
            hashUtilizados.entrySet().forEach((Map.Entry<String, Object> entry) -> {
                if(entry.getValue() instanceof Material){
                    this.valorMateriales+=this.datos[0];
                    System.out.println(" Sumando MATERIAL: "+entry.getKey()+" "+this.datos[0]);
                
                }
                if(entry.getValue() instanceof MDF){
                     //this.datos=this.materialesUtilizados.get(entry.getKey());
                    this.totalMDF+=this.datos[0];
                    System.out.println("MDF EN ESPERA "+entry.getKey()+" "+this.datos[0]);
                
                }
                
            });
            //Set inverdsion
            this.jLabel33.setText(Integer.toString(this.valorMateriales));
            
            //lo que se gana
            int win=(this.valorMateriales*g)/100;
            
            this.jLabel38.setText(Integer.toString(win));//
            
            gananciatotal+=win+this.totalMDF;
            
            this.jLabel41.setText(Integer.toString(this.totalMDF));
            this.jLabel42.setText(Integer.toString(gananciatotal));
            
            //set total materiales
            int auxTotal=this.valorMateriales+win;
            //this.jLabel38.setText(Integer.toString(auxTotal));
            
            
                    
            System.out.println("MAT: $"+this.valorMateriales);
            System.out.println("MAT win: $"+win);
            System.out.println("MAT total: $"+auxTotal);
           System.out.println("MDF: $"+this.totalMDF);
            
            this.total=auxTotal+this.totalMDF;
            this.jLabel44.setText(Integer.toString(this.total));
            System.out.println("TOTAL: "+this.total);
            
            //ganancia
            
            //total
        }
    
    }
    
    
    public void crearTXTProducto(Producto p){
         try {
            File aux = new File("Productos/"+p.nombre+".txt");
            aux.delete();
            File file = new File("Productos/"+p.nombre+".txt");
            FileWriter fw = new FileWriter(file);
            System.out.println("CREANDO TXT PRODUCTO");
            this.datos= new Integer[3];
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(p.nombre);
                    bw.newLine();
                        for(int l=0;l<this.listaMateriales.size();l++){
                                    bw.newLine();
                                    bw.write(this.listaMateriales.get(l));
                                    bw.newLine();
                                    this.datos=p.hashMateriales.get(this.listaMateriales.get(l));
                                     System.out.println("0"+this.datos[0]);
                                      System.out.println("1"+this.datos[1]);
                                       System.out.println("2"+this.datos[2]);
                                    bw.write(String.valueOf(this.datos[0]));//valor
                                    bw.newLine();
                                    bw.write(String.valueOf(this.datos[1]));//cantidad
                                    bw.newLine();
                                    bw.write(String.valueOf(this.datos[2]));// 1 mat -- 2 MDF
                                    bw.newLine();
                                    bw.write("*");
                            }
                        bw.close();
              }
            } catch (IOException e) {
            }
          System.out.println("Producto creado");
    }
    public void importarDatosProductos() throws IOException {
        
        final File product = new File("Productos");
        for (final File ficheroEntrada : product.listFiles()) {
            if (ficheroEntrada.isFile()) {
                if(this.update==true){
                    this.updateListas();
                    //this.crearTXTProducto();
                    this.update=false;
                    System.out.println("UPDATE COMPLETE");
                }
                
            }
        }
    }
    
    public void calcularConsumoLuz(){
        int v=0;
        try {
            v=Integer.parseInt(this.jTextField5.getText());
        } catch(NumberFormatException e) {}
        if(v!=0){
            this.consumoLuz= this.luz*Integer.parseInt(this.jTextField5.getText());
            this.jLabel31.setText(Integer.toString(this.consumoLuz));
        }
    }
    
   
    public void setTablasMateriales(){
        System.out.println();
        this.jMater4.setListData(listaVisualCompleta);
        this.jMater5.setListData(ListaVisualCompletaPrecio);
        this.jMater3.setListData(listaVisualCompleta);
    }
    public void setearUsado(){
        this.jMater1.setListData(visualUsado);
    }
    public void setearTablasMDF(){
        //this.tablacontactosEditarLanchas.setListData(listadoClientes);
        this.jMater4.setListData(listaVisualCompleta);
        this.jMater5.setListData(ListaVisualCompletaPrecio);
        this.jMater3.setListData(listaVisualCompleta);
        this.jMater1.setListData(visualUsadoPrecio);
    }
    
       
    public void añadirUsado(){
        
        this.costo=0;
        this.cantidad=Integer.parseInt(jTextField3.getText());
        if(this.listaMateriales.contains(this.jMater4.getSelectedValue())){
            
            this.mate=this.hashMAT.get(this.jMater4.getSelectedValue());
            this.mate.setCantidad(this.cantidad);
            this.hashUtilizados.put(this.mate.getNombreTabla(), this.mate);
            this.listaUsado.add(this.mate.getNombreTabla());
            System.out.println("SE GUARDO MATERIAL: "+this.mate.getdatos());
            System.out.println("Original: "+this.hashMAT.get(this.jMater4.getSelectedValue()).getdatos());
            
        }
        if(this.listaMDF.contains(this.jMater4.getSelectedValue())){
            //this.costo=this.hashMDF.get(this.jMater4.getSelectedValue())*this.cantidad;
            this.mdf=this.hashMDF.get(this.jMater4.getSelectedValue());
            this.mdf.setCantidad(this.cantidad);
            this.hashUtilizados.put(this.mate.getNombreTabla(), this.mdf);
            this.listaUsado.add(this.mdf.getNombreTabla());
            System.out.println("SE GUARDO MATERIAL: "+this.mdf.getNombreTabla());
            System.out.println("Original: "+this.hashMDF.get(this.jMater4.getSelectedValue()).getNombreTabla());
        }
        this.jTextField3.setText("");
        this.updateListaUsados();
        
    }
    
    public void updateListaUsados(){
        
        this.visualUsado= new String[this.listaUsado.size()];
            for (int i=0; i<listaUsado.size(); i++) {
                 this.visualUsado[i]=listaUsado.get(i);//lista de usados
            }

        this.jMater1.setListData(visualUsado);
    }
        
    public void updateListas(){
        this.listaMDF= new  ArrayList<>();
        this.listaMateriales=new  ArrayList<>();
        this.listaCompleta=new  ArrayList<>();
        this.listaCompletaPrecio=new  ArrayList<>();
        
        this.hashMAT.entrySet().forEach((Map.Entry<String, Material> entry) -> {
            this.listaMateriales.add(entry.getKey());
            this.listaCompleta.add(entry.getKey());
            this.listaCompletaPrecio.add(entry.getKey()+":  $ "+this.hashMAT.get(entry.getKey()).precio);
            
        });
         this.hashMDF.entrySet().forEach((Map.Entry<String, MDF> entry) -> {
            this.listaMDF.add(entry.getKey());
            this.listaCompleta.add(entry.getKey());
            this.listaCompletaPrecio.add(entry.getKey()+":  $ "+this.hashMDF.get(entry.getKey()).precio);
            
        });
        System.out.println("Lista Completa: "+this.listaCompleta.toString());
        System.out.println("Lista Precio completa: "+this.listaCompletaPrecio.toString());
        System.out.println("Lista Materiales : "+this.listaMateriales.toString());

        
        this.listaVisualCompleta= new String[this.listaCompleta.size()];
        this.ListaVisualCompletaPrecio= new String[this.listaCompletaPrecio.size()];
      
        for (int i=0; i<this.listaCompleta.size(); i++) {
             this.listaVisualCompleta[i]=listaCompleta.get(i);
             this.ListaVisualCompletaPrecio[i]=listaCompletaPrecio.get(i);
        }
        System.out.println("SE ACTUALIZO");
        this.setTablasMateriales();
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new fondoPanel("met.jpg");
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTotal = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jMater1 = new javax.swing.JList<>();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        jMater4 = new javax.swing.JList<>();
        jButton9 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        porcentajeGanancia = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        lista = new fondoPanel("met.jpg");
        jScrollPane12 = new javax.swing.JScrollPane();
        jMater5 = new javax.swing.JList<>();
        jLabel26 = new javax.swing.JLabel();
        editar = new fondoPanel("met.jpg");
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        jMater3 = new javax.swing.JList<>();
        jButton4 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        botonBorrar = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        javax.swing.JPanel mdf = new fondoPanel("met.jpg");
        jButton10 = new javax.swing.JButton();
        inputAlto = new javax.swing.JTextField();
        inputAncho = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        gananciaMDF = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        valorMDF = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        visualInversion = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        valorsegmento = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        lista1 = new fondoPanel("met.jpg");
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        visualMDFPrecio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        visualMDFALto = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        visualMDFancho = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        visualMDFLuz = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jPanel2 = new fondoPanel("met.jpg");
        jLabel2 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jTotal1 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jMater6 = new javax.swing.JList<>();
        jButton13 = new javax.swing.JButton();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(95, 158, 160));

        jTabbedPane1.setBackground(new java.awt.Color(8, 69, 84));
        jTabbedPane1.setForeground(new java.awt.Color(242, 242, 242));
        jTabbedPane1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(95, 158, 160));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LISTA MATERIALES");

        jButton1.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/fd.png"))); // NOI18N
        jButton1.setToolTipText("Ingresar");
        jButton1.setAlignmentY(0.0F);
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/contabilidad.png"))); // NOI18N
        jButton2.setToolTipText("Calcular");
        jButton2.setAlignmentY(0.0F);
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jMater1.setBackground(new java.awt.Color(255, 255, 204));
        jMater1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jMater1.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jMater1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jMater1.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane8.setViewportView(jMater1);

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("N°");
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jTextField3.setBackground(new java.awt.Color(255, 255, 204));
        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jMater4.setBackground(new java.awt.Color(255, 255, 204));
        jMater4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jMater4.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jMater4.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jMater4.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane11.setViewportView(jMater4);

        jButton9.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/delete.png"))); // NOI18N
        jButton9.setToolTipText("Eliminar");
        jButton9.setAlignmentY(0.0F);
        jButton9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("MATERIALES UTILIZADOS");

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel17.setText("% Ganancia (40-50)");

        porcentajeGanancia.setBackground(new java.awt.Color(255, 255, 204));
        porcentajeGanancia.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        porcentajeGanancia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        porcentajeGanancia.setAlignmentX(2.0F);
        porcentajeGanancia.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel32.setBackground(new java.awt.Color(255, 255, 255));
        jLabel32.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("Total: $");

        jLabel33.setBackground(new java.awt.Color(255, 255, 204));
        jLabel33.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 102, 0));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setAlignmentX(2.0F);
        jLabel33.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel33.setOpaque(true);

        jLabel36.setBackground(new java.awt.Color(255, 255, 255));
        jLabel36.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Materiales: $");

        jLabel38.setBackground(new java.awt.Color(255, 255, 204));
        jLabel38.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setAlignmentX(2.0F);
        jLabel38.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel38.setOpaque(true);

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Inversion");

        jLabel39.setBackground(new java.awt.Color(255, 255, 255));
        jLabel39.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("MDF: $");

        jLabel41.setBackground(new java.awt.Color(255, 255, 204));
        jLabel41.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setAlignmentX(2.0F);
        jLabel41.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel41.setOpaque(true);

        jLabel42.setBackground(new java.awt.Color(255, 255, 204));
        jLabel42.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(51, 255, 0));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setAlignmentX(2.0F);
        jLabel42.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel42.setOpaque(true);

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Ganancias");

        jLabel43.setBackground(new java.awt.Color(255, 255, 255));
        jLabel43.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Materiales: $");

        jLabel30.setBackground(new java.awt.Color(255, 255, 255));
        jLabel30.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Valor Producto");

        jLabel44.setBackground(new java.awt.Color(255, 255, 204));
        jLabel44.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setAlignmentX(2.0F);
        jLabel44.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel44.setOpaque(true);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/save.png"))); // NOI18N
        jButton14.setToolTipText("Guardar");
        jButton14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(751, 751, 751))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(porcentajeGanancia, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel36)
                                    .addGap(9, 9, 9)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                        .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(porcentajeGanancia, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(29, 29, 29)
                                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane11)
                                    .addComponent(jScrollPane8))))
                        .addGap(0, 83, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTotal)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("CALCULADORA  ", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/calcular.png")), jPanel1); // NOI18N

        jTabbedPane2.setBackground(new java.awt.Color(8, 69, 84));
        jTabbedPane2.setForeground(new java.awt.Color(242, 242, 242));
        jTabbedPane2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        lista.setBackground(new java.awt.Color(95, 158, 160));

        jMater5.setBackground(new java.awt.Color(255, 255, 204));
        jMater5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jMater5.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jMater5.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jMater5.setToolTipText("");
        jMater5.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane12.setViewportView(jMater5);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("MATERIALES");

        javax.swing.GroupLayout listaLayout = new javax.swing.GroupLayout(lista);
        lista.setLayout(listaLayout);
        listaLayout.setHorizontalGroup(
            listaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(listaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addContainerGap(648, Short.MAX_VALUE))
        );
        listaLayout.setVerticalGroup(
            listaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("PRECIOS", lista);

        editar.setBackground(new java.awt.Color(95, 158, 160));
        editar.setForeground(new java.awt.Color(255, 255, 255));
        editar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Nombre:");

        jTextField1.setBackground(new java.awt.Color(255, 255, 204));
        jTextField1.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jTextField1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Precio:");

        jTextField2.setBackground(new java.awt.Color(255, 255, 204));
        jTextField2.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jTextField2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/edit.png"))); // NOI18N
        jButton3.setToolTipText("Editar");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jMater3.setBackground(new java.awt.Color(255, 255, 204));
        jMater3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jMater3.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jMater3.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jMater3.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jMater3.setVerifyInputWhenFocusTarget(false);
        jScrollPane10.setViewportView(jMater3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/save.png"))); // NOI18N
        jButton4.setToolTipText("Guardar");
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Nombre:");

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Precio:");

        jTextField4.setBackground(new java.awt.Color(255, 255, 204));
        jTextField4.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jTextField4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField6.setBackground(new java.awt.Color(255, 255, 204));
        jTextField6.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jTextField6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/anadir.png"))); // NOI18N
        jButton6.setToolTipText("Añadir");
        jButton6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        botonBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/delete.png"))); // NOI18N
        botonBorrar.setToolTipText("Eliminar");
        botonBorrar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBorrarActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("EDITAR");

        jLabel19.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("MATERIALES");

        jLabel18.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("AÑADIR");

        javax.swing.GroupLayout editarLayout = new javax.swing.GroupLayout(editar);
        editar.setLayout(editarLayout);
        editarLayout.setHorizontalGroup(
            editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jScrollPane10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editarLayout.createSequentialGroup()
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4)))
                    .addGroup(editarLayout.createSequentialGroup()
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(editarLayout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(editarLayout.createSequentialGroup()
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))))))
                .addContainerGap(156, Short.MAX_VALUE))
        );
        editarLayout.setVerticalGroup(
            editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editarLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField6)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(94, 94, 94)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editarLayout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(editarLayout.createSequentialGroup()
                                .addComponent(botonBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(180, 180, 180)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("EDITAR/AGREGAR/ELIMINAR", editar);

        mdf.setBackground(new java.awt.Color(95, 158, 160));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/contabilidad.png"))); // NOI18N
        jButton10.setToolTipText("Calcular");
        jButton10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        inputAlto.setBackground(new java.awt.Color(255, 255, 204));
        inputAlto.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        inputAlto.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputAlto.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        inputAlto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputAltoActionPerformed(evt);
            }
        });

        inputAncho.setBackground(new java.awt.Color(255, 255, 204));
        inputAncho.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        inputAncho.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputAncho.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        inputAncho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputAnchoActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Ancho:");

        jLabel28.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Alto:");

        jLabel29.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("MDF UTILIZADO");

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/anadir.png"))); // NOI18N
        jButton11.setToolTipText("Añadir");
        jButton11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("Dificultad:");

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Minutos Luz:");

        jTextField5.setBackground(new java.awt.Color(255, 255, 204));
        jTextField5.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel27.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("LUZ: $");

        jLabel31.setBackground(new java.awt.Color(255, 255, 204));
        jLabel31.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel31.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel31.setOpaque(true);

        jLabel40.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("MDF: $");

        gananciaMDF.setBackground(new java.awt.Color(255, 255, 204));
        gananciaMDF.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        gananciaMDF.setForeground(new java.awt.Color(51, 255, 0));
        gananciaMDF.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gananciaMDF.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        gananciaMDF.setOpaque(true);

        jComboBox1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Básico", "Intermedio", "Complejo" }));
        jComboBox1.setToolTipText("");
        jComboBox1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jComboBox1.setOpaque(true);

        jLabel34.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("GANANCIA");

        valorMDF.setBackground(new java.awt.Color(255, 255, 204));
        valorMDF.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        valorMDF.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        valorMDF.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        valorMDF.setOpaque(true);

        jLabel46.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel46.setText("MDF: $");

        jLabel47.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel47.setText("Total:");

        visualInversion.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        visualInversion.setForeground(new java.awt.Color(255, 102, 0));
        visualInversion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        visualInversion.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        visualInversion.setOpaque(true);

        jLabel45.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("VALOR");

        valorsegmento.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        valorsegmento.setForeground(new java.awt.Color(0, 0, 204));
        valorsegmento.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        valorsegmento.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        valorsegmento.setOpaque(true);

        jLabel48.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText(" INVERSION");

        javax.swing.GroupLayout mdfLayout = new javax.swing.GroupLayout(mdf);
        mdf.setLayout(mdfLayout);
        mdfLayout.setHorizontalGroup(
            mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mdfLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mdfLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(inputAncho)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                        .addComponent(inputAlto))))
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mdfLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(valorMDF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .addComponent(visualInversion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .addComponent(gananciaMDF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(mdfLayout.createSequentialGroup()
                                .addComponent(valorsegmento, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(220, Short.MAX_VALUE))
        );
        mdfLayout.setVerticalGroup(
            mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mdfLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(valorMDF, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(visualInversion, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(gananciaMDF, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(valorsegmento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputAlto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(mdfLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputAncho, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(mdfLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(306, 306, 306))))
        );

        jTabbedPane2.addTab("AGREGAR MDF", mdf);

        lista1.setBackground(new java.awt.Color(95, 158, 160));

        jLabel22.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("VALORES");

        jLabel24.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Precio:  $");

        visualMDFPrecio.setBackground(new java.awt.Color(255, 255, 204));
        visualMDFPrecio.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        visualMDFPrecio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        visualMDFPrecio.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        visualMDFPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualMDFPrecioActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Alto:");

        visualMDFALto.setBackground(new java.awt.Color(255, 255, 204));
        visualMDFALto.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        visualMDFALto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        visualMDFALto.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        visualMDFALto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualMDFALtoActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Ancho:");

        visualMDFancho.setBackground(new java.awt.Color(255, 255, 204));
        visualMDFancho.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        visualMDFancho.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        visualMDFancho.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        visualMDFancho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualMDFanchoActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Minuto Luz: $");

        visualMDFLuz.setBackground(new java.awt.Color(255, 255, 204));
        visualMDFLuz.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        visualMDFLuz.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        visualMDFLuz.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        visualMDFLuz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualMDFLuzActionPerformed(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/save.png"))); // NOI18N
        jButton12.setToolTipText("Guardar");
        jButton12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout lista1Layout = new javax.swing.GroupLayout(lista1);
        lista1.setLayout(lista1Layout);
        lista1Layout.setHorizontalGroup(
            lista1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lista1Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(lista1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lista1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(visualMDFPrecio)
                    .addGroup(lista1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(lista1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(visualMDFancho)
                            .addComponent(visualMDFALto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .addComponent(visualMDFLuz, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                .addContainerGap(583, Short.MAX_VALUE))
        );
        lista1Layout.setVerticalGroup(
            lista1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lista1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(lista1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(lista1Layout.createSequentialGroup()
                        .addComponent(visualMDFPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(lista1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(visualMDFALto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(lista1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(visualMDFancho, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(lista1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(visualMDFLuz, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(312, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("PLANCHA MDF", lista1);

        jTabbedPane1.addTab("MATERIALES  ", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/mat.png")), jTabbedPane2); // NOI18N

        jPanel2.setBackground(new java.awt.Color(95, 158, 160));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("LISTA PRODUCTOS");

        jButton5.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/fd.png"))); // NOI18N
        jButton5.setToolTipText("Ingresar");
        jButton5.setAlignmentY(0.0F);
        jButton5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jMater6.setBackground(new java.awt.Color(255, 255, 204));
        jMater6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jMater6.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jMater6.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jMater6.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane13.setViewportView(jMater6);

        jButton13.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/delete.png"))); // NOI18N
        jButton13.setToolTipText("Eliminar");
        jButton13.setAlignmentY(0.0F);
        jButton13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(751, 751, 751))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 84, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTotal1)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("PRODUCTOS  ", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/caja.png")), jPanel2, ""); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 979, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("PRODUCTOS");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(!"".equals(this.jTextField3.getText()) && Integer.parseInt(this.jTextField3.getText())>0){
            this.añadirUsado();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
            System.out.println("1");
        if(!"".equals(this.jTextField4.getText()) && !"".equals(this.jTextField6.getText())){
            System.out.println("Añadiendo: "+ this.jTextField6.getText()+"Valor: "+this.jTextField4.getText());
            Material mat=new Material(this.jTextField6.getText(),Integer.parseInt(this.jTextField4.getText()));
            this.hashMAT.put(this.jTextField6.getText(), mat);
            this.jTextField6.setText("");
            this.jTextField4.setText("");
            this.reescribirMateriales();
            //JOptionPane.showMessageDialog(null,"Agregado con exito");
           
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void botonBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBorrarActionPerformed
        selected=this.jMater3.getSelectedValue();

        if(hashMAT.containsKey(selected)==true){
            this.hashMAT.remove(selected);
            selected="";
        }
        if(hashMDF.containsKey(selected)==true){
            this.hashMDF.remove(selected);
            selected="";
        }
        this.updateListas();
        this.setTablasMateriales();
        this.crearTXTMateriales();
        
    }//GEN-LAST:event_botonBorrarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(hashMAT.containsKey(this.selected) || hashMDF.containsKey(this.selected) && !"".equals(this.jTextField1.getText()) && !"".equals(this.jTextField2.getText())){
            if(this.editMAT==true){
                System.out.println("selected mat: "+this.selected);
                this.listaMateriales.remove(selected);
                hashMAT.remove(selected);
                selected="";
                Material mat=new Material(this.jTextField1.getText(),Integer.parseInt(this.jTextField2.getText()));
                hashMAT.put(this.jTextField1.getText(), mat);
                //this.listaMateriales.add(this.jTextField1.getText());
                this.jTextField1.setText("");
                this.jTextField2.setText("");
                this.editMAT=false;
                
            }
            if(this.editMDF==true){
                System.out.println("selected mdf: "+this.selected);
                this.listaMDF.remove(selected);
                hashMDF.remove(selected);
                selected="";
                //MDF mdf=new MDF(this.jTextField1.getText(),this.jTextField2.getText()));
                //hashMDF.put(, Integer.parseInt();
                //this.listaMDF.add(this.jTextField1.getText());
                this.jTextField1.setText("");
                this.jTextField2.setText("");
                this.editMDF=false;
            }
            
            this.updateListas();
            this.setTablasMateriales();
            this.crearTXTMateriales();
            //JOptionPane.showMessageDialog(null,"Editado con exito");
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(this.jMater3.getSelectedValue()!=null){
            if(this.listaMateriales.contains(this.jMater3.getSelectedValue())){
                this.mate=this.hashMAT.get(this.jMater3.getSelectedValue());
                this.selected= this.jMater3.getSelectedValue();
                System.out.println("SELECTED MAT: "+this.jMater3.getSelectedValue());
                jTextField1.setText(selected);
                jTextField2.setText(String.valueOf(mate.precio));//extraemos precio
                this.editMAT=true;
                
            }
            if(this.listaMDF.contains(this.jMater3.getSelectedValue())){
                this.mdf=this.hashMDF.get(this.jMater3.getSelectedValue());
                this.selected= this.jMater3.getSelectedValue();
                System.out.println("SELECTED MDF: "+this.jMater3.getSelectedValue());
                jTextField1.setText(selected);
                jTextField2.setText(String.valueOf(mdf.precio));//extraemos precio
                this.editMDF=true;
            }
            
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
      
       
       selected=this.jMater1.getSelectedValue();
       System.out.println("SIZE lista Usado: "+this.listaUsado.size());
       System.out.println("SIZE hash usado: "+this.hashUtilizados.size());
       System.out.println("tratando de eliminar: "+selected);
       if(hashUtilizados.containsKey(selected)){
            this.hashUtilizados.remove(selected);
            this.listaUsado.remove(selected);
            selected="";
            this.updateListaUsados();
            //JOptionPane.showMessageDialog(null,"Eliminado con exito");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.calcularTotal();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void visualMDFLuzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualMDFLuzActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualMDFLuzActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        this.mdfValor=Integer.parseInt(this.visualMDFPrecio.getText());
        this.mdfAlto=Integer.parseInt(this.visualMDFALto.getText());
        this.mdfAncho=Integer.parseInt(this.visualMDFancho.getText());
        this.luz=Integer.parseInt(this.visualMDFLuz.getText());
        this.jLabel31.setText(this.visualMDFLuz.getText());
        this.updateListas();
        this.setTablasMateriales();
        this.crearTXTMateriales();
        JOptionPane.showMessageDialog(null,"Editado con exito");
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if(!"".equals(this.inputAlto.getText()) && !"".equals(this.inputAncho.getText())){
            System.out.println("EL MDF VALE: $ "+this.mdf.precio);
            this.hashMDF.put(this.mdf.nombre, this.mdf);
            this.crearTXTMDf();
            this.importarMDF();
            this.inputAlto.setText("");
            this.inputAncho.setText("");
            this.jTextField5.setText("");
            this.jLabel31.setText("");
            this.gananciaMDF.setText("");
            this.valorsegmento.setText("");
            this.valorMDF.setText("");
            this.visualInversion.setText("");
            this.setTablasMateriales();
            
            //this.reescribirTXTMateriales();
          
            JOptionPane.showMessageDialog(null,"Agregado con exito");
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void inputAnchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputAnchoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputAnchoActionPerformed

    private void inputAltoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputAltoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputAltoActionPerformed

    private void visualMDFPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualMDFPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualMDFPrecioActionPerformed

    private void visualMDFanchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualMDFanchoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualMDFanchoActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if(!"".equals(this.visualMDFALto.getText()) && !"".equals(this.visualMDFancho.getText())
            &&!"".equals(this.visualMDFPrecio.getText()) &&!"".equals(this.inputAlto.getText()) &&!"".equals(this.inputAncho.getText())){

            //PANEL COMPLETO
            int p=Integer.parseInt(this.visualMDFPrecio.getText());
            int x=Integer.parseInt(this.visualMDFALto.getText());
            int y=Integer.parseInt(this.visualMDFancho.getText());
            int a=x*y;

            //SEGMENTO
            int x1=Integer.parseInt(this.inputAlto.getText());
            int y1=Integer.parseInt(this.inputAncho.getText());
            int a1=x1*y1;
            //VALOR SEGMENTO
            this.val=(a1*p)/a;

            //valor ganancia
            System.out.println("DIFICULPTAD: "+Arrays.toString(this.dificultad));
            
            int sel = this.jComboBox1.getSelectedIndex();
            System.out.println("posicion: "+sel);
            if(sel==0){
                this. valorMdfGanancia=((this.dificultad[sel])*this.val)/100;
            }
            if(sel==1){
                this. valorMdfGanancia=((this.dificultad[sel])*this.val)/100;      
            }
            if(sel==2){
                this. valorMdfGanancia=((this.dificultad[sel])*this.val)/100; 
            }
            
            this.gananciaMDF.setText(Integer.toString(valorMdfGanancia));
            this.calcularConsumoLuz();
            
            this.inversionCorte=this.val+this.consumoLuz;
            this.valorMDF.setText(Integer.toString(this.val));
            
            
            //set visual ganancia
            //set visual inversion
            this.visualInversion.setText(Integer.toString(this.inversionCorte));
            
            //set visual producto
            this.valorsegmento.setText(Integer.toString(this.valorMdfGanancia+this.inversionCorte));
            
            this.mdf=new MDF("MDF "+this.inputAlto.getText()+" X "+this.inputAncho.getText(),(this.valorMdfGanancia+this.inversionCorte),this.val, x1, y1);
            
            
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void visualMDFALtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualMDFALtoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualMDFALtoActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        String nomb=JOptionPane.showInputDialog("Nombre del producto: ");
        
        //Producto p=new Producto(nomb,this.total,this.);
        //this.crearTXTProducto(p);
        //producto.visualizarMateriales();
       
    }//GEN-LAST:event_jButton14ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new Interfaz().setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonBorrar;
    private javax.swing.JPanel editar;
    private javax.swing.JLabel gananciaMDF;
    private javax.swing.JTextField inputAlto;
    private javax.swing.JTextField inputAncho;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    public javax.swing.JList<String> jMater1;
    public javax.swing.JList<String> jMater3;
    public javax.swing.JList<String> jMater4;
    public javax.swing.JList<String> jMater5;
    public javax.swing.JList<String> jMater6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel jTotal;
    private javax.swing.JLabel jTotal1;
    private javax.swing.JPanel lista;
    private javax.swing.JPanel lista1;
    private javax.swing.JTextField porcentajeGanancia;
    private javax.swing.JLabel valorMDF;
    private javax.swing.JLabel valorsegmento;
    private javax.swing.JLabel visualInversion;
    private javax.swing.JTextField visualMDFALto;
    private javax.swing.JTextField visualMDFLuz;
    private javax.swing.JTextField visualMDFPrecio;
    private javax.swing.JTextField visualMDFancho;
    // End of variables declaration//GEN-END:variables
}
