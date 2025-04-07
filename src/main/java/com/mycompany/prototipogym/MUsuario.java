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
 * @author User
 */
public class MUsuario extends javax.swing.JFrame {
    private static final String FILE_PATH = "archivos/usuarios.txt";

    /**
     * Creates new form MUsuario
     */
    public MUsuario() {
        initComponents();
        setTitle("Mantenimiento de Usuario");
        setLocationRelativeTo(null);

    }
    


    private void cargarUsuario() {
    String usuario = txtMUusuario.getText().trim();
    
    File archivo = new File(FILE_PATH);
    if(!archivo.exists()){
        //txtMUAccion.setText("Creando");
        return;
    }
    boolean usuarioEncontrado = false;
    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            // Se asume que el registro tiene 6 elementos:
            // [0] usuario, [1] contraseña, [2] nivel, [3] nombre, [4] apellido, [5] correo
            if( datos[0].equals(usuario)) {
                usuarioEncontrado = true;
                // Se llenan los campos con la información obtenida:
                txtMUpwd.setText(datos[1]);
                txtMUAccion.setText("Modificando");
                // Ajusta el índice del combo según la información del archivo:
                // En este ejemplo, si el valor en el archivo es "1" se asocia a "1 Normal" (índice 0),
                // y si es "0" se asocia a "0 Administrador" (índice 1)
                if(datos[2].equals("1")){
                    cmbMUnivel.setSelectedIndex(1);
                    
                } else if(datos[2].equals("0")){
                    cmbMUnivel.setSelectedIndex(0);
                    
                }
                txtMUnom.setText(datos[3]);
                txtMUApellido.setText(datos[4]);
                txtMUCorreo.setText(datos[5]);
                txtMUAccion.setText("Modificando");
                break;
            }

        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!usuarioEncontrado) {
        txtMUAccion.setText("Creando");
        txtMUpwd.setText("");
        txtMUnom.setText("");
        txtMUApellido.setText("");
        txtMUCorreo.setText("");
    }
    
    
}
    

    
    private boolean validarCampos() {
        return !txtMUusuario.getText().trim().isEmpty() &&
               !txtMUpwd.getText().trim().isEmpty() &&
               !txtMUnom.getText().trim().isEmpty() &&
               !txtMUApellido.getText().trim().isEmpty();
    }
    
    private boolean esAdministrador() {
        return cmbMUnivel.getSelectedIndex() == 1; 
    }
    
private void guardarDatos() {
    if (!validarCampos()) {
        JOptionPane.showMessageDialog(this, "Todos los campos excepto correo son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String usuario = txtMUusuario.getText();
    String contrasena = txtMUpwd.getText();
    String nivel = String.valueOf(cmbMUnivel.getSelectedIndex());
    String nombre = txtMUnom.getText();
    String apellido = txtMUApellido.getText();
    String correo = txtMUCorreo.getText();
    String nuevaLinea = usuario + "," + contrasena + "," + nivel + "," + nombre + "," + apellido + "," + correo;

    File archivo = new File(FILE_PATH);
    boolean usuarioExiste = false;
    StringBuilder contenido = new StringBuilder();

    // Leer el archivo y modificar la línea si el usuario ya existe
    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length > 0 && datos[0].equals(usuario)) {
                contenido.append(nuevaLinea).append("\n");
                usuarioExiste = true;
            } else {
                contenido.append(linea).append("\n");
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Si el usuario no existe, lo agregamos al final
    if (!usuarioExiste) {
        contenido.append(nuevaLinea).append("\n");
    }

    // Escribir el nuevo contenido en el archivo
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
        bw.write(contenido.toString());
        JOptionPane.showMessageDialog(this, usuarioExiste ? "Usuario actualizado correctamente." : "Usuario guardado exitosamente.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    limpiarCampos();
    txtMUAccion.setText("");
}

    
    private void limpiarCampos() {
        txtMUusuario.setText("");
        txtMUpwd.setText("");
        txtMUnom.setText("");
        txtMUApellido.setText("");
        txtMUCorreo.setText("");
        cmbMUnivel.setSelectedIndex(0);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMUusuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtMUnom = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMUApellido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtMUCorreo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cmbMUnivel = new javax.swing.JComboBox<>();
        jBCancelar = new javax.swing.JButton();
        jBLimpiar = new javax.swing.JButton();
        jBGuardar = new javax.swing.JButton();
        txtMUAccion = new javax.swing.JTextField();
        txtMUpwd = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Mantenimiento de Usuario");

        jLabel2.setText("Usuario:");

        txtMUusuario.setColumns(12);
        txtMUusuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMUusuarioActionPerformed(evt);
            }
        });

        jLabel3.setText("Contraseña:");

        jLabel4.setText("Nivel de Acceso:");

        txtMUnom.setColumns(12);

        jLabel5.setText("Nombre:");

        txtMUApellido.setColumns(12);

        jLabel6.setText("Apellidos:");

        txtMUCorreo.setColumns(12);

        jLabel7.setText("Correo:");

        cmbMUnivel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 Normal", "0 Administrador" }));

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

        txtMUpwd.setColumns(12);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                            .addComponent(txtMUusuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel2))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jBCancelar)
                                        .addGap(35, 35, 35)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addComponent(jBLimpiar))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(47, 47, 47)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtMUpwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtMUCorreo)
                                                .addComponent(txtMUApellido)
                                                .addComponent(txtMUnom)
                                                .addComponent(cmbMUnivel, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMUusuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtMUpwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbMUnivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtMUnom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtMUApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtMUCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCancelar)
                    .addComponent(jBLimpiar)
                    .addComponent(jBGuardar))
                .addGap(25, 25, 25))
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

    private void txtMUusuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMUusuarioActionPerformed
    cargarUsuario();
    }//GEN-LAST:event_txtMUusuarioActionPerformed

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
            java.util.logging.Logger.getLogger(MUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MUsuario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbMUnivel;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtMUAccion;
    private javax.swing.JTextField txtMUApellido;
    private javax.swing.JTextField txtMUCorreo;
    private javax.swing.JTextField txtMUnom;
    private javax.swing.JPasswordField txtMUpwd;
    private javax.swing.JTextField txtMUusuario;
    // End of variables declaration//GEN-END:variables
}
