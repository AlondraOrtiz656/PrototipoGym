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
public class MRActividades extends javax.swing.JFrame {
    private static final String FILE_PATH = "archivos/reserva_actividad.txt";

    /**
     * Creates new form MActividades
     */
    public MRActividades() {
        initComponents();
        setTitle("Mantenimiento de Reservas de Actividades");
        setLocationRelativeTo(null);

    }
    


    private void cargarUsuario() {
    int id_reservaact;
    try {
        id_reservaact = Integer.parseInt(txtMRAid.getText().trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID inválido. Deber ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (id_reservaact ==0) {
        return;
    }

    File archivo = new File(FILE_PATH);
    boolean idEncontrado = false;

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");

            if (datos[0].equals(String.valueOf(id_reservaact))) {
                idEncontrado = true;
                // Se llenan los campos con la información obtenida:
                DateTimeFormatter formatoMostrar = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                LocalDate fechaReserva = LocalDate.parse(datos[1]);
                LocalDate fechaBaja = LocalDate.parse(datos[2]);

                txtMRAfechareser.setText(fechaReserva.format(formatoMostrar));
                txtMRAfechabaja.setText(fechaBaja.format(formatoMostrar));

                txtMRA_IDestadoreser.setText(datos[3]);
                txtMRA_IDcliente.setText(datos[4]);
                txtMRA_IDact.setText(datos[5]);
                txtMRA_IDhorarioact.setText(datos[6]);
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
        txtMRAfechareser.setText("");
        txtMRAfechabaja.setText("");
        txtMRA_IDestadoreser.setText("");
        txtMRA_IDcliente.setText("");
        txtMRA_IDact.setText("");
        txtMRA_IDhorarioact.setText("");
        txtMUAccion.setText("Creando");
    }
}

    private boolean existeIdEstadoReserva(String id_estado_reserva) {
        File archivo = new File("archivos/estado_reserva.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if ((datos[0]).equals(id_estado_reserva)) {
                    return true; // Se encontró el ID en el archivo 
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de estado de reserva.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    return true; // Se encontró el ID en el archivo
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de cliente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // No se encontró el ID
    }
    
    private boolean existeIdActividad(int id_act) {
        File archivo = new File("archivos/actividades.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (Integer.parseInt(datos[0]) == id_act) {
                    return true; // Se encontró el ID en el archivo de entrenador
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de actividad.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // No se encontró el ID
    }
    
    private boolean existeIdHorarioAtc(String id_horarioact) {
        File archivo = new File("archivos/horario_act.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if ((datos[0]).equals(id_horarioact)) {
                    return true; // Se encontró el ID en el archivo de entrenador
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de horario de actividad.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // No se encontró el ID
    }

  
    private boolean validarCampos() {
        return !txtMRAid.getText().trim().isEmpty() &&
               !txtMRAfechareser.getText().trim().isEmpty() &&
               !txtMRAfechabaja.getText().trim().isEmpty() &&
               !txtMRA_IDestadoreser.getText().trim().isEmpty() &&
               !txtMRA_IDcliente.getText().trim().isEmpty() &&
               !txtMRA_IDact.getText().trim().isEmpty() &&
               !txtMRA_IDhorarioact.getText().trim().isEmpty();
    }
    
    
private void guardarDatos() {
    if (!validarCampos()) {
        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int id_estado_reserva;
    
    try {
            id_estado_reserva = Integer.parseInt(txtMRAid.getText());    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID inválido. Deber ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    LocalDate fechareser;    
    LocalDate fechabaja; 

    try {
        fechareser = LocalDate.parse(txtMRAfechareser.getText(), formatter);
        fechabaja = LocalDate.parse(txtMRAfechabaja.getText(), formatter);
    } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd-MM-yyyy (ej: 05-04-2025).", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    String id_estadoreser = txtMRA_IDestadoreser.getText();
    int id_cliente;    
    int id_actividad;
    
    try {
            id_cliente = Integer.parseInt(txtMRA_IDcliente.getText());    
            id_actividad = Integer.parseInt(txtMRA_IDact.getText());    
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID inválido. Deber ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    String id_horarioact = txtMRA_IDhorarioact.getText();

    // Verificar si los ID existen
    if (!existeIdEstadoReserva(id_estadoreser)) {
        JOptionPane.showMessageDialog(this, "El ID de estado de reserva  no existe. Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }    
    
    if (!existeIdCliente(id_cliente)) {
        JOptionPane.showMessageDialog(this, "El ID de cliente no existe. Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }    
    
    
    if (!existeIdActividad(id_actividad)) {
        JOptionPane.showMessageDialog(this, "El ID de estado de actividad no existe. Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (!existeIdHorarioAtc(id_horarioact)) {
        JOptionPane.showMessageDialog(this, "El ID de estado de horario no existe. Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String nuevaLinea = id_estado_reserva + "," + fechareser + "," + fechabaja + "," + id_estadoreser + "," + id_cliente + "," + id_actividad + "," + id_horarioact;
    File archivo = new File(FILE_PATH);
    boolean reservaactExiste = false;
    StringBuilder contenido = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length >= 2) {
                if ((datos[0]).equals(id_estado_reserva)) {
                    contenido.append(nuevaLinea).append("\n");
                    reservaactExiste = true;
                } else {
                    contenido.append(linea).append("\n");
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!reservaactExiste) {
        contenido.append(nuevaLinea).append("\n");
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
        bw.write(contenido.toString());
        JOptionPane.showMessageDialog(this, reservaactExiste ? "Reserva Actividad actualizada correctamente." : "Reserva Actividad guardada exitosamente.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    limpiarCampos();
}


    
    private void limpiarCampos() {
        txtMRAid.setText("");
        txtMRAfechareser.setText("");
        txtMRAfechabaja.setText("");
        txtMRA_IDestadoreser.setText("");
        txtMRA_IDcliente.setText("");
        txtMRA_IDact.setText("");
        txtMRA_IDhorarioact.setText("");
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
        txtMRAid = new javax.swing.JTextField();
        txtMRAfechareser = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jBCancelar = new javax.swing.JButton();
        jBLimpiar = new javax.swing.JButton();
        jBGuardar = new javax.swing.JButton();
        txtMUAccion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMRA_IDact = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMRA_IDcliente = new javax.swing.JTextField();
        txtMRA_IDestadoreser = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtMRAfechabaja = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtMRA_IDhorarioact = new javax.swing.JTextField();

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Mantenimiento de Reservas de Actividades");

        jLabel2.setText("ID Reserva Actvidad:");

        txtMRAid.setColumns(12);
        txtMRAid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMRAidActionPerformed(evt);
            }
        });

        txtMRAfechareser.setColumns(12);
        txtMRAfechareser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMRAfechareserActionPerformed(evt);
            }
        });

        jLabel5.setText("Fecha Reserva:");

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

        txtMRA_IDact.setColumns(12);
        txtMRA_IDact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMRA_IDactActionPerformed(evt);
            }
        });

        jLabel4.setText("ID Cliente Res. Act.:");

        txtMRA_IDcliente.setColumns(12);
        txtMRA_IDcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMRA_IDclienteActionPerformed(evt);
            }
        });

        txtMRA_IDestadoreser.setColumns(12);
        txtMRA_IDestadoreser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMRA_IDestadoreserActionPerformed(evt);
            }
        });

        jLabel7.setText("ID Est Reserva Act:");

        jLabel8.setText("Fecha Baja:");

        txtMRAfechabaja.setColumns(12);
        txtMRAfechabaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMRAfechabajaActionPerformed(evt);
            }
        });

