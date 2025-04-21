/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.prototipogym;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author asist-depti
 */
public class MCuotas extends javax.swing.JFrame {

    public MCuotas() {
        initComponents();
        setTitle("Pantera Fitness");
        setLocationRelativeTo(null);
    }

    private static final String ENCABEZADO_PATH = "archivos/encabezado_cuota.txt";
    private static final String DETALLE_PATH = "archivos/detalle_cuota.txt";
    private static final String CLIENTE_PATH = "archivos/cliente.txt";
    private static final String COBROS_PATH = "archivos/cobros.txt";

private void verificarOCargarCuota() {
    String idCuota = txtMCid.getText().trim();
    File archivo = new File(ENCABEZADO_PATH);
    boolean encontrado = false;

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length >= 5 && datos[0].equals(idCuota)) {
                SimpleDateFormat formato = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
                fechaChooser.setDate(formato.parse(datos[1]));
                txtMC_IDcliente.setText(datos[2]);
                txtMCvalorcobro.setText(datos[3]);
                txtMUAccion.setText("Modificando");
                
                cargarNombreCliente(datos[2]);
                generarDetalleCuotas(idCuota, datos[2]); // Primero generas
                cargarDetalleCuota(idCuota, datos[2]);   // Luego cargas lo generado
                
                encontrado = true;
                break;
            }
        }
    } catch (Exception e) {
        mostrarError("verificar cuota", e);
    }

    if (!encontrado) {
        txtMUAccion.setText("Creando");
        fechaChooser.setDate(new Date());
        txtMCvalorcobro.setText("");
        txtnombrecliente.setText("");
        txtMC_IDcliente.setText("");
        DefaultTableModel modelo = (DefaultTableModel) TMCdetalle.getModel();
        modelo.setRowCount(0);
    }
    
}


    private void cargarDetalleCuota(String idCuota, String idCliente) {
        DefaultTableModel modelo = (DefaultTableModel) TMCdetalle.getModel();
        modelo.setRowCount(0);
        File detalleArchivo = new File(DETALLE_PATH);
        File cobrosArchivo = new File(COBROS_PATH);

        try (BufferedReader br = new BufferedReader(new FileReader(detalleArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 5 && datos[0].equals(idCuota)) {
                String concepto = datos[2]; // <--- Agregado
                String idCobro = buscarIdCobroCorrespondiente(idCliente, concepto, new File(COBROS_PATH));
                modelo.addRow(new Object[]{datos[0], datos[1], datos[2], datos[3], idCobro});
            }

            }
        } catch (IOException e) {
            mostrarError("cargar detalle cuota", e);
        }
    }

private String buscarIdCobroCorrespondiente(String idCliente, String concepto, File archivoCobros) {
    try (BufferedReader br = new BufferedReader(new FileReader(archivoCobros))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length >= 5 && datos[2].equals(idCliente) && datos[4].equalsIgnoreCase(concepto)) {
                return datos[0]; // ID del cobro
            }
        }
    } catch (IOException e) {
        mostrarError("buscar ID cobro", e);
    }
    return "No encontrado";
}


    private void cargarNombreCliente(String idCliente) {
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length > 13 && datos[0].equals(idCliente)) {
                    txtnombrecliente.setText(datos[1]);
                    txtMCvalorcobro.setText(datos[13]);
                    return;
                }
            }
        } catch (IOException e) {
            mostrarError("cargar cliente", e);
        }
        txtnombrecliente.setText("Cliente no encontrado");
        txtMCvalorcobro.setText("");
    }

