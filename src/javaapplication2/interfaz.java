
package javaapplication2;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public final class interfaz extends javax.swing.JFrame {
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
    int win=0;
    
    boolean update=false;
    boolean load=false;
    boolean editMDF=false;
    boolean editMAT=false;
    
    String selected="";
    String nom="";
    
    HashMap<String, Producto> hashProductos = new HashMap<>();//matriz de productos
    HashMap<String, Producto> hashUtilizados = new HashMap<>();//Matriz de materiales usados
    
    Integer[] dificultad= new Integer[6];
    
    String[] vacio= new String[0];
    String[] visualProducto= new String[100];
    String[] visualProductoUsado= new String[100];
    
    ArrayList<String> listaMDF= new ArrayList<>();
    ArrayList<String> listaUsado= new ArrayList<>();
    ArrayList<String> listaProductosPrecio= new ArrayList<>();
    ArrayList<String> listaProductos= new ArrayList<>();
    
    
    
    public interfaz() throws IOException {
        initComponents();
        this.setVisible(true);
        this.setTitle("DC LASER V1.0");
        Image logo = new ImageIcon(getClass().getResource("1.jpg")).getImage();
        this.setIconImage(logo);
        this.setResizable(false);
        
        //creamos carpeta productos
        File carpetaProductos = new File("Productos");
        carpetaProductos.mkdirs();
        
        this.dificultad[0]=721;
        this.dificultad[1]=4400;
        this.dificultad[2]=5300;
        this.dificultad[3]=6100;
        this.dificultad[4]=6800;
        this.dificultad[5]=7427;
        
        this.gestionarConfig();
        this.gestionarProductos();
   
    }
    public void vaciarTablaMatUsados(){
        String[] vacio= new String[0];
        this.tablaCalculadoraUsados.setListData(vacio);
        
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
    public void addProduct(Producto p){
        this.hashProductos.put(p.getNombreTabla(),p);
        
    }
    public Producto getProducto(String key){
        return this.hashProductos.get(key);
        
    }
    public void crearTXTConfig(){
         try {
            File aux = new File("Config.txt");
            aux.delete();
            File file = new File("Config.txt");
            FileWriter fw = new FileWriter(file);
            System.out.println("CREANDO TXT config");
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                    //while minetras exista linea
                    bw.write(String.valueOf(this.mdfAlto));
                    bw.newLine();
                    bw.write(String.valueOf(this.mdfAncho));
                    bw.newLine();
                    bw.write(String.valueOf(this.mdfValor));
                    bw.newLine();
                    bw.write(String.valueOf(this.luz));
                    bw.close();
              }
            } catch (IOException e) {
            }
          System.out.println("TXT Congif CREADO");
          this.updateListaProductos();
    }
    public void crearTXTProductos(){
         try {
            File aux = new File("Productos/Productos.txt");
            aux.delete();
            File file = new File("Productos/Productos.txt");
            FileWriter fw = new FileWriter(file);
            System.out.println("CREANDO TXT PRODUCTO");
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                    //while minetras exista linea
                  for(int l=0;l<this.listaProductos.size();l++){
                                this.prod=this.hashProductos.get(this.listaProductos.get(l));
                                bw.write("*");
                                bw.newLine();
                                bw.write(this.prod.getNombre());
                                bw.newLine();
                                bw.write(String.valueOf(this.prod.precio));
                                bw.newLine();
                    }
                    bw.close();
              }
            } catch (IOException e) {
            }
          System.out.println("TXT CREADO");
          this.updateListaProductos();
    }
    public void importarDatosProducto(File f){
        this.hashProductos=new HashMap<>();
        BufferedReader entrada = null; 
        try {
            entrada = new BufferedReader( new FileReader( f ) );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while(entrada.ready()){
                if("*".equals(entrada.readLine())){
                    this.prod=new Producto();
                    this.prod.setNombre(entrada.readLine());//nombre
                    System.out.println("prod: "+this.prod.getNombre());
                    this.prod.setPrecio(Integer.parseInt(entrada.readLine()));//precio
                    System.out.println("precio: "+this.prod.getPrecio());
                    this.hashProductos.put(this.prod.getNombre(), this.prod);
                }
                
            }
        } catch (IOException ex) {            
            Logger.getLogger(interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
                        
                        this.updateListaProductos();
                        this.crearTXTProductos();
    }
     public void importarDatosConfig(File f){
        BufferedReader entrada = null; 
        try {
            entrada = new BufferedReader( new FileReader( f ) );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while(entrada.ready()){
               this.mdfAlto=Integer.parseInt(entrada.readLine());//alto
               this.mdfAncho=Integer.parseInt(entrada.readLine());//ancho
               this.mdfValor=Integer.parseInt(entrada.readLine());//precio
               this.luz=Integer.parseInt(entrada.readLine());//luz
            }
        } catch (IOException ex) {            
            Logger.getLogger(interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
                        
        this.setearConfig();
    }                   
                    
                    
  public void gestionarConfig(){
        final File confi = new File("Config.txt");
        if(confi.exists()){
            System.out.println("Importando");
            this.importarDatosConfig(confi);
            this.updateListaProductos();
            System.out.println("Importado ok");
        }else{
            this.crearTXTConfig();
            this.gestionarConfig();
        }
    
    }
    public void gestionarProductos(){
        System.out.println("Operando Productos");
        final File product = new File("Productos/Productos.txt");
        if(product.exists()){
            System.out.println("Importando");
            this.importarDatosProducto(product);
            this.updateListaProductos();
            System.out.println("Importado ok");
        }else{
            this.crearTXTProductos();
        }
    
    }
    public void inicializarTXTConfig(){
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
    public void updateListaProductos(){
        this.listaProductos=new ArrayList<>();
        this.hashProductos.entrySet().forEach((Map.Entry<String, Producto> entry) -> {
            this.listaProductos.add(entry.getKey());
            
        });
        Collections.sort(this.listaProductos); 
        this.visualProducto= new String[this.listaProductos.size()];
            for (int i=0; i<listaProductos.size(); i++) {
                 this.visualProducto[i]=listaProductos.get(i);//lista de usados
            }
        this.verTabla.setListData(visualProducto);
        this.añadirTabla.setListData(visualProducto);
        this.eliminarTabla.setListData(visualProducto);
        this.editarTabla.setListData(visualProducto);
        this.tablaCalculadoraMateriales.setListData(visualProducto);
        
        
    }
    public void setearConfig(){
        this.visualMDFALto.setText(Integer.toString(this.mdfAlto));
        this.visualMDFancho.setText(Integer.toString(this.mdfAncho));
        this.visualMDFPrecio.setText(Integer.toString(this.mdfValor));
        this.visualMDFLuz.setText(Integer.toString(this.luz));
        
    }
    public void calcularConsumoLuz(){
        int v=0;
        try {
            v=Integer.parseInt(this.jTextField5.getText());
        } catch(NumberFormatException e) {}
        if(v!=0){
            this.consumoLuz= this.luz*Integer.parseInt(this.jTextField5.getText());
            this.visualLuzCorte.setText(Integer.toString(this.consumoLuz));
        }
    }
  
    
    public void setearUsado(){
    }
    
    public void setTablaUsados(){
    }
    
    public void añadirUsado(){
        this.cantidad=Integer.parseInt(jTextField3.getText());
        if(this.listaProductos.contains(this.tablaCalculadoraMateriales.getSelectedValue())){
            this.prod =new Producto();
            this.prod.setNombre(this.hashProductos.get(this.tablaCalculadoraMateriales.getSelectedValue()).getNombre());
            this.prod.setPrecio(this.hashProductos.get(this.tablaCalculadoraMateriales.getSelectedValue()).getPrecio());
            this.hashUtilizados.put(this.prod.getNombreTabla(), this.prod);
        }
        this.jTextField3.setText("");
        this.updateListaUsados();
    }
    public void vaciarInputsCalcular(){
        this.totalPedido.setText("");
    }
    
    public void updateListaUsados(){
            this.listaUsado= new ArrayList<>();
            this.hashUtilizados.entrySet().forEach((Map.Entry<String, Producto> entry) -> {
                if(entry.getValue() instanceof Producto){
                    this.listaUsado.add(entry.getKey());//recorremos y extraemos las keys
                }
            });
            Collections.sort(this.listaUsado);  
            this.visualProductoUsado= new String[this.listaUsado.size()];
            for (int i=0; i<listaUsado.size(); i++) {
                 this.visualProductoUsado[i]=listaUsado.get(i);//copamos keys a un arreglo
            }
            this.tablaCalculadoraUsados.setListData(visualProductoUsado);
    }
        
 
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        venta = new javax.swing.JTabbedPane();
        pedidos = new javax.swing.JTabbedPane();
        cotizar = new fondoPanel("met.jpg");
        jTotal = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tablaCalculadoraMateriales = new javax.swing.JList<>();
        jTextField3 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        tablaCalculadoraUsados = new javax.swing.JList<>();
        jLabel32 = new javax.swing.JLabel();
        totalPedido = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        ver1 = new fondoPanel("met.jpg");
        jLabel9 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jTotal2 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        tablaProductos1 = new javax.swing.JList<>();
        jButton17 = new javax.swing.JButton();
        jScrollPane17 = new javax.swing.JScrollPane();
        tablaProductosMateriales1 = new javax.swing.JList<>();
        jLabel14 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        productos = new javax.swing.JTabbedPane();
        ver = new fondoPanel("met.jpg");
        jScrollPane19 = new javax.swing.JScrollPane();
        verTabla = new javax.swing.JList<>();
        jLabel38 = new javax.swing.JLabel();
        añadir = new fondoPanel("met.jpg");
        jScrollPane15 = new javax.swing.JScrollPane();
        añadirTabla = new javax.swing.JList<>();
        jLabel35 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        editar = new fondoPanel("met.jpg");
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        editarTabla = new javax.swing.JList<>();
        jButton4 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        eliminar = new fondoPanel("met.jpg");
        jScrollPane12 = new javax.swing.JScrollPane();
        eliminarTabla = new javax.swing.JList<>();
        jLabel33 = new javax.swing.JLabel();
        botonBorrar = new javax.swing.JButton();
        laser = new fondoPanel("met.jpg");
        javax.swing.JPanel grabar = new fondoPanel("met.jpg");
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
        visualLuzCorte = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        gananciaMDF = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        valorMDF = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        visualInversion = new javax.swing.JLabel();
        valorsegmento = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        config = new fondoPanel("met.jpg");
        establecerValores = new fondoPanel("met.jpg");
        jLabel24 = new javax.swing.JLabel();
        visualMDFPrecio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        visualMDFALto = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        visualMDFancho = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        visualMDFLuz = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(95, 158, 160));

        venta.setBackground(new java.awt.Color(8, 69, 84));
        venta.setForeground(new java.awt.Color(242, 242, 242));
        venta.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        pedidos.setBackground(new java.awt.Color(8, 69, 84));
        pedidos.setForeground(new java.awt.Color(242, 242, 242));
        pedidos.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        cotizar.setBackground(new java.awt.Color(95, 158, 160));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PRODUCTOS");

        tablaCalculadoraMateriales.setBackground(new java.awt.Color(255, 255, 204));
        tablaCalculadoraMateriales.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tablaCalculadoraMateriales.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        tablaCalculadoraMateriales.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        tablaCalculadoraMateriales.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane11.setViewportView(tablaCalculadoraMateriales);

        jTextField3.setBackground(new java.awt.Color(255, 255, 204));
        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("N°");
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.TOP);

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

        jButton9.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/delete.png"))); // NOI18N
        jButton9.setToolTipText("Eliminar");
        jButton9.setAlignmentY(0.0F);
        jButton9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        tablaCalculadoraUsados.setBackground(new java.awt.Color(255, 255, 204));
        tablaCalculadoraUsados.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tablaCalculadoraUsados.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        tablaCalculadoraUsados.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        tablaCalculadoraUsados.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane8.setViewportView(tablaCalculadoraUsados);

        jLabel32.setBackground(new java.awt.Color(255, 255, 255));
        jLabel32.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText("TOTAL");

        totalPedido.setBackground(new java.awt.Color(255, 255, 204));
        totalPedido.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        totalPedido.setForeground(new java.awt.Color(51, 255, 0));
        totalPedido.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalPedido.setAlignmentX(2.0F);
        totalPedido.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        totalPedido.setOpaque(true);

        jLabel44.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel44.setText("CALCULAR");

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/calcular.png"))); // NOI18N
        jButton16.setToolTipText("Guardar");
        jButton16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("GUARDAR");

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/add.png"))); // NOI18N
        jButton14.setToolTipText("Guardar");
        jButton14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/carrito-de-compras.png"))); // NOI18N
        jButton15.setToolTipText("Guardar");
        jButton15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton15.setPreferredSize(new java.awt.Dimension(30, 30));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cotizarLayout = new javax.swing.GroupLayout(cotizar);
        cotizar.setLayout(cotizarLayout);
        cotizarLayout.setHorizontalGroup(
            cotizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cotizarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1477, Short.MAX_VALUE))
            .addGroup(cotizarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cotizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cotizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cotizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cotizarLayout.createSequentialGroup()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(cotizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(totalPedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(cotizarLayout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(601, Short.MAX_VALUE))
        );
        cotizarLayout.setVerticalGroup(
            cotizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cotizarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cotizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cotizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cotizarLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(cotizarLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jTotal)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pedidos.addTab("COTIZAR", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/calculadora.png")), cotizar); // NOI18N

        ver1.setBackground(new java.awt.Color(95, 158, 160));

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("PEDIDOS REALIZADOS");

        jButton7.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/fd.png"))); // NOI18N
        jButton7.setToolTipText("Ingresar");
        jButton7.setAlignmentY(0.0F);
        jButton7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        tablaProductos1.setBackground(new java.awt.Color(255, 255, 204));
        tablaProductos1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tablaProductos1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        tablaProductos1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        tablaProductos1.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane16.setViewportView(tablaProductos1);

        jButton17.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/delete.png"))); // NOI18N
        jButton17.setToolTipText("Eliminar");
        jButton17.setAlignmentY(0.0F);
        jButton17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        tablaProductosMateriales1.setBackground(new java.awt.Color(255, 255, 204));
        tablaProductosMateriales1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tablaProductosMateriales1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        tablaProductosMateriales1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        tablaProductosMateriales1.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane17.setViewportView(tablaProductosMateriales1);

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("DETALLE");

        jButton18.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/dust.png"))); // NOI18N
        jButton18.setToolTipText("Eliminar");
        jButton18.setAlignmentY(0.0F);
        jButton18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ver1Layout = new javax.swing.GroupLayout(ver1);
        ver1.setLayout(ver1Layout);
        ver1Layout.setHorizontalGroup(
            ver1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ver1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ver1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(ver1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(ver1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ver1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTotal2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(751, 751, 751))
                    .addGroup(ver1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(ver1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        ver1Layout.setVerticalGroup(
            ver1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ver1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ver1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ver1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ver1Layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addGap(18, 18, 18)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                    .addComponent(jScrollPane16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTotal2)
                .addContainerGap())
        );

        pedidos.addTab("REGISTRO DE PEDIDOS", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/lista-de-quehaceres.png")), ver1, ""); // NOI18N

        venta.addTab("VENTA", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/contabilidad.png")), pedidos); // NOI18N

        productos.setBackground(new java.awt.Color(8, 69, 84));
        productos.setForeground(new java.awt.Color(242, 242, 242));
        productos.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        ver.setBackground(new java.awt.Color(95, 158, 160));
        ver.setForeground(new java.awt.Color(255, 255, 255));
        ver.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        verTabla.setBackground(new java.awt.Color(255, 255, 204));
        verTabla.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        verTabla.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        verTabla.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        verTabla.setSelectionBackground(new java.awt.Color(153, 153, 153));
        verTabla.setVerifyInputWhenFocusTarget(false);
        jScrollPane19.setViewportView(verTabla);

        jLabel38.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("PRODUCTOS");

        javax.swing.GroupLayout verLayout = new javax.swing.GroupLayout(ver);
        ver.setLayout(verLayout);
        verLayout.setHorizontalGroup(
            verLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(verLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(verLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jScrollPane19))
                .addContainerGap(1190, Short.MAX_VALUE))
        );
        verLayout.setVerticalGroup(
            verLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(verLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        productos.addTab("LISTA", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/lista-de-quehaceres.png")), ver); // NOI18N

        añadir.setBackground(new java.awt.Color(95, 158, 160));
        añadir.setForeground(new java.awt.Color(255, 255, 255));
        añadir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        añadirTabla.setBackground(new java.awt.Color(255, 255, 204));
        añadirTabla.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        añadirTabla.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        añadirTabla.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        añadirTabla.setSelectionBackground(new java.awt.Color(153, 153, 153));
        añadirTabla.setVerifyInputWhenFocusTarget(false);
        jScrollPane15.setViewportView(añadirTabla);

        jLabel35.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("PRODUCTOS");

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Nombre:");

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Precio:");

        jTextField4.setBackground(new java.awt.Color(255, 255, 204));
        jTextField4.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jTextField4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/save.png"))); // NOI18N
        jButton6.setToolTipText("Añadir");
        jButton6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jTextField6.setBackground(new java.awt.Color(255, 255, 204));
        jTextField6.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jTextField6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("AÑADIR");

        javax.swing.GroupLayout añadirLayout = new javax.swing.GroupLayout(añadir);
        añadir.setLayout(añadirLayout);
        añadirLayout.setHorizontalGroup(
            añadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(añadirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(añadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jScrollPane15))
                .addGroup(añadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(añadirLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(añadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(añadirLayout.createSequentialGroup()
                                .addGroup(añadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(añadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField6)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(añadirLayout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(735, Short.MAX_VALUE))
        );
        añadirLayout.setVerticalGroup(
            añadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(añadirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(añadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(añadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(añadirLayout.createSequentialGroup()
                        .addGroup(añadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField6)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(añadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        productos.addTab("AÑADIR", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/add.png")), añadir); // NOI18N

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

        editarTabla.setBackground(new java.awt.Color(255, 255, 204));
        editarTabla.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        editarTabla.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        editarTabla.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        editarTabla.setSelectionBackground(new java.awt.Color(153, 153, 153));
        editarTabla.setVerifyInputWhenFocusTarget(false);
        jScrollPane10.setViewportView(editarTabla);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/save.png"))); // NOI18N
        jButton4.setToolTipText("Guardar");
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("DATOS");

        jLabel19.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("PRODUCTOS");

        javax.swing.GroupLayout editarLayout = new javax.swing.GroupLayout(editar);
        editar.setLayout(editarLayout);
        editarLayout.setHorizontalGroup(
            editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jScrollPane10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(editarLayout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(735, Short.MAX_VALUE))
        );
        editarLayout.setVerticalGroup(
            editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editarLayout.createSequentialGroup()
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        productos.addTab("EDITAR", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/edit.png")), editar); // NOI18N

        eliminar.setBackground(new java.awt.Color(95, 158, 160));
        eliminar.setForeground(new java.awt.Color(255, 255, 255));
        eliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        eliminarTabla.setBackground(new java.awt.Color(255, 255, 204));
        eliminarTabla.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        eliminarTabla.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        eliminarTabla.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        eliminarTabla.setSelectionBackground(new java.awt.Color(153, 153, 153));
        eliminarTabla.setVerifyInputWhenFocusTarget(false);
        jScrollPane12.setViewportView(eliminarTabla);

        jLabel33.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("PRODUCTOS");

        botonBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/delete.png"))); // NOI18N
        botonBorrar.setToolTipText("Eliminar");
        botonBorrar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBorrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout eliminarLayout = new javax.swing.GroupLayout(eliminar);
        eliminar.setLayout(eliminarLayout);
        eliminarLayout.setHorizontalGroup(
            eliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eliminarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(eliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jScrollPane12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1132, Short.MAX_VALUE))
        );
        eliminarLayout.setVerticalGroup(
            eliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eliminarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(eliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        productos.addTab("ELIMINAR", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/delete.png")), eliminar); // NOI18N

        laser.setBackground(new java.awt.Color(95, 158, 160));

        grabar.setBackground(new java.awt.Color(95, 158, 160));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/calcular.png"))); // NOI18N
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
        jLabel29.setText("DIMENSIONES MDF");

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/add.png"))); // NOI18N
        jButton11.setToolTipText("Añadir");
        jButton11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("Tipo:");

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Minutos Luz:");

        jTextField5.setBackground(new java.awt.Color(255, 255, 204));
        jTextField5.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel27.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("LUZ: $");

        visualLuzCorte.setBackground(new java.awt.Color(255, 255, 204));
        visualLuzCorte.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        visualLuzCorte.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        visualLuzCorte.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        visualLuzCorte.setOpaque(true);

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
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Solo Corte", "Gr Básico", "Gr Simple", "Gr Intermedio", "Gr Difícil", "Gr Complejo" }));
        jComboBox1.setToolTipText("");
        jComboBox1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

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
        jLabel47.setText("Total: $");

        visualInversion.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        visualInversion.setForeground(new java.awt.Color(255, 102, 0));
        visualInversion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        visualInversion.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        visualInversion.setOpaque(true);

        valorsegmento.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        valorsegmento.setForeground(new java.awt.Color(0, 0, 204));
        valorsegmento.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        valorsegmento.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        valorsegmento.setOpaque(true);

        jLabel48.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText(" INVERSION");

        jLabel41.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Total: $");

        jLabel42.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Calcular");

        jLabel43.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Guardar");

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Cantidad:");

        jTextField7.setBackground(new java.awt.Color(255, 255, 204));
        jTextField7.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        javax.swing.GroupLayout grabarLayout = new javax.swing.GroupLayout(grabar);
        grabar.setLayout(grabarLayout);
        grabarLayout.setHorizontalGroup(
            grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grabarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(grabarLayout.createSequentialGroup()
                        .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                                .addComponent(jLabel40)
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(grabarLayout.createSequentialGroup()
                                    .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel42)
                                        .addGroup(grabarLayout.createSequentialGroup()
                                            .addGap(12, 12, 12)
                                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(18, 18, 18)
                                    .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel43)
                                        .addGroup(grabarLayout.createSequentialGroup()
                                            .addGap(12, 12, 12)
                                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(0, 4, Short.MAX_VALUE))
                                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(gananciaMDF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(valorsegmento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(grabarLayout.createSequentialGroup()
                                .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                    .addComponent(inputAncho, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(inputAlto, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(grabarLayout.createSequentialGroup()
                                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(visualInversion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(grabarLayout.createSequentialGroup()
                                        .addComponent(jLabel46)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(valorMDF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(grabarLayout.createSequentialGroup()
                                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(visualLuzCorte, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)))))))
                    .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(grabarLayout.createSequentialGroup()
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, grabarLayout.createSequentialGroup()
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(937, Short.MAX_VALUE))
        );
        grabarLayout.setVerticalGroup(
            grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grabarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(grabarLayout.createSequentialGroup()
                        .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(grabarLayout.createSequentialGroup()
                                .addComponent(inputAlto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(inputAncho, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(valorMDF, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(grabarLayout.createSequentialGroup()
                                            .addGap(44, 44, 44)
                                            .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(visualInversion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(grabarLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(grabarLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(visualLuzCorte, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(gananciaMDF, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(valorsegmento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(grabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(grabarLayout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(grabarLayout.createSequentialGroup()
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(114, 114, 114))
        );

        javax.swing.GroupLayout laserLayout = new javax.swing.GroupLayout(laser);
        laser.setLayout(laserLayout);
        laserLayout.setHorizontalGroup(
            laserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1527, Short.MAX_VALUE)
            .addGroup(laserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(laserLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(grabar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        laserLayout.setVerticalGroup(
            laserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 584, Short.MAX_VALUE)
            .addGroup(laserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(laserLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(grabar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        productos.addTab("CORTE LASER", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/laser.png")), laser, ""); // NOI18N

        venta.addTab("PRODUCTOS", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/carpenter.png")), productos); // NOI18N

        config.setBackground(new java.awt.Color(95, 158, 160));

        establecerValores.setBackground(new java.awt.Color(95, 158, 160));

        jLabel24.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Valor:  $");

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
        jLabel15.setText("Minuto: $");

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

        jLabel64.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel64.setText("LUZ");

        jLabel65.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("PLANCHA MDF");

        javax.swing.GroupLayout establecerValoresLayout = new javax.swing.GroupLayout(establecerValores);
        establecerValores.setLayout(establecerValoresLayout);
        establecerValoresLayout.setHorizontalGroup(
            establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(establecerValoresLayout.createSequentialGroup()
                .addGroup(establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, establecerValoresLayout.createSequentialGroup()
                            .addGap(142, 142, 142)
                            .addGroup(establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(visualMDFancho)
                                .addComponent(visualMDFALto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                .addComponent(jLabel65, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(establecerValoresLayout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addGroup(establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(establecerValoresLayout.createSequentialGroup()
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(visualMDFPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(establecerValoresLayout.createSequentialGroup()
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(visualMDFLuz, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                        .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
                .addContainerGap(542, Short.MAX_VALUE))
        );
        establecerValoresLayout.setVerticalGroup(
            establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(establecerValoresLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(visualMDFALto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(visualMDFancho, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(visualMDFPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(establecerValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(visualMDFLuz, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(227, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout configLayout = new javax.swing.GroupLayout(config);
        config.setLayout(configLayout);
        configLayout.setHorizontalGroup(
            configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(establecerValores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        configLayout.setVerticalGroup(
            configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(establecerValores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 76, Short.MAX_VALUE))
        );

        venta.addTab("CONFIGURACION", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/config.png")), config, ""); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(venta, javax.swing.GroupLayout.PREFERRED_SIZE, 984, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(venta, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        venta.getAccessibleContext().setAccessibleName("PRODUCTOS");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        boolean isNumeric =  this.jTextField3.getText().matches("[+-]?\\d*(\\.\\d+)?");
        if(!"".equals(this.jTextField3.getText()) && isNumeric==true){
            this.añadirUsado();
            this.tablaCalculadoraMateriales.clearSelection();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
       this.selected=this.tablaCalculadoraUsados.getSelectedValue();
       System.out.println("SIZE lista Usado: "+this.listaUsado.size());
       System.out.println("SIZE hash usado: "+this.hashUtilizados.size());
       System.out.println("tratando de eliminar: "+selected);
       if(this.hashUtilizados.containsKey(selected)){
            this.hashUtilizados.remove(selected);
            this.listaUsado.remove(selected);
            selected="";
            this.updateListaUsados();
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        //guardar cotizacion
        if(this.prod.precio!=0){
            ImageIcon icon = new ImageIcon("save.png");
            this.prod.setNombre((String) JOptionPane.showInputDialog(null, "Nombre: ", "Guardar", JOptionPane.INFORMATION_MESSAGE, icon, null,""));
            this.crearTXTProductos();
            this.vaciarTablaMatUsados();
            this.prod=new Producto();
            this.hashUtilizados=new HashMap<>();
            this.gestionarProductos();
            this.updateListaProductos();
            this.updateListaUsados();
            this.vaciarInputsCalcular();
            
            //producto.visualizarMateriales();
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        this.mdfValor=Integer.parseInt(this.visualMDFPrecio.getText());
        this.mdfAlto=Integer.parseInt(this.visualMDFALto.getText());
        this.mdfAncho=Integer.parseInt(this.visualMDFancho.getText());
        this.luz=Integer.parseInt(this.visualMDFLuz.getText());
        this.visualLuzCorte.setText(this.visualMDFLuz.getText());
        this.crearTXTConfig();
        this.setearConfig();
        JOptionPane.showMessageDialog(null,"CONFIG EDITADA");
    }//GEN-LAST:event_jButton12ActionPerformed

    private void visualMDFLuzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualMDFLuzActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualMDFLuzActionPerformed

    private void visualMDFanchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualMDFanchoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualMDFanchoActionPerformed

    private void visualMDFALtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualMDFALtoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualMDFALtoActionPerformed

    private void visualMDFPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualMDFPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualMDFPrecioActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        
        if(!"".equals(this.inputAlto.getText()) && !"".equals(this.inputAncho.getText())){
            ImageIcon icon = new ImageIcon("save.png");
            
            if("Solo Corte".equals(this.nom)){
                this.mdf.setNombre((String) JOptionPane.showInputDialog(null, "Nombre: ", "Guardar", JOptionPane.INFORMATION_MESSAGE, icon, null,"")+": "+this.mdf.alto+"X"+this.mdf.ancho);
                this.prod=new Producto();
                this.prod.setNombre(this.mdf.getNombre());
                this.prod.setPrecio(this.mdf.getPrecio());
                this.hashProductos.put(this.prod.getNombre(), this.prod);
                this.updateListaProductos();
                this.crearTXTProductos();
            }
            if("Grabado Basico".equals(this.nom)||"Grabado Simple".equals(this.nom)||"Grabado Intermedio".equals(this.nom)||"Grabado Dificil".equals(this.nom)||"Grabado Complejo".equals(this.nom)){
                this.mdf.setNombre((String) JOptionPane.showInputDialog(null, "Nombre: ", "Guardar", JOptionPane.INFORMATION_MESSAGE, icon, null,"")+": "+this.mdf.alto+"X"+this.mdf.ancho);
                this.prod=new Producto();
                this.prod.setNombre(this.mdf.getNombre());
                this.prod.setPrecio(this.mdf.getPrecio());
                this.hashProductos.put(this.prod.getNombre(), this.prod);
                this.updateListaProductos();
                this.crearTXTProductos();
            }
            
            this.inputAlto.setText("");
            this.inputAncho.setText("");
            this.jTextField5.setText("");
            this.visualLuzCorte.setText("");
            this.gananciaMDF.setText("");
            this.valorsegmento.setText("");
            this.valorMDF.setText("");
            this.visualInversion.setText("");
            //this.setTablasMateriales();
            this.mdf=new MDF();
            JOptionPane.showMessageDialog(null,"Agregado con exito");
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void inputAnchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputAnchoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputAnchoActionPerformed

    private void inputAltoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputAltoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputAltoActionPerformed

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
            System.out.println("val segmento: "+this.val);
            System.out.println("DIFICULPTAD: "+Arrays.toString(this.dificultad));

            int sel = this.jComboBox1.getSelectedIndex();
            
            if(sel==0){
                this.nom="Solo Corte";
                this. valorMdfGanancia=((this.dificultad[sel])*this.val)/100;
            }
            if(sel==1){
                this.nom="Grabado Basico";
                this. valorMdfGanancia=((this.dificultad[sel])*this.val)/100;
            }
            if(sel==2){
                this.nom="Grabado Simple";
                this. valorMdfGanancia=((this.dificultad[sel])*this.val)/100;
            }
            if(sel==3){
                this.nom="Grabado Intermedio";
                this. valorMdfGanancia=((this.dificultad[sel])*this.val)/100;
            }
            if(sel==4){
                this.nom="Grabado Dificil";
                this. valorMdfGanancia=((this.dificultad[sel])*this.val)/100;
            }
            if(sel==5){
                this.nom="Grabado Complejo";
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

            this.mdf=new MDF("",(this.valorMdfGanancia+this.inversionCorte),this.val, x1, y1);
            System.out.println("MDF Original ____> "+this.mdf.precio);
            System.out.println("MDF ____> "+Math.round(this.mdf.precio));
            System.out.println("MDF ____> "+Math.round(this.mdf.precio));
            int h=this.valorMdfGanancia+this.inversionCorte;
            this.redondear(h);
            
        }
    }//GEN-LAST:event_jButton10ActionPerformed
    public void redondear(int valor){
        String s=Integer.toString(valor);
        String[] n=s.split("(?!^)");
        for(int i=0;i<n.length;i++){
            if(i<=n.length/2){
                    int aux=Integer.parseInt(n[i]);
                    if(i==n.length/2){
                        int aux2=Integer.parseInt(n[i]);
                        if(aux2>5){
                             n[i]="0";
                            int a2=1;
                            System.out.println(n[i]);
                         }
                    
            }
            //System.out.println(n[i]);
        }
        System.out.println(Arrays.toString(n));
        
        
    }
    }
    private void botonBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBorrarActionPerformed
        
        if(this.eliminarTabla.getSelectedValue()!=null){
            this.selected=this.eliminarTabla.getSelectedValue();
            if(this.listaProductos.contains(selected)){
                System.out.println("Prodcuto ELIMINADO");
                this.hashProductos.remove(selected);
                this.selected="";
                this.updateListaProductos();
                this.crearTXTProductos();
            }
            this.eliminarTabla.clearSelection();
        }

    }//GEN-LAST:event_botonBorrarActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        if(!"".equals(this.jTextField4.getText()) && !"".equals(this.jTextField6.getText())){
            
            this.prod=new Producto();
            this.prod.setNombre(this.jTextField6.getText());
            this.prod.setPrecio(Integer.parseInt(this.jTextField4.getText()));
            
            this.hashProductos.put(this.prod.getNombre(), this.prod);
            this.jTextField6.setText("");
            this.jTextField4.setText("");

            this.updateListaProductos();
            this.crearTXTProductos();

            //JOptionPane.showMessageDialog(null,"Agregado con exito");

        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //EDITAR PRODUCTO
        if(!"".equals(this.selected) && !"".equals(this.jTextField1.getText()) && !"".equals(this.jTextField2.getText())){
                this.hashProductos.remove(selected);
                this.prod.setNombre(this.jTextField1.getText());
                this.prod.setPrecio(Integer.parseInt(this.jTextField2.getText()));
                this.hashProductos.put(this.prod.getNombre(),this.prod);
                this.jTextField1.setText("");
                this.jTextField2.setText("");
                this.updateListaProductos();
                this.crearTXTProductos();
                selected="";
                this.editarTabla.clearSelection();
                JOptionPane.showMessageDialog(null,"Editado con exito");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(this.editarTabla.getSelectedValue()!=null){
            if(this.listaProductos.contains(this.editarTabla.getSelectedValue())){
                this.selected=this.editarTabla.getSelectedValue();
                this.prod=this.hashProductos.get(this.selected);
                System.out.println("SELECTED PROD: "+this.editarTabla.getSelectedValue());
                jTextField1.setText(this.prod.getNombre());
                jTextField2.setText(String.valueOf(this.prod.precio));//extraemos precio

            }

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
            //this.calcularTotal();
            
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

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
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new interfaz().setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel añadir;
    public javax.swing.JList<String> añadirTabla;
    private javax.swing.JButton botonBorrar;
    private javax.swing.JPanel config;
    private javax.swing.JPanel cotizar;
    private javax.swing.JPanel editar;
    public javax.swing.JList<String> editarTabla;
    private javax.swing.JPanel eliminar;
    public javax.swing.JList<String> eliminarTabla;
    private javax.swing.JPanel establecerValores;
    private javax.swing.JLabel gananciaMDF;
    private javax.swing.JTextField inputAlto;
    private javax.swing.JTextField inputAncho;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
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
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JLabel jTotal;
    private javax.swing.JLabel jTotal2;
    private javax.swing.JPanel laser;
    private javax.swing.JTabbedPane pedidos;
    private javax.swing.JTabbedPane productos;
    public javax.swing.JList<String> tablaCalculadoraMateriales;
    public javax.swing.JList<String> tablaCalculadoraUsados;
    public javax.swing.JList<String> tablaProductos1;
    public javax.swing.JList<String> tablaProductosMateriales1;
    private javax.swing.JLabel totalPedido;
    private javax.swing.JLabel valorMDF;
    private javax.swing.JLabel valorsegmento;
    private javax.swing.JTabbedPane venta;
    private javax.swing.JPanel ver;
    private javax.swing.JPanel ver1;
    public javax.swing.JList<String> verTabla;
    private javax.swing.JLabel visualInversion;
    private javax.swing.JLabel visualLuzCorte;
    private javax.swing.JTextField visualMDFALto;
    private javax.swing.JTextField visualMDFLuz;
    private javax.swing.JTextField visualMDFPrecio;
    private javax.swing.JTextField visualMDFancho;
    // End of variables declaration//GEN-END:variables
}
