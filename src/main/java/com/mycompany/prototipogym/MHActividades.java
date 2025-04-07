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
import javax.swing.JOptionPane;

/**
 *
 * @author asist-depti
 */
public class MHActividades extends javax.swing.JFrame {
    private static final String FILE_PATH = "archivos/horario_act.txt";

    /**
     * Creates new form MHActividades
     */
    public MHActividades() {
        initComponents();
        setTitle("Mantenimiento de Horario de Actividades");
        setLocationRelativeTo(null);

    }
    


    private void cargarUsuario() {
    String id_horario_act = txtMHAid.getText().trim();
    

    if (id_horario_act.isEmpty()) {
        return;
    }

    File archivo = new File(FILE_PATH);
    boolean idEncontrado = false;

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");

                if ((datos[0]).equals(id_horario_act)) {
                idEncontrado = true;
                // Se llenan los campos con la información obtenida:
                txtMHAdia.setText(datos[1]);
                txtMHAhora.setText(datos[2]);
                txtMHA_IDact.setText(datos[3]);
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
        txtMHAdia.setText("");
        txtMHAhora.setText("");
        txtMHA_IDact.setText("");
        txtMUAccion.setText("Creando");
    }
}

    private boolean existeIdActividad(int id_act) {
        File archivo = new File("archivos/actividades.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (Integer.parseInt(datos[0]) == id_act) {
                    return true; // Se encontró el ID en el archivo de localización
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de horario actividad.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // No se encontró el ID
    }

  
    private boolean validarCampos() {
        return !txtMHAid.getText().trim().isEmpty() &&
               !txtMHAdia.getText().trim().isEmpty() &&
               !txtMHAhora.getText().trim().isEmpty() &&
               !txtMHA_IDact.getText().trim().isEmpty();
    }
    
    
private void guardarDatos() {
    if (!validarCampos()) {
        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    String id_horarioact = txtMHAid.getText().trim();
    int hora, id_act;

    try {
        hora = Integer.parseInt(txtMHAhora.getText().trim());
        id_act = Integer.parseInt(txtMHA_IDact.getText().trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Ingrese solo números válidos en los campos Hora y ID Actividad.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String dia = txtMHAdia.getText().trim();

    // Verificar si el ID de Actividad existe
    if (!existeIdActividad(id_act)) {
        JOptionPane.showMessageDialog(this, "El ID de Actividad no existe. Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String nuevaLinea = id_horarioact + "," + dia + "," + hora + "," + id_act;
    File archivo = new File(FILE_PATH);
    boolean HorarioActExiste = false;
    StringBuilder contenido = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length >= 2) {
                if ((datos[0]).equals(id_horarioact)) {
                    contenido.append(nuevaLinea).append("\n");
                    HorarioActExiste = true;
                } else {
                    contenido.append(linea).append("\n");
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!HorarioActExiste) {
        contenido.append(nuevaLinea).append("\n");
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
        bw.write(contenido.toString());
        JOptionPane.showMessageDialog(this, HorarioActExiste ? "Horario de Actividad actualizado correctamente." : "Horario de Actividad guardado exitosamente.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    limpiarCampos();
    txtMUAccion.setText("");
}


    
    private void limpiarCampos() {
        txtMHAid.setText("");
        txtMHAdia.setText("");
        txtMHAhora.setText("");
        txtMHA_IDact.setText("");        
        txtMUAccion.setText("");
    }
    
    Menu m = new Menu();
    
    private void cancelar() {
        dispose();
        m.setVisible(true);
        txtMUAccion.setText("");
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
        txtMHAid = new javax.swing.JTextField();
        txtMHAdia = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jBCancelar = new javax.swing.JButton();
        jBLimpiar = new javax.swing.JButton();
        jBGuardar = new javax.swing.JButton();
        txtMUAccion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMHA_IDact = new javax.swing.JTextField();
        txtMHAhora = new javax.swing.JTextField();

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Mantenimiento de Horario de Actividades");

        jLabel2.setText("ID Horario:");

        txtMHAid.setColumns(12);
        txtMHAid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMHAidActionPerformed(evt);
            }
        });

        txtMHAdia.setColumns(12);

        jLabel5.setText("Día de actividad:");

        jLabel6.setText("Hora de actvidad:");

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

        jLabel3.setText("ID Actividad:");

        txtMHA_IDact.setColumns(12);
        txtMHA_IDact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMHA_IDactActionPerformed(evt);
            }
        });

        txtMHAhora.setColumns(12);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txtMUAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jBGuardar)
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtMHAid, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMHA_IDact, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMHAdia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMHAhora, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jBCancelar)
                                .addGap(61, 61, 61)
                                .addComponent(jBLimpiar)))
                        .addGap(73, 73, 73))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(txtMUAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMHAid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(txtMHAdia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(txtMHAhora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMHA_IDact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void txtMHAidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMHAidActionPerformed
        cargarUsuario();
    }//GEN-LAST:event_txtMHAidActionPerformed

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

    private void txtMHA_IDactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMHA_IDactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMHA_IDactActionPerformed

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
            java.util.logging.Logger.getLogger(MHActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MHActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MHActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MHActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MHActividades().setVisible(true);
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField txtMHA_IDact;
    private javax.swing.JTextField txtMHAdia;
    private javax.swing.JTextField txtMHAhora;
    private javax.swing.JTextField txtMHAid;
    private javax.swing.JTextField txtMUAccion;
    // End of variables declaration//GEN-END:variables
}
