/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.prototipogym;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.io.FileReader;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author asist-depti
 */
public class PACuota extends javax.swing.JFrame {

    /**
     * Creates new form PACuota
     */
    public PACuota() {
        initComponents();
        setTitle("Pantera Fitness");
        setLocationRelativeTo(null);
        // Establecer fecha de inicio: 1 de abril de 2025
        Calendar inicio = Calendar.getInstance();
        inicio.set(2025, Calendar.APRIL, 1); // Recuerda: los meses empiezan desde 0 (enero = 0)
        fechainicioChooser.setDate(inicio.getTime());

        // Establecer fecha final: 30 de abril de 2025
        Calendar fin = Calendar.getInstance();
        fin.set(2025, Calendar.APRIL, 30);
        fechafinalChooser.setDate(fin.getTime());
    }
    
    private static final String ENCABEZADO_PATH = "archivos/encabezado_cuota.txt";
    private static final String DETALLE_PATH = "archivos/detalle_cuota.txt";
    private static final String CLIENTE_PATH = "archivos/cliente.txt";
    private static final String COBROS_PATH = "archivos/cobros.txt";
    
    private void procesarCobros() {
     try {
        // --- parseo rango como LocalDate
        LocalDate inicio = fechainicioChooser.getDate()
                              .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fin    = fechafinalChooser.getDate()
                              .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("es","ES"));

        // —————————————————————————————————————————
        // 1) Leemos detalle_cuota y construimos cobrosPagados SOLO si la fecha del cobro está en el rango
        List<String> lineasDetalle = Files.readAllLines(Paths.get(DETALLE_PATH));
        Set<String> cobrosPagados = new HashSet<>();

        // Primero cargamos un mapa idCobro→fecha para poder mirar la fecha al filtrar
        Map<String,LocalDate> fechaCobroMap = new HashMap<>();
        for (String cob : Files.readAllLines(Paths.get(COBROS_PATH))) {
            String[] c = cob.split(",");
            if (c.length >= 6) {
                try {
                    LocalDate d = LocalDate.parse(c[1].trim(), fmt);
                    fechaCobroMap.put(c[0].trim(), d);
                } catch (DateTimeParseException ignore) {}
            }
        }

        // Ahora recorremos detalle
        for (String linea : lineasDetalle) {
            String[] p = linea.split(",");
            if (p.length >= 6 && p[5].equalsIgnoreCase("true")) {
                String idCobro = p[4].trim();
                LocalDate d = fechaCobroMap.get(idCobro);
                if (d != null 
                    && !d.isBefore(inicio) 
                    && !d.isAfter(fin)) {
                    cobrosPagados.add(idCobro);
                }
            }
        }

        // —————————————————————————————————————————
        // 2) Actualizamos cobros.txt SOLO para los idCobro en cobrosPagados
        List<String> todas = Files.readAllLines(Paths.get(COBROS_PATH));
        List<String> salida = new ArrayList<>();
        for (String linea : todas) {
            String[] p = linea.split(",");
            if (p.length >= 6) {
                String idCobro = p[0].trim();
                if (cobrosPagados.contains(idCobro)) {
                    p[5] = "true";
                }
            }
            salida.add(String.join(",", p));
        }
        Files.write(Paths.get(COBROS_PATH), salida, StandardOpenOption.TRUNCATE_EXISTING);

        // —————————————————————————————————————————
        // 3) Calculamos el nuevo idCuota
        int nuevoIdCuota = 1;
        for (String linea : lineasDetalle) {
            String[] p = linea.split(",");
            if (p.length>=6 && (p[5].equalsIgnoreCase("true")||p[5].equalsIgnoreCase("Procesado"))) {
                try {
                    int id = Integer.parseInt(p[0]);
                    if (id >= nuevoIdCuota) nuevoIdCuota = id + 1;
                } catch (NumberFormatException ign) {}
            }
        }

// —————————————————————————————————————————
// 4) Marcamos procesados y reasignamos no procesados
List<String> restantes = new ArrayList<>();

// 4.a Conserva todos los que ya estaban true, convirtiendo a "Procesado"
//     sólo los que están dentro del rango (cobrosPagados).
for (String linea : lineasDetalle) {
    String[] p = linea.split(",");
    if (p.length < 6) continue;

    if (p[5].equalsIgnoreCase("true")) {
        // si este idCobro está en cobrosPagados (rango), lo marco Procesado
        if (cobrosPagados.contains(p[4])) {
            p[5] = "Procesado";
        }
        // si NO está en cobrosPagados, lo dejo como "true"
        restantes.add(String.join(",", p));
    }
}

// 4.b Ahora los que eran false → los reasigno al nuevoIdCuota
int sec = 1;
String idClienteParaEncabezado = null;
String valorCobroParaEncabezado = null;
for (String linea : lineasDetalle) {
    String[] p = linea.split(",");
    if (p.length>=6 && p[5].equalsIgnoreCase("false")) {
        p[0] = String.valueOf(nuevoIdCuota);
        p[1] = String.format("%03d", sec++);
        restantes.add(String.join(",",p));
        if (idClienteParaEncabezado==null) {
            idClienteParaEncabezado = buscarIdClienteEnCobros(p[4]);
            valorCobroParaEncabezado = p[3];
        }
    }
}

// 4.c Añade los faltantes de cobros.txt 
// Construye un set con los idCobro que ya están en 'restantes'
Set<String> detalleExistente = new HashSet<>();
for (String l : restantes) {
    String[] p = l.split(",");
    if (p.length >= 5) {
        detalleExistente.add(p[4]); 
    }
}

// Lee todos los cobros.txt y añade los que sean de ESTE cliente y no estén en detalleExistente
List<String> todosCobros = Files.readAllLines(Paths.get(COBROS_PATH));
for (String cLine : todosCobros) {
    String[] c = cLine.split(",");
    // c[2]=idCliente, c[0]=idCobro, c[5]=status
    if (c.length >= 6
        && c[2].equals(idClienteParaEncabezado)
        && c[5].equalsIgnoreCase("false")
        && !detalleExistente.contains(c[0])) {
        
        String concepto = c[4];
        String valor    = c[3];
        String idCobro   = c[0];
        
        String nueva = String.join(",",
            String.valueOf(nuevoIdCuota),
            String.format("%03d", sec++),
            concepto,
            valor,
            idCobro,
            "false"
        );
        restantes.add(nueva);
    }
}

        // —————————————————————————————————————————
        // 5) AHORA creamos la línea de encabezado para ese nuevoIdCuota
if (idClienteParaEncabezado != null) {
    // declaro sdf aquí, ya disponible en esta sección
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("es","ES"));
    String fechaHoy = sdf.format(new Date());
    String lineaEnc = String.join(",",
        String.valueOf(nuevoIdCuota),
        fechaHoy,
        idClienteParaEncabezado,
        valorCobroParaEncabezado
    );
    Files.write(
        Paths.get(ENCABEZADO_PATH),
        Collections.singletonList(lineaEnc),
        StandardOpenOption.APPEND, StandardOpenOption.CREATE
    );
}