        jLabel6.setText("ID Res. Hor. Act:");

        txtMRA_IDhorarioact.setColumns(12);
        txtMRA_IDhorarioact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMRA_IDhorarioactActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jBCancelar)
                .addGap(65, 65, 65)
                .addComponent(jBLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(jBGuardar)
                .addGap(46, 46, 46))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMRA_IDcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(36, 36, 36)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtMRAid, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMRA_IDact, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMRAfechareser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMRA_IDhorarioact, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMRA_IDestadoreser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMRAfechabaja, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGap(23, 23, 23)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtMUAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                    .addComponent(txtMRAid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtMRAfechareser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtMRAfechabaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtMRA_IDestadoreser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMRA_IDcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMRA_IDact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMRA_IDhorarioact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMRAidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMRAidActionPerformed
    cargarUsuario();
    }//GEN-LAST:event_txtMRAidActionPerformed

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

    private void txtMRA_IDactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMRA_IDactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMRA_IDactActionPerformed

    private void txtMRAfechareserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMRAfechareserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMRAfechareserActionPerformed

    private void txtMRA_IDclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMRA_IDclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMRA_IDclienteActionPerformed

    private void txtMRA_IDestadoreserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMRA_IDestadoreserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMRA_IDestadoreserActionPerformed

    private void txtMRAfechabajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMRAfechabajaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMRAfechabajaActionPerformed

    private void txtMRA_IDhorarioactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMRA_IDhorarioactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMRA_IDhorarioactActionPerformed

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
            java.util.logging.Logger.getLogger(MRActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MRActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MRActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MRActividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new MRActividades().setVisible(true);
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField txtMRA_IDact;
    private javax.swing.JTextField txtMRA_IDcliente;
    private javax.swing.JTextField txtMRA_IDestadoreser;
    private javax.swing.JTextField txtMRA_IDhorarioact;
    private javax.swing.JTextField txtMRAfechabaja;
    private javax.swing.JTextField txtMRAfechareser;
    private javax.swing.JTextField txtMRAid;
    private javax.swing.JTextField txtMUAccion;
    // End of variables declaration//GEN-END:variables
}
