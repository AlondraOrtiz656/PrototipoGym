package com.mycompany.prototipogym;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author asist-depti
 */
public class CSalas extends javax.swing.JFrame {

    private static final String FILE_PATH = "archivos/salas.txt";
    private List<String[]> listaUsuarios = new ArrayList<>();
    private Menu menuOriginal;

    public CSalas(Menu menu) {
        initComponents();
        setTitle("Consulta de Salas");
        setLocationRelativeTo(null);
        this.menuOriginal = menu;
        cargarUsuarios();
    }

    private void cargarUsuarios() {
    DefaultTableModel modelo = (DefaultTableModel) CUTable.getModel();
    modelo.setRowCount(0); // Limpiar tabla
    listaUsuarios.clear(); // Limpiar lista

    try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            // Se usa split(",", -1) para incluir campos vacíos al final
            String[] datos = linea.split(",", -1);
            if (datos.length >= 4) {
                String[] fila = new String[] {
                    datos[0].trim(), // ID
                    datos[1].trim(), // nombre
                    datos[2].trim(), // descripción
                    datos[3].trim(), // ID localización
                    
                    
                };
                modelo.addRow(fila);
                listaUsuarios.add(fila);
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer el archivo de salas: " + e.getMessage());
    }
}


    private void filtrarTabla(String texto) {
        int columna = cmbfiltro.getSelectedIndex();

        DefaultTableModel modelo = (DefaultTableModel) CUTable.getModel();
        modelo.setRowCount(0); // Limpiar tabla

        for (String[] fila : listaUsuarios) {
            if (fila[columna].toLowerCase().contains(texto.toLowerCase())) {
                modelo.addRow(fila);
            }
        }
    }
    
    private void cancelar() {
        this.dispose();  
        if (menuOriginal != null) {
        menuOriginal.setVisible(true);  
    }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        CUTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbfiltro = new javax.swing.JComboBox<>();
        txtconsulta = new javax.swing.JTextField();
        btncancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        CUTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Descripción", "ID Localización"
            }
        ));
        jScrollPane1.setViewportView(CUTable);

        jLabel1.setText("Consulta de Salas");

        jLabel2.setText("Filtrar por:");

        cmbfiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nombre", "Descripción", "Id Localización" }));
        cmbfiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbfiltroActionPerformed(evt);
            }
        });

        txtconsulta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtconsultaKeyReleased(evt);
            }
        });

        btncancelar.setText("Cancelar");
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbfiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtconsulta)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(213, 213, 213)
                        .addComponent(btncancelar)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbfiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtconsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbfiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbfiltroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbfiltroActionPerformed

    private void txtconsultaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtconsultaKeyReleased
        filtrarTabla(txtconsulta.getText());
    }//GEN-LAST:event_txtconsultaKeyReleased

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btncancelarActionPerformed

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
            java.util.logging.Logger.getLogger(CSalas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CSalas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CSalas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CSalas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new CSalas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable CUTable;
    private javax.swing.JButton btncancelar;
    private javax.swing.JComboBox<String> cmbfiltro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtconsulta;
    // End of variables declaration//GEN-END:variables
}