        // calcular el nextSec basado en lo que ya hay en 'restantes'
sec = 1;
for (String l : restantes) {
    String[] p = l.split(",");
    if (p.length >= 2 && Integer.parseInt(p[1]) >= sec) {
        sec = Integer.parseInt(p[1]) + 1;
    }
}

// 1) cargamos todos los cobros.txt para este cliente
List<String> cobros = Files.readAllLines(Paths.get(COBROS_PATH));
Set<String> detalleCobrosExistentes = new HashSet<>();
for (String l : restantes) {
    String[] p = l.split(",");
    if (p.length >= 5) {
        detalleCobrosExistentes.add(p[4]); // idCobro ya en detalle
    }
}

// 2) para cada línea de cobros.txt, si es de nuestro cliente y no está en detalle, la añadimos
for (String cLine : cobros) {
    String[] c = cLine.split(",");
    if (c.length >= 6 && c[2].equals(idClienteParaEncabezado) && !detalleCobrosExistentes.contains(c[0])) {
        String concepto = c[4];
        String valor    = c[3];
        // construyo nueva fila de detalle: idCuota, sec, concepto, valor, idCobro, false
        String nueva = String.join(",",
            String.valueOf(nuevoIdCuota),
            String.format("%03d", sec++),
            concepto,
            valor,
            c[0],
            "false"
        );
        restantes.add(nueva);
    }
}
        // —————————————————————————————————————————
        // 6) Guardamos el detalle actualizado
        Files.write(Paths.get(DETALLE_PATH), restantes, StandardOpenOption.TRUNCATE_EXISTING);

        JOptionPane.showMessageDialog(this, "Cobros procesados");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al procesar cobros: " + e.getMessage());
    }
}

// Método auxiliar para encontrar el idCliente en cobros.txt dado un idCobro
private String buscarIdClienteEnCobros(String idCobro) {
    try (BufferedReader br = new BufferedReader(new FileReader(COBROS_PATH))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] p = linea.split(",");
            if (p.length>=3 && p[0].equals(idCobro)) {
                return p[2];
            }
        }
    } catch (IOException ignored) {}
    return "";
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

        jPanel1 = new javax.swing.JPanel();
        fechainicioChooser = new com.toedter.calendar.JDateChooser();
        fechafinalChooser = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        btnprocesar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Actualizar Cobro");

        btnprocesar.setText("Procesar");
        btnprocesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprocesarActionPerformed(evt);
            }
        });

        btnsalir.setText("Volver");
        btnsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(btnprocesar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                .addComponent(btnsalir)
                .addGap(69, 69, 69))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fechainicioChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fechafinalChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(43, 43, 43)
                .addComponent(fechainicioChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(fechafinalChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnprocesar)
                    .addComponent(btnsalir))
                .addGap(36, 36, 36))
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

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        cancelar();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void btnprocesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprocesarActionPerformed
         procesarCobros();
    }//GEN-LAST:event_btnprocesarActionPerformed
    
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
            java.util.logging.Logger.getLogger(PACuota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PACuota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PACuota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PACuota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PACuota().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnprocesar;
    private javax.swing.JButton btnsalir;
    private com.toedter.calendar.JDateChooser fechafinalChooser;
    private com.toedter.calendar.JDateChooser fechainicioChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
