/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.prototipogym;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;

/**
 *
 * @author asist-depti
 */
public class MReservas extends javax.swing.JFrame {
    private static final String FILE_PATH = "archivos/reservas.txt";

    /**
     * Creates new form MActividades
     */
    public MReservas() {
        initComponents();
        setTitle("Mantenimiento de Reservas");
        setLocationRelativeTo(null);

    }
    


    private void cargarUsuario() {
    String id_reservas;
    try {
        id_reservas = txtMRid.getText().trim();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (id_reservas.isEmpty()) {
        return;
    }

    File archivo = new File(FILE_PATH);
    boolean idEncontrado = false;

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");

                if ((datos[0]).equals(id_reservas)) {
                idEncontrado = true;
                // Se llenan los campos con la información obtenida:
                txtMR_IDsalareserva.setText(datos[1]);
                txtMR_IDclientereser.setText(datos[2]);
                txtMRfechareser.setText(datos[3]);
                txtMR_IDhorarioreser.setText(datos[4]);
                txtMR_IDestadoreser.setText(datos[5]);
                txtMUAccion.setText("Modificando");
                break;
            }
        }
        
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Error al procesar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!idEncontrado) {
        txtMR_IDsalareserva.setText("");
        txtMR_IDclientereser.setText("");
        txtMRfechareser.setText("");
        txtMR_IDhorarioreser.setText("");
        txtMR_IDestadoreser.setText("");
        txtMUAccion.setText("Creando");
    }
}

    private boolean existeIdSalas(int id_salareserva) {
        File archivo = new File("archivos/salas.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (Integer.parseInt(datos[0]) == id_salareserva) {
                    return true; // Se encontró el ID en el archivo de localización
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de salas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // No se encontró el ID
    }
    
    private boolean existeIdCliente(int id_cliente) {
        File archivo = new File("archivos/cliente.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (Integer.parseInt(datos[0]) == id_cliente) {
                    return true; // Se encontró el ID en el archivo de entrenador
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de cliente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // No se encontró el ID
    }
    
    private boolean existeIdReservaActividad(int id_reserva_actividad) {
        File archivo = new File("archivos/reserva_actividad.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (Integer.parseInt(datos[0]) == id_reserva_actividad) {
                    return true; // Se encontró el ID en el archivo de entrenador
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de reserva actividad.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // No se encontró el ID
    }
    
    private boolean existeIdEstadoReserva(int estado_reserva) {
        File archivo = new File("archivos/estado_reserva.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (Integer.parseInt(datos[0]) == estado_reserva) {
                    return true; // Se encontró el ID en el archivo de entrenador
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de estado de reserva.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // No se encontró el ID
    }

  
    private boolean validarCampos() {
        return !txtMR_IDsalareserva.getText().trim().isEmpty() &&
               !txtMR_IDclientereser.getText().trim().isEmpty() &&
               !txtMRfechareser.getText().trim().isEmpty() &&
               !txtMR_IDhorarioreser.getText().trim().isEmpty() &&
               !txtMR_IDestadoreser.getText().trim().isEmpty();
    }
    
    
private void guardarDatos() {
    if (!validarCampos()) {
        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String id_reservas = txtMRid.getText();
    int id_salareserva = Integer.parseInt(txtMR_IDhorarioreser.getText());
    int id_cliente = Integer.parseInt(txtMR_IDestadoreser.getText());
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    LocalDate fechareser;    

    try {
        fechareser = LocalDate.parse(txtMRfechareser.getText(), formatter);
    } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd-MM-yyyy (ej: 05-04-2025).", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    int id_reserva_actividad = Integer.parseInt(txtMR_IDhorarioreser.getText());
    int id_estado_reserva = Integer.parseInt(txtMR_IDestadoreser.getText());

    // Verificar si los ID existen
    if (!existeIdSalas(id_salareserva)) {
        JOptionPane.showMessageDialog(this, "El ID de salas no existe. Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (!existeIdCliente(id_cliente)) {
        JOptionPane.showMessageDialog(this, "El ID de cliente no existe. Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (!existeIdReservaActividad(id_reserva_actividad)) {
        JOptionPane.showMessageDialog(this, "El ID de Horario Reserva no existe. Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (!existeIdEstadoReserva(id_estado_reserva)) {
        JOptionPane.showMessageDialog(this, "El ID de estado de reserva no existe. Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String nuevaLinea = id_reservas + "," + id_salareserva + "," + id_cliente + "," + fechareser + "," + id_reserva_actividad + "," + id_estado_reserva;
    File archivo = new File(FILE_PATH);
    boolean reservaExiste = false;
    StringBuilder contenido = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length >= 2) {
                if ((datos[0]).equals(id_reservas)) {
                    contenido.append(nuevaLinea).append("\n");
                    reservaExiste = true;
                } else {
                    contenido.append(linea).append("\n");
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!reservaExiste) {
        contenido.append(nuevaLinea).append("\n");
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
        bw.write(contenido.toString());
        JOptionPane.showMessageDialog(this, reservaExiste ? "Reserva actualizada correctamente." : "Reserva guardada exitosamente.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    limpiarCampos();
    txtMUAccion.setText("");
}


    
    private void limpiarCampos() {
        txtMR_IDsalareserva.setText("");
        txtMR_IDclientereser.setText("");
        txtMRfechareser.setText("");
        txtMR_IDhorarioreser.setText("");
        txtMR_IDestadoreser.setText("");
        txtMUAccion.setText("");
    }
    
    Menu m = new Menu();
    
    private void cancelar() {
        dispose();
        m.setVisible(true);
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
        txtMRid = new javax.swing.JTextField();
        txtMR_IDsalareserva = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jBCancelar = new javax.swing.JButton();
        jBLimpiar = new javax.swing.JButton();
        jBGuardar = new javax.swing.JButton();
        txtMUAccion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMR_IDestadoreser = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMR_IDhorarioreser = new javax.swing.JTextField();
        txtMRfechareser = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtMR_IDclientereser = new javax.swing.JTextField();

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Mantenimiento de Reservas");

        jLabel2.setText("ID Reserva:");

        txtMRid.setColumns(12);
        txtMRid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMRidActionPerformed(evt);
            }
        });

        txtMR_IDsalareserva.setColumns(12);
        txtMR_IDsalareserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMR_IDsalareservaActionPerformed(evt);
            }
        });

        jLabel5.setText("ID Sala Reserva:");

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

        jLabel3.setText("ID Estado Reserva:");

        txtMR_IDestadoreser.setColumns(12);
        txtMR_IDestadoreser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMR_IDestadoreserActionPerformed(evt);
            }
        });

        jLabel4.setText("ID Horario Reserva:");

        txtMR_IDhorarioreser.setColumns(12);
        txtMR_IDhorarioreser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMR_IDhorarioreserActionPerformed(evt);
            }
        });

        txtMRfechareser.setColumns(12);
        txtMRfechareser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMRfechareserActionPerformed(evt);
            }
        });

        jLabel7.setText("Fecha Reserva:");

        jLabel8.setText("ID Cliente Reserva:");

        txtMR_IDclientereser.setColumns(12);
        txtMR_IDclientereser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMR_IDclientereserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(txtMUAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jBGuardar)
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jBCancelar)
                                .addGap(61, 61, 61)
                                .addComponent(jBLimpiar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(36, 36, 36)
                                        .addComponent(txtMRfechareser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtMR_IDhorarioreser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGap(36, 36, 36)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(txtMRid, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtMR_IDestadoreser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtMR_IDsalareserva, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(36, 36, 36)
                                        .addComponent(txtMR_IDclientereser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(73, 73, 73))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(txtMUAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMRid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtMR_IDsalareserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtMR_IDclientereser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtMRfechareser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMR_IDhorarioreser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMR_IDestadoreser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCancelar)
                    .addComponent(jBLimpiar)
                    .addComponent(jBGuardar))
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMRidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMRidActionPerformed
    cargarUsuario();
    }//GEN-LAST:event_txtMRidActionPerformed

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
        guardarDatos();        
        
    }//GEN-LAST:event_jBGuardarActionPerformed

    private void txtMR_IDestadoreserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMR_IDestadoreserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMR_IDestadoreserActionPerformed

    private void txtMR_IDsalareservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMR_IDsalareservaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMR_IDsalareservaActionPerformed

    private void txtMR_IDhorarioreserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMR_IDhorarioreserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMR_IDhorarioreserActionPerformed

    private void txtMRfechareserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMRfechareserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMRfechareserActionPerformed

    private void txtMR_IDclientereserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMR_IDclientereserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMR_IDclientereserActionPerformed

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
            java.util.logging.Logger.getLogger(MReservas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MReservas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MReservas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MReservas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MReservas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField txtMR_IDclientereser;
    private javax.swing.JTextField txtMR_IDestadoreser;
    private javax.swing.JTextField txtMR_IDhorarioreser;
    private javax.swing.JTextField txtMR_IDsalareserva;
    private javax.swing.JTextField txtMRfechareser;
    private javax.swing.JTextField txtMRid;
    private javax.swing.JTextField txtMUAccion;
    // End of variables declaration//GEN-END:variables
}
