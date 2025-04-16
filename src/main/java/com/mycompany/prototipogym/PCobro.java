/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.prototipogym;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PCobro extends javax.swing.JFrame {

    /**
     * Creates new form PCobro
     */
    public PCobro() {
        initComponents();
        setTitle("Generar Cobros");
        setLocationRelativeTo(null);
        fechacobroChooser.setDate(new Date());

    }
   
    
public static class GeneradorCobros {

        private static final String RUTA_CLIENTES = "archivos/cliente.txt";
        private static final String RUTA_COBROS = "archivos/cobros.txt";

        // Patrón de fecha usado en el archivo cliente.txt, usando Locale para interpretar "abr"
        private static final DateTimeFormatter FORMATO_FECHA =
                DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("es", "ES"));

        public static void generarCobros(LocalDate fechaCobroUsuario) throws IOException {
            // Leer los registros existentes en cobros.txt para evitar duplicados
            List<String> registrosExistentes = new ArrayList<>();
            File archivoCobros = new File(RUTA_COBROS);
            if (archivoCobros.exists()) {
                registrosExistentes = Files.readAllLines(archivoCobros.toPath());
            }
            
            int ultimoId = obtenerUltimoIdCobro(registrosExistentes);
            
            List<String> lineasClientes = Files.readAllLines(Paths.get(RUTA_CLIENTES));
            List<String> nuevosCobros = new ArrayList<>();

            for (String linea : lineasClientes) {
                String[] datos = linea.split(",");
                if (datos.length < 14) {
                    continue; // demasiados valores, solo son 13
                }
                String idCliente = datos[0].trim();
                String fechaIngresoStr = datos[8].trim();
                String valorCobroStr = datos[13].trim();
                
                // Intentar parsear la fecha de ingreso usando el patrón con Locale
                LocalDate fechaIngreso;
                try {
                    fechaIngreso = LocalDate.parse(fechaIngresoStr, FORMATO_FECHA);
                } catch (Exception ex) {
                    System.err.println("Error al parsear la fecha (" + fechaIngresoStr + ") para el cliente " + idCliente);
                    continue;
                }
                
                // Verificar que hayan pasado al menos 30 días entre la fecha de ingreso y la fecha seleccionada
                long diasTranscurridos = ChronoUnit.DAYS.between(fechaIngreso, fechaCobroUsuario);
                if (diasTranscurridos < 30) {
                    continue;
                }
                
                
                LocalDate fechaCobroGenerado = fechaCobroUsuario;
                
                // Verificar si ya se generó un cobro para este cliente y  fecha
                if (existeCobro(registrosExistentes, idCliente, fechaCobroGenerado.format(FORMATO_FECHA))) {
                    continue;
                }
                
                ultimoId++;
                String idCobro = String.format("%03d", ultimoId);
                
                // para el "Cobro [mes] [año]"
                String mesTexto = fechaCobroGenerado.getMonth().toString().substring(0, 1) +
                                   fechaCobroGenerado.getMonth().toString().substring(1).toLowerCase();
                String concepto = "Cobro " + mesTexto + " " + fechaCobroGenerado.getYear();
                
                String status = "false";
                
                
                String registroCobro = idCobro + "," +
                        fechaCobroGenerado.format(FORMATO_FECHA) + "," +
                        idCliente + "," +
                        valorCobroStr + "," +
                        concepto + "," +
                        status;
                
                nuevosCobros.add(registroCobro);
                // se grega a la lista de existente para evitar duplicados en el mismo proceso.
                registrosExistentes.add(registroCobro);
            }
            if (!nuevosCobros.isEmpty()) {
                Files.write(Paths.get(RUTA_COBROS), nuevosCobros, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Se generaron " + nuevosCobros.size() + " nuevos cobros.");
            } else {
                System.out.println("No se generaron nuevos cobros.");
            }
        }

       
        private static boolean existeCobro(List<String> registros, String idCliente, String fechaCobro) {
            for (String reg : registros) {
                String[] campos = reg.split(",");
                if (campos.length >= 3) {
                    String idClienteExistente = campos[2].trim();
                    String fechaCobroExistente = campos[1].trim();
                    if (idClienteExistente.equals(idCliente) && fechaCobroExistente.equals(fechaCobro)) {
                        return true;
                    }
                }
            }
            return false;
        }

        
        private static int obtenerUltimoIdCobro(List<String> registros) {
            int ultimo = 0;
            for (String reg : registros) {
                String[] campos = reg.split(",");
                if (campos.length > 0) {
                    try {
                        int id = Integer.parseInt(campos[0].trim());
                        if (id > ultimo) {
                            ultimo = id;
                        }
                    } catch (NumberFormatException ex) {
                        //Se ignora si es de un formato inválido, porque el método es una lista y se rompe
                    }
                }
            }
            return ultimo;
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fechacobroChooser = new com.toedter.calendar.JDateChooser();
        btnGencobro = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Generar Cobro");

        jLabel2.setText("Fecha:");

        btnGencobro.setText("Generar Cobro");
        btnGencobro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGencobroActionPerformed(evt);
            }
        });

        btnVolver.setText("Volver");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(168, 168, 168)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGencobro)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnVolver, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fechacobroChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(fechacobroChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGencobro)
                    .addComponent(btnVolver))
                .addGap(39, 39, 39))
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

    private void btnGencobroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGencobroActionPerformed
        Date fechaSeleccionada = fechacobroChooser.getDate();
        if (fechaSeleccionada == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "Seleccione una fecha de cobro.");
            return;
        }
        // Convertir de Date a LocalDate
        LocalDate fechaCobroUsuario = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        try {
            GeneradorCobros.generarCobros(fechaCobroUsuario);
            javax.swing.JOptionPane.showMessageDialog(null, "Cobros generados correctamente.");
        } catch (IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error generando cobros: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnGencobroActionPerformed

    /**
     * @param args the command line arguments*/
     
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
            java.util.logging.Logger.getLogger(PCobro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PCobro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PCobro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PCobro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PCobro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGencobro;
    private javax.swing.JButton btnVolver;
    private com.toedter.calendar.JDateChooser fechacobroChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
