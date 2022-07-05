
package javaapplication2;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public final class Interfaz extends javax.swing.JFrame {

    HashMap<String, Integer[]> materialesUtilizados = new HashMap<>();
    HashMap<String, Integer> materiales = new HashMap<>();
    int total=0;
    int luz=25;
    int consumoLuz=0;
    int cantidad=0;
    int ganancia=0;
    int valorMateriales=0;
    int mdfValor=9000;
    int mdfAlto=149; 
    int mdfAncho=250;
    int mdfPorcentaje=12000;
    int valorSegmento=0;
    int val=0;
    int gastoCorte=0;
    boolean update=false;
    boolean load=false;
    
    String[] listaVisualMateriales= new String[100];
    String[] ListaVisualMaterialesPrecio= new String[100];
    
    String[] lUsado= new String[100];
    String[] lUPrecio= new String[100];
    
    ArrayList<String> mDF= new ArrayList<>();
    ArrayList<String> trozo= new ArrayList<>();
    
    ArrayList<String> listaMateriales= new ArrayList<>();
    ArrayList<String> listaPrecioMateriales= new ArrayList<>();
    
    ArrayList<String> listaUsado= new ArrayList<>();
    ArrayList<String> listaUsadoPrecio= new ArrayList<>();
    
    String selected="";
    
    public Interfaz() throws IOException {
        initComponents();
        this.setVisible(true);
        this.setTitle("DC LASER V1.0");
        Image logo = new ImageIcon(getClass().getResource("1.jpg")).getImage();
        this.setIconImage(logo);
        
        
        File mat = new File("Materiales");
        mat.mkdirs();
        
        //Crear txt Magteriales y txt
        
        load=true;
        this.operarTxT();
        //txtMateriales.createNewFile();
        
        //System.out.println("INICIALIZANDO TXT");
        
        //this.updateListaMaterialesTablas();
        
         //System.out.println("SET TABLAS");
        //this.setTablasMateriales();
        
        
        //this.actualizarTablaMDF();
    }
    public void setTablaMDF(){
        this.visualMDFPrecio.setText(Integer.toString(this.mdfValor));
        this.visualMDFALto.setText(Integer.toString(this.mdfAlto));
        this.visualMDFancho.setText(Integer.toString(this.mdfAncho));
        this.visualMDFPorcentaje.setText(Integer.toString(this.mdfPorcentaje));
    }
    public void segmentarMDF(){
        this.mdfValor=Integer.parseInt(this.visualMDFPrecio.getText());
        this.mdfAlto=Integer.parseInt(this.visualMDFPrecio.getText());
        this.mdfAncho=Integer.parseInt(this.visualMDFPrecio.getText());
        this.mdfPorcentaje=Integer.parseInt(this.visualMDFPrecio.getText());
        
    }
        
    public void inicializarTxTMateriales(){
          try {
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
                        bw.newLine();
                        bw.write("-");
                        bw.newLine();
                        bw.close();
              }
            } catch (IOException e) {
            }
          
    }
    public void actualizarTxTMateriales(){
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
                        bw.write("-");
                        bw.newLine();
                            for(int l=0;l<this.listaMateriales.size();l++){
                                System.out.println("material");
                                bw.write(this.listaMateriales.get(l));//nombre
                                bw.newLine();
                                bw.write(String.valueOf(this.materiales.get(this.listaMateriales.get(l))));//valor
                                if(l!=this.listaMateriales.size()-1){
                                    bw.newLine();
                                    bw.write("-");
                                    bw.newLine();
                                }
                            }
                        bw.close();
              }
            } catch (IOException e) {
            }
          System.out.println("TXT ACTUALIZADO");
    }
    //importa datos del txt o lo crea
    public void operarTxT() throws IOException {
        
        final File mate = new File("Materiales");
        for (final File ficheroEntrada : mate.listFiles()) {
            if (ficheroEntrada.isFile() && "Materiales.txt".equals(ficheroEntrada.getName())) {
                if(this.update==true){
                    System.out.println("Actualizando Materiales");
                    this.updateListaMaterialesTablas();
                    this.actualizarTxTMateriales();
                    this.setTablasMateriales();
                    this.update=false;
                }
                if(this.load=true){
                    System.out.println("cargando Materiales");
                    this.importarDatosTxT(ficheroEntrada);
                    this.updateListaMaterialesTablas();
                    this.setTablasMateriales();
                    this.setTablaMDF();
                    this.load=false;
                }
                
            }
            else{
                this.inicializarTxTMateriales();
            }
        }
    }

    public void calcularTotal(){
        if(!"".equals(this.jLabel31.getText()) && !"".equals(this.jTextField11.getText())){
            this.total=0;
            //System.out.println("TOTAL 1: "+this.total);
            int g=Integer.parseInt(this.jTextField11.getText());
            materialesUtilizados.entrySet().forEach((Map.Entry<String, Integer[]> entry) -> {
                Integer[] datos=this.materialesUtilizados.get(entry.getKey());
                this.total+=datos[0];
            });
            
            this.valorMateriales=this.total;
            this.jLabel38.setText(Integer.toString(this.valorMateriales));
            System.out.println("MATERIALES: "+this.valorMateriales);
            this.ganancia=(g*total)/100;
            System.out.println("GANANCIA: "+this.ganancia);
            System.out.println("Total: "+this.total);
            
            //System.out.println("%: "+g);
            
            this.total+=this.consumoLuz+this.ganancia;
            
            
            
            this.jLabel33.setText(Integer.toString(this.ganancia));
            this.jLabel34.setText(Integer.toString(this.total));      
            
            
            
        }
    
    }
    public void importarDatosTxT(File f){
        BufferedReader entrada = null; 
                try { 
                    entrada = new BufferedReader( new FileReader( f ) ); 
                    String linea;
                    String valor;
                    this.materiales=new HashMap<>();
                    while(entrada.ready()){ 
                        linea = entrada.readLine();
                        System.out.println("**************: "+linea );
                        if(linea.equals("*")){
                                //sET lUZ
                                linea=entrada.readLine();
                                luz=Integer.parseInt(linea);
                                //SET tablas luz
                                this.visualMDFLuz.setText(linea);
                                this.jLabel31.setText(linea);
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
                                System.out.println("%: "+linea ); 
                                linea=entrada.readLine();
                                System.out.println("*: "+linea );
                                linea=entrada.readLine();
                                System.out.println("-: "+linea );
                            }
                        if(linea.equals("-") && entrada.ready()){
                                linea = entrada.readLine();
                                valor =entrada.readLine();
                                System.out.println("Importado: "+linea+"  "+valor); 
                                materiales.put(linea, Integer.parseInt(valor));
                        }
                            //
                    }
                    //System.out.println("FIN ");
                }catch (IOException e) { 
                    } 
                    finally{ 
                        try{ 
                            entrada.close(); 
                        }
                        catch(IOException e1){} 
                    } 
                
                System.out.println("Los Materiales se cargaron: "+this.listaMateriales);
                
    }
    public void calcularConsumoLuz(){
        int v=0;
        try {
            v=Integer.parseInt(this.jTextField5.getText());
        } catch(NumberFormatException e) {}
        if(v!=0){
            this.consumoLuz= this.luz*Integer.parseInt(this.jTextField5.getText());
            this.listaPrecioMateriales.add(Integer.toString(this.consumoLuz));
            this.jLabel31.setText(Integer.toString(this.consumoLuz));
        }
    }
    
   
    public void setTablasMateriales(){
        System.out.println();
        this.jMater4.setListData(listaVisualMateriales);
        this.jMater5.setListData(ListaVisualMaterialesPrecio);
        this.jMater3.setListData(listaVisualMateriales);
        this.jMater1.setListData(lUsado);
    }
    public void setearUsado(){
        this.jMater1.setListData(lUsado);
    }
    public void setearTablasMDF(){
        //this.tablacontactosEditarLanchas.setListData(listadoClientes);
        this.jMater4.setListData(listaVisualMateriales);
        this.jMater5.setListData(ListaVisualMaterialesPrecio);
        this.jMater3.setListData(listaVisualMateriales);
        this.jMater1.setListData(lUPrecio);
    }
    
    
    public void añadirGAsto(String nombre, Integer valor){
        this.materiales.put(nombre, valor);
        
    }
    public void añadirUsado(){
        this.cantidad=Integer.parseInt(jTextField3.getText());
        int costo=this.materiales.get(this.jMater4.getSelectedValue())*this.cantidad;
        Integer valores[]= new Integer[2];
        valores[0]=costo;
        valores[1]=this.cantidad;
        this.materialesUtilizados.put(valores[1]+" "+this.jMater4.getSelectedValue()+" $"+valores[0], valores);
        this.listaUsado.add(valores[1]+" "+this.jMater4.getSelectedValue()+" $"+valores[0]);
        System.out.println("SE GUARDO: "+valores[1]+" "+this.jMater4.getSelectedValue()+" $"+valores[0]);
        this.jTextField3.setText("");
        this.updateListaUsados();
        
    }
    
    public void updateListaUsados(){
        
        this.lUsado= new String[this.listaUsado.size()];
            for (int i=0; i<listaUsado.size(); i++) {
                 this.lUsado[i]=listaUsado.get(i);//lista de usados
            }

        this.jMater1.setListData(lUsado);
    }
        
    public void updateListaMaterialesTablas(){
        this.listaMateriales=new  ArrayList<>();
        this.listaPrecioMateriales= new  ArrayList<>();
        
        materiales.entrySet().forEach((Map.Entry<String, Integer> entry) -> {
            listaMateriales.add(entry.getKey());
            listaPrecioMateriales.add(entry.getKey()+":  $ "+this.materiales.get(entry.getKey()));
        });
        
        this.listaVisualMateriales= new String[this.listaMateriales.size()];
        this.ListaVisualMaterialesPrecio= new String[this.listaPrecioMateriales.size()];
        //System.out.println(lista.toString());
      
        for (int i=0; i<listaMateriales.size(); i++) {
             listaVisualMateriales[i]=listaMateriales.get(i);
             ListaVisualMaterialesPrecio[i]=listaPrecioMateriales.get(i);
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new fondoPanel("wood.jpg");
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
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
        jTextField11 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        lista = new fondoPanel("wood.jpg");
        jScrollPane12 = new javax.swing.JScrollPane();
        jMater5 = new javax.swing.JList<>();
        jLabel26 = new javax.swing.JLabel();
        editar = new fondoPanel("wood.jpg");
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
        mdf = new fondoPanel("wood.jpg");
        jLabel22 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        visualMDFALto = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        visualMDFancho = new javax.swing.JTextField();
        visualMDFPrecio = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        inputAlto = new javax.swing.JTextField();
        inputAncho = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        gananciaEsperada = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        visualMDFPorcentaje = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        visualMDFLuz = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        costonetoMDF1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(95, 158, 160));

        jTabbedPane1.setBackground(new java.awt.Color(8, 69, 84));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 0));
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(95, 158, 160));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 0));
        jLabel1.setText("LISTA MATERIALES");

        jButton1.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/fd.png"))); // NOI18N
        jButton1.setToolTipText("Ingresar");
        jButton1.setAlignmentY(0.0F);
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/calcular.png"))); // NOI18N
        jButton2.setToolTipText("Calcular");
        jButton2.setAlignmentY(0.0F);
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 0));
        jLabel3.setText("Valor Total:  $");

        jMater1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jMater1.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jMater1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jMater1.setSelectionBackground(new java.awt.Color(255, 255, 102));
        jMater1.setSelectionForeground(new java.awt.Color(0, 255, 0));
        jScrollPane8.setViewportView(jMater1);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("N°");
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jMater4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jMater4.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jMater4.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jMater4.setSelectionBackground(new java.awt.Color(255, 255, 102));
        jMater4.setSelectionForeground(new java.awt.Color(0, 255, 0));
        jScrollPane11.setViewportView(jMater4);

        jButton9.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/delete.png"))); // NOI18N
        jButton9.setToolTipText("Eliminar");
        jButton9.setAlignmentY(0.0F);
        jButton9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 0));
        jLabel4.setText("MATERIALES UTILIZADOS");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 0));
        jLabel17.setText("% Ganancia (40-50)");

        jTextField11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTextField11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 0));
        jLabel32.setText("Ganancia:  $");

        jLabel33.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel33.setForeground(java.awt.Color.yellow);

        jLabel34.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel34.setForeground(java.awt.Color.yellow);

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 0));
        jLabel36.setText("Materiales:  $");

        jLabel38.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel38.setForeground(java.awt.Color.yellow);

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
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel36)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel32)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(0, 396, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane11)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(33, 33, 33)
                                        .addComponent(jLabel8)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(58, 58, 58)
                                                .addComponent(jTotal))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton1)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane8)))))
                .addContainerGap())
        );

        jTabbedPane1.addTab("CALCULADORA", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/calcular.png")), jPanel1); // NOI18N

        jTabbedPane2.setBackground(new java.awt.Color(8, 69, 84));
        jTabbedPane2.setForeground(new java.awt.Color(255, 255, 0));
        jTabbedPane2.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N

        lista.setBackground(new java.awt.Color(95, 158, 160));

        jMater5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jMater5.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jMater5.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jMater5.setSelectionBackground(new java.awt.Color(255, 255, 102));
        jMater5.setSelectionForeground(new java.awt.Color(0, 255, 0));
        jScrollPane12.setViewportView(jMater5);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 0));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("PRECIO MATERIALES");

        javax.swing.GroupLayout listaLayout = new javax.swing.GroupLayout(lista);
        lista.setLayout(listaLayout);
        listaLayout.setHorizontalGroup(
            listaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(listaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addContainerGap(653, Short.MAX_VALUE))
        );
        listaLayout.setVerticalGroup(
            listaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listaLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("PRECIOS", lista);

        editar.setBackground(new java.awt.Color(95, 158, 160));
        editar.setForeground(new java.awt.Color(255, 255, 255));
        editar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel5.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 0));
        jLabel5.setText("Nombre:");

        jTextField1.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jTextField1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 0));
        jLabel6.setText("Precio:");

        jTextField2.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jTextField2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/edit.png"))); // NOI18N
        jButton3.setToolTipText("Editar");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jMater3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jMater3.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jMater3.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jMater3.setSelectionBackground(new java.awt.Color(255, 255, 102));
        jMater3.setSelectionForeground(new java.awt.Color(0, 255, 0));
        jMater3.setVerifyInputWhenFocusTarget(false);
        jScrollPane10.setViewportView(jMater3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/save.png"))); // NOI18N
        jButton4.setToolTipText("Guardar");
        jButton4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 0));
        jLabel12.setText("Nombre:");

        jLabel13.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 0));
        jLabel13.setText("Precio:");

        jTextField4.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jTextField4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField6.setFont(new java.awt.Font("Palatino Linotype", 2, 14)); // NOI18N
        jTextField6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/anadir.png"))); // NOI18N
        jButton6.setToolTipText("Añadir");
        jButton6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        botonBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/delete.png"))); // NOI18N
        botonBorrar.setToolTipText("Eliminar");
        botonBorrar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        botonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBorrarActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 0));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Editar");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("MATERIALES");

        jLabel18.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Añadir");

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
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editarLayout.createSequentialGroup()
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(54, 54, 54)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(editarLayout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(9, 9, 9)
                                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(editarLayout.createSequentialGroup()
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6))
                                    .addComponent(jTextField2)))
                            .addGroup(editarLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1))
                            .addGroup(editarLayout.createSequentialGroup()
                                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(editarLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(editarLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                                                .addComponent(jTextField4))))))))
                    .addGroup(editarLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(105, 105, 105))
        );
        editarLayout.setVerticalGroup(
            editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10)
                    .addGroup(editarLayout.createSequentialGroup()
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(editarLayout.createSequentialGroup()
                                .addComponent(botonBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(editarLayout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 265, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane2.addTab("EDITAR/AGREGAR/ELIMINAR", editar);

        mdf.setBackground(new java.awt.Color(95, 158, 160));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 0));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Valor plancha MDF");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel7.setText("Alto:");

        visualMDFALto.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        visualMDFALto.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        visualMDFALto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        visualMDFALto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualMDFALtoActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 0));
        jLabel23.setText("Ancho:");

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/calcular.png"))); // NOI18N
        jButton10.setToolTipText("Calcular");
        jButton10.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        visualMDFancho.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        visualMDFancho.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        visualMDFancho.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        visualMDFancho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualMDFanchoActionPerformed(evt);
            }
        });

        visualMDFPrecio.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        visualMDFPrecio.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        visualMDFPrecio.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        visualMDFPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualMDFPrecioActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 0));
        jLabel24.setText("Precio:  $");

        inputAlto.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        inputAlto.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        inputAlto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        inputAlto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputAltoActionPerformed(evt);
            }
        });

        inputAncho.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        inputAncho.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        inputAncho.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        inputAncho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputAnchoActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 0));
        jLabel25.setText("Ancho:");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 0));
        jLabel28.setText("Alto:");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 0));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("MDF UTILIZADO");

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/anadir.png"))); // NOI18N
        jButton11.setToolTipText("Añadir");
        jButton11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 0));
        jLabel35.setText("Valor Producto:  $");

        gananciaEsperada.setBackground(new java.awt.Color(255, 255, 255));
        gananciaEsperada.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        gananciaEsperada.setForeground(new java.awt.Color(255, 255, 0));
        gananciaEsperada.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gananciaEsperada.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        gananciaEsperada.setOpaque(true);

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 0));
        jLabel37.setText("% Ganancia:");

        visualMDFPorcentaje.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        visualMDFPorcentaje.setForeground(new java.awt.Color(102, 51, 0));
        visualMDFPorcentaje.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        visualMDFPorcentaje.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        visualMDFPorcentaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualMDFPorcentajeActionPerformed(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/save.png"))); // NOI18N
        jButton12.setToolTipText("Guardar");
        jButton12.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 0));
        jLabel10.setText("Minutos de Consumo:  $");

        jTextField5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextField5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 0));
        jLabel27.setText("Consumo Luz:  $");

        jLabel31.setBackground(new java.awt.Color(255, 255, 255));
        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 0));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel31.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jLabel31.setOpaque(true);

        visualMDFLuz.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        visualMDFLuz.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        visualMDFLuz.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        visualMDFLuz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualMDFLuzActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 0));
        jLabel15.setText("Precio Luz:  $");

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 0));
        jLabel40.setText("Ganancia MDF:  $");

        costonetoMDF1.setBackground(new java.awt.Color(255, 255, 255));
        costonetoMDF1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        costonetoMDF1.setForeground(new java.awt.Color(255, 255, 0));
        costonetoMDF1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        costonetoMDF1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        costonetoMDF1.setOpaque(true);

        javax.swing.GroupLayout mdfLayout = new javax.swing.GroupLayout(mdf);
        mdf.setLayout(mdfLayout);
        mdfLayout.setHorizontalGroup(
            mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mdfLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mdfLayout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputAncho, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(inputAlto)))
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gananciaEsperada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(costonetoMDF1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(102, 102, 102)
                .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(visualMDFPorcentaje, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                            .addComponent(visualMDFLuz)))
                    .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(mdfLayout.createSequentialGroup()
                                .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(visualMDFancho, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(visualMDFALto)
                                        .addComponent(visualMDFPrecio)))))
                        .addComponent(jButton12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(98, Short.MAX_VALUE))
        );
        mdfLayout.setVerticalGroup(
            mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mdfLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(18, 18, 18)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inputAlto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputAncho, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(costonetoMDF1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(gananciaEsperada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(mdfLayout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(visualMDFPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(visualMDFALto, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(visualMDFancho, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(visualMDFPorcentaje, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(visualMDFLuz, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(mdfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(276, 276, 276))
        );

        jTabbedPane2.addTab("MDF", mdf);

        jTabbedPane1.addTab("MATERIALES", new javax.swing.ImageIcon(getClass().getResource("/javaapplication2/mat.png")), jTabbedPane2); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 984, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(!"".equals(this.jTextField3.getText()) && Integer.parseInt(this.jTextField3.getText())>0){
            this.añadirUsado();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if(!"".equals(this.jTextField6.getText()) && !"".equals(this.jTextField4.getText())){
            System.out.println("Añadiendo: "+ this.jTextField6.getText()+"Valor: "+this.jTextField4.getText());
            this.materiales.put(this.jTextField6.getText(), Integer.parseInt(this.jTextField4.getText()));
            this.jTextField6.setText("");
            this.jTextField4.setText("");
            
            
            try {
                update=true;
                this.updateListaMaterialesTablas();
                System.out.println("update: "+this.update+ "Load: "+this.load);
                this.operarTxT();
                //JOptionPane.showMessageDialog(null,"Agregado con exito");
            } catch (IOException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void botonBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBorrarActionPerformed
        selected=this.listaMateriales.get(this.jMater3.getSelectedIndex());

        if(!"".equals(selected) && materiales.containsKey(selected)){
            materiales.remove(selected);
            selected="";
            this.updateListaMaterialesTablas();
            this.setTablasMateriales();
            this.actualizarTxTMateriales();
            //JOptionPane.showMessageDialog(null,"Eliminado con exito");
        }
        
        
    }//GEN-LAST:event_botonBorrarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(materiales.containsKey(selected) && !"".equals(this.jTextField1.getText()) && !"".equals(this.jTextField2.getText())){
            this.listaUsado.remove(selected);
            materiales.remove(selected);
            selected="";
            materiales.put(this.jTextField1.getText(), Integer.parseInt(this.jTextField2.getText()));
            this.listaUsado.add(this.jTextField1.getText());
            this.jTextField1.setText("");
            this.jTextField2.setText("");
            this.updateListaMaterialesTablas();
            this.setTablasMateriales();
            this.actualizarTxTMateriales();
            //JOptionPane.showMessageDialog(null,"Editado con exito");
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(this.jMater3.getSelectedValue()!=null){
            jTextField1.setText(this.jMater3.getSelectedValue());
            jTextField2.setText(this.materiales.get(this.jMater3.getSelectedValue()).toString());//extraemos precio
            selected=this.listaMateriales.get(this.jMater3.getSelectedIndex());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
      
       
       selected=this.listaUsado.get(this.jMater1.getSelectedIndex());
       System.out.println("SIZE lista Usado: "+this.listaUsado.size());
       System.out.println("SIZE hash usado: "+this.materialesUtilizados.size());
       System.out.println("tratando de eliminar: "+selected);
       if(materialesUtilizados.containsKey(selected)){
            this.materialesUtilizados.remove(selected);
            this.listaUsado.remove(selected);
            selected="";
            this.updateListaUsados();
            //JOptionPane.showMessageDialog(null,"Eliminado con exito");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.calcularConsumoLuz();        
        this.calcularTotal();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void visualMDFLuzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualMDFLuzActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualMDFLuzActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        this.mdfValor=Integer.parseInt(this.visualMDFPrecio.getText());
        this.mdfAlto=Integer.parseInt(this.visualMDFALto.getText());
        this.mdfAncho=Integer.parseInt(this.visualMDFancho.getText());
        this.mdfPorcentaje=Integer.parseInt(this.visualMDFPorcentaje.getText());
        this.luz=Integer.parseInt(this.visualMDFLuz.getText());
        this.jLabel31.setText(this.visualMDFLuz.getText());
        this.updateListaMaterialesTablas();
        this.setTablasMateriales();
        this.actualizarTxTMateriales();
        JOptionPane.showMessageDialog(null,"Editado con exito");
    }//GEN-LAST:event_jButton12ActionPerformed

    private void visualMDFPorcentajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualMDFPorcentajeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualMDFPorcentajeActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if(!"".equals(this.inputAlto.getText()) && !"".equals(this.inputAncho.getText())){
            System.out.println("EL MDF VALE: $ "+this.gastoCorte);
            materiales.put("MDF "+this.inputAlto.getText()+" X "+this.inputAncho.getText(), this.gastoCorte);
            this.inputAlto.setText("");
            this.inputAncho.setText("");

            this.updateListaMaterialesTablas();
            this.setTablasMateriales();
            this.actualizarTxTMateriales();
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
            this. valorSegmento=(Integer.parseInt(this.visualMDFPorcentaje.getText())*val)/100;
            this.costonetoMDF1.setText(Integer.toString(valorSegmento));
            this.calcularConsumoLuz();
            this.gastoCorte=this. valorSegmento+this.consumoLuz;
            this.gananciaEsperada.setText(Integer.toString(gastoCorte));

            System.out.println("1: "+valorSegmento);

            //valor neto

        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void visualMDFALtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualMDFALtoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualMDFALtoActionPerformed

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
    private javax.swing.JLabel costonetoMDF1;
    private javax.swing.JPanel editar;
    private javax.swing.JLabel gananciaEsperada;
    private javax.swing.JTextField inputAlto;
    private javax.swing.JTextField inputAncho;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    public javax.swing.JList<String> jMater1;
    public javax.swing.JList<String> jMater3;
    public javax.swing.JList<String> jMater4;
    public javax.swing.JList<String> jMater5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel jTotal;
    private javax.swing.JPanel lista;
    private javax.swing.JPanel mdf;
    private javax.swing.JTextField visualMDFALto;
    private javax.swing.JTextField visualMDFLuz;
    private javax.swing.JTextField visualMDFPorcentaje;
    private javax.swing.JTextField visualMDFPrecio;
    private javax.swing.JTextField visualMDFancho;
    // End of variables declaration//GEN-END:variables
}