private void generarDetalleCuotas(String idCuota, String idCliente) {
    String valorCuota = "", nombreCliente = "", fechaIngresoStr = "";
    List<String> conceptosExistentes = new ArrayList<>();

    // Obtener datos del cliente
    try (BufferedReader br = new BufferedReader(new FileReader(CLIENTE_PATH))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos[0].equals(idCliente)) {
                valorCuota = datos[13];         // Valor de la cuota
                nombreCliente = datos[1];       // Nombre del cliente
                fechaIngresoStr = datos[8];     // Fecha de ingreso (posición 8)
                break;
            }
        }
    } catch (IOException e) {
        mostrarError("leyendo cliente.txt", e);
        return;
    }

    if (valorCuota.isEmpty() || fechaIngresoStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Cliente no encontrado o sin fecha de ingreso/valor de cuota.");
        return;
    }

    // Cargar conceptos existentes
    try (BufferedReader br = new BufferedReader(new FileReader(DETALLE_PATH))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length >= 5 && datos[0].equals(idCuota)) {
                conceptosExistentes.add(datos[2]);
            }
        }
    } catch (IOException ignored) {}

    // Convertir fecha de ingreso a Calendar
    SimpleDateFormat sdfInput = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
    SimpleDateFormat sdfConcepto = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
    Calendar fechaIngreso = Calendar.getInstance();
    Calendar fechaActual = Calendar.getInstance();

    try {
        Date fecha = sdfInput.parse(fechaIngresoStr);
        fechaIngreso.setTime(fecha);
    } catch (ParseException e) {
        mostrarError("convertir fecha de ingreso", e);
        return;
    }

    // La primera cuota es el mes siguiente al de ingreso
    fechaIngreso.add(Calendar.MONTH, 1);

    // Generar conceptos desde el mes siguiente hasta el mes actual
    List<String> conceptosNuevos = new ArrayList<>();
    Calendar iterador = (Calendar) fechaIngreso.clone();

    while (!iterador.after(fechaActual)) {
    String mesConFormato = sdfConcepto.format(iterador.getTime());
    mesConFormato = mesConFormato.substring(0, 1).toUpperCase() + mesConFormato.substring(1);
    String concepto = "Cobro " + mesConFormato;
    if (!conceptosExistentes.contains(concepto)) {
        conceptosNuevos.add(concepto);
    }
    iterador.add(Calendar.MONTH, 1);
}

    if (conceptosNuevos.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay cuotas pendientes por generar.");
        cargarDetalleCuota(idCuota, idCliente);
        return;
    }

    int ultimoSec = obtenerUltimoSecCuota(idCuota);
    

    try (PrintWriter pw = new PrintWriter(new FileWriter(DETALLE_PATH, true))) {
    for (String concepto : conceptosNuevos) {
        ultimoSec++;
        String sec = String.format("%03d", ultimoSec);
        String idCobro = buscarIdCobroCorrespondiente(idCliente, concepto, new File(COBROS_PATH)); // <- dentro del bucle
        pw.println(idCuota + "," + sec + "," + concepto + "," + valorCuota + "," + idCobro);
    }
    JOptionPane.showMessageDialog(this, "Cuotas generadas correctamente.");
} catch (IOException e) {
    mostrarError("guardar detalle cuota", e);
}
}



    private int obtenerUltimoSecCuota(String idCuota) {
        int ultimo = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(DETALLE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 2 && datos[0].equals(idCuota)) {
                    try {
                        int sec = Integer.parseInt(datos[1]);
                        if (sec > ultimo) ultimo = sec;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException ignored) {}
        return ultimo;
    }

    private void guardarCuota() {
        String idCuota = txtMCid.getText().trim();
        String idCliente = txtMC_IDcliente.getText().trim();
        String valorCobro = txtMCvalorcobro.getText().trim();
        String fecha = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES")).format(fechaChooser.getDate());
        String nuevaLinea = idCuota + "," + fecha + "," + idCliente + "," + valorCobro + ",false";

        List<String> lineas = new ArrayList<>();
        boolean encontrado = false;

        File archivo = new File(ENCABEZADO_PATH);
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[0].equals(idCuota)) {
                    lineas.add(nuevaLinea);
                    encontrado = true;
                } else {
                    lineas.add(linea);
                }
            }
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            mostrarError("leer archivo", e);
            return;
        }

        if (!encontrado) {
            lineas.add(nuevaLinea);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (String l : lineas) pw.println(l);
            JOptionPane.showMessageDialog(this, "Cuota guardada correctamente.");
        } catch (IOException e) {
            mostrarError("guardar cuota", e);
        }
    }

    private void mostrarError(String contexto, Exception e) {
        JOptionPane.showMessageDialog(this, "Error al " + contexto + ": " + e.getMessage());
    }



    private void limpiarCampos() {
        txtMCid.setText("");
        txtMC_IDcliente.setText("");
        txtnombrecliente.setText("");
        fechaChooser.setDate(null);
        txtMCvalorcobro.setText("");
        txtMUAccion.setText("");
        DefaultTableModel modelo = (DefaultTableModel) TMCdetalle.getModel();
        modelo.setRowCount(0);
    }

    private void cancelar() {
        this.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMCid = new javax.swing.JTextField();
        txtMC_IDcliente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jBCancelar = new javax.swing.JButton();
        jBLimpiar = new javax.swing.JButton();
        jBGuardar = new javax.swing.JButton();
        txtMUAccion = new javax.swing.JTextField();
        txtMCvalorcobro = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtnombrecliente = new javax.swing.JTextField();
        fechaChooser = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        TMCdetalle = new javax.swing.JTable();

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Cuotas");

        jLabel2.setText("ID Cuota:");

        txtMCid.setColumns(12);
        txtMCid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMCidActionPerformed(evt);
            }
        });

        txtMC_IDcliente.setColumns(12);
        txtMC_IDcliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMC_IDclienteFocusLost(evt);
            }
        });
        txtMC_IDcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMC_IDclienteActionPerformed(evt);
            }
        });

        jLabel5.setText("Valor Cuota:");

        jBCancelar.setText("Cancelar");
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });

        jBLimpiar.setText("Limpiar");
        jBLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBLimpiarActionPerformed(evt);
            }
        });

        jBGuardar.setText("Guardar");
        jBGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGuardarActionPerformed(evt);
            }
        });

        txtMUAccion.setColumns(8);
        txtMUAccion.setEnabled(false);
        txtMUAccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMUAccionActionPerformed(evt);
            }
        });

        txtMCvalorcobro.setColumns(12);
        txtMCvalorcobro.setEnabled(false);
        txtMCvalorcobro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMCvalorcobroActionPerformed(evt);
            }
        });

        jLabel7.setText("Fecha:");

        jLabel8.setText("ID Cliente:");

        txtnombrecliente.setEditable(false);
        txtnombrecliente.setColumns(12);
        txtnombrecliente.setText("Nombre Cliente");
        txtnombrecliente.setEnabled(false);
        txtnombrecliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreclienteActionPerformed(evt);
            }
        });

        TMCdetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Cuota", "Secuencia", "Concepto", "Valor Cuota", "ID Cobro", "Pago"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TMCdetalle);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(224, 224, 224)
                .addComponent(txtMUAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMC_IDcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jBCancelar))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtMCvalorcobro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtnombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(fechaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jBLimpiar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jBGuardar)))
                        .addGap(0, 52, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMCid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtMUAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fechaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtMCid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(txtnombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtMC_IDcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtMCvalorcobro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCancelar)
                    .addComponent(jBLimpiar)
                    .addComponent(jBGuardar))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMCidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMCidActionPerformed
        verificarOCargarCuota();
    }//GEN-LAST:event_txtMCidActionPerformed

    private void txtMUAccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMUAccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMUAccionActionPerformed

    private void jBLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_jBLimpiarActionPerformed

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_jBCancelarActionPerformed

    private void jBGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGuardarActionPerformed
        guardarCuota();
        
    }//GEN-LAST:event_jBGuardarActionPerformed

    private void txtMC_IDclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMC_IDclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMC_IDclienteActionPerformed

    private void txtMCvalorcobroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMCvalorcobroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMCvalorcobroActionPerformed

    private void txtnombreclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreclienteActionPerformed

    private void txtMC_IDclienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMC_IDclienteFocusLost
        String idCliente = txtMC_IDcliente.getText().trim();
        if (!idCliente.isEmpty()) {
            cargarNombreCliente(idCliente);
        }
    }//GEN-LAST:event_txtMC_IDclienteFocusLost

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
            java.util.logging.Logger.getLogger(MCuotas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MCuotas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MCuotas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MCuotas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MCuotas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TMCdetalle;
    private com.toedter.calendar.JDateChooser fechaChooser;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField txtMC_IDcliente;
    private javax.swing.JTextField txtMCid;
    private javax.swing.JTextField txtMCvalorcobro;
    private javax.swing.JTextField txtMUAccion;
    private javax.swing.JTextField txtnombrecliente;
    // End of variables declaration//GEN-END:variables
}
