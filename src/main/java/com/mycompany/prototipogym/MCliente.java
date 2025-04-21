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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class MCliente extends javax.swing.JFrame {
    private static final String FILE_PATH = "archivos/cliente.txt";


    /**
     * Creates new form MUsuario
     */
    public MCliente() {
        initComponents();
        setTitle("Pantera Fitness");
        setLocationRelativeTo(null);
    }
    


private void cargarUsuario() {
    int id_cliente;
    try {
        id_cliente = Integer.parseInt(txtMCid.getText().trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (id_cliente == 0) {
        return;
    }

    File archivo = new File(FILE_PATH);
    boolean idEncontrado = false;

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");

                if (Integer.parseInt(datos[0]) == id_cliente) {
                idEncontrado = true;
                // Se llenan los campos con la información obtenida:
                txtMCnom.setText(datos[1]);
                txtMCapellidop.setText(datos[2]);
                txtMCapellidom.setText(datos[3]);
                jtaMCdirrec.setText(datos[4]);
                String fechaStringNac = datos[5]; // Fecha de nacimiento
                String fechaStringIng = datos[8]; // Fecha de ingreso

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new java.util.Locale("es", "ES")); // Asegura la configuración regional en español

                try {
                    // Parsear la fecha de nacimiento
                    Date fechaNac = sdf.parse(fechaStringNac);
                    fechanacChooser.setDate(fechaNac);  

                    // Parsear la fecha de ingreso
                    Date fechaIng = sdf.parse(fechaStringIng);
                    fechaingreChooser.setDate(fechaIng);  
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Error al parsear la fecha. Verifica el formato (ej. '9 abr 2025').", "Error", JOptionPane.ERROR_MESSAGE);
                }
                txtMCtele.setText(datos[6]);
                txtMCcelular.setText(datos[7]);
                cmbMCstatus.setSelectedItem(datos[9]);
                int tipoCliente = Integer.parseInt(datos[10]); //  datos[10] es el tipo de cliente (1 o 2)

                if (tipoCliente == 1) {
                    rbtnSocio.setSelected(true);
                } else if (tipoCliente == 2) {
                    rbtnInvitado.setSelected(true);
                } else {
                    rbtnSocio.setSelected(false);
                    rbtnInvitado.setSelected(false);
                    JOptionPane.showMessageDialog(this, "Tipo de cliente desconocido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
                txtMCcorreo.setText(datos[11]);
                txtMCbalance.setText(datos[12]);
                txtMCvalorcuota.setText(datos[13]);
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
        txtMCnom.setText("");
        txtMCapellidop.setText("");
        txtMCapellidom.setText("");
        jtaMCdirrec.setText("");
        fechanacChooser.setDate(null);
        txtMCtele.setText("");
        txtMCcelular.setText("");
        fechaingreChooser.setDate(new Date());
        cmbMCstatus.setSelectedIndex(0);
        txtMCcorreo.setText("");
        buttonGroup1.clearSelection();
        txtMCbalance.setText("");
        txtMCvalorcuota.setText("");
        txtMUAccion.setText("Creando");
    }
}
    

    
private boolean validarCampos() {
    if (!rbtnSocio.isSelected() && !rbtnInvitado.isSelected()) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de cliente.", "Validación", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    return  
        !txtMCid.getText().trim().isEmpty() &&
        !txtMCnom.getText().trim().isEmpty() &&
        !txtMCapellidop.getText().trim().isEmpty() &&
        !txtMCapellidom.getText().trim().isEmpty() &&
        !jtaMCdirrec.getText().trim().isEmpty() &&
        fechanacChooser.getDate() != null &&
        !txtMCtele.getText().trim().isEmpty() &&
        !txtMCcelular.getText().trim().isEmpty() &&
        fechaingreChooser.getDate() != null &&
        !txtMCvalorcuota.getText().trim().isEmpty();
}

    
        
private void guardarDatos() {
    if (!validarCampos()) {
        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.\nExcepto correo y balance.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int id_cliente;
    try {
        id_cliente = Integer.parseInt(txtMCid.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID de cliente inválido. Debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String nombre = txtMCnom.getText();
    String apellidop = txtMCapellidop.getText();
    String apellidom = txtMCapellidom.getText();
    String dirrecion = jtaMCdirrec.getText().replace("\n", " ").replace(",", "");
    String telefono = txtMCtele.getText();
    String celular = txtMCcelular.getText();
    String status = String.valueOf(cmbMCstatus.getSelectedItem());

    int tipocliente;
    if (rbtnSocio.isSelected()) {
        tipocliente = 1; // Socio
    } else if (rbtnInvitado.isSelected()) {
        tipocliente = 2; // Invitado
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione el tipo de cliente (Socio o Invitado).", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (status.equalsIgnoreCase("Activo") && tipocliente != 1) {
    JOptionPane.showMessageDialog(this, "Solo los clientes tipo Socio pueden tener estado Activo.", "Error", JOptionPane.ERROR_MESSAGE);
    return;
}

    String correo = txtMCcorreo.getText();

    double balance;
    double cuota = 0.0;

    balance =+ cuota;
    
    try {
        cuota = Double.parseDouble(txtMCvalorcuota.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Cuota inválida. Se establecerá en 0.0 por defecto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    txtMCbalance.setText(String.valueOf(balance));
    txtMCvalorcuota.setText(String.valueOf(cuota));

        // Obtener las fechas desde los JDateChooser
    Date fechaNacDate = fechanacChooser.getDate();
    Date fechaIngDate = fechaingreChooser.getDate();

    // Formatear las fechas a cadena con el formato "dd MMM yyyy"
    SimpleDateFormat sdfOutput = new SimpleDateFormat("dd MMM yyyy", new java.util.Locale("es", "ES"));

    String fechaNacString = sdfOutput.format(fechaNacDate);
    String fechaIngString = sdfOutput.format(fechaIngDate);

    // Incluir las fechas en el string para guardar en el archivo
    String nuevaLinea = id_cliente + "," + nombre + "," + apellidop + "," + apellidom + "," + dirrecion + "," +
            fechaNacString + "," + telefono + "," + celular + "," + fechaIngString + "," +
            status + "," + tipocliente + "," + correo + "," + balance + "," + cuota;


    File archivo = new File(FILE_PATH);
    boolean clienteExiste = false;
    StringBuilder contenido = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length > 0 && datos[0].equals(String.valueOf(id_cliente))) {
                contenido.append(nuevaLinea).append("\n");
                clienteExiste = true;
            } else {
                contenido.append(linea).append("\n");
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!clienteExiste) {
        contenido.append(nuevaLinea).append("\n");
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
        bw.write(contenido.toString());
        JOptionPane.showMessageDialog(this, clienteExiste ? "Cliente actualizado correctamente." : "Cliente guardado exitosamente.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    limpiarCampos();
}


    
    private void limpiarCampos() {
        txtMCid.setText("");
        txtMCnom.setText("");
        txtMCapellidop.setText("");
        txtMCapellidom.setText("");
        jtaMCdirrec.setText("");
        fechanacChooser.setDate(null);
        txtMCtele.setText("");
        txtMCcelular.setText("");
        fechaingreChooser.setDate(null);
        cmbMCstatus.setSelectedIndex(0);
        txtMCcorreo.setText("");
        txtMCbalance.setText("");
        buttonGroup1.clearSelection();
        txtMCvalorcuota.setText("");
        txtMUAccion.setText("");
        
    }
    
    
    private void cancelar() {
        this.dispose();  // cierras MSalas

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMCid = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtMCapellidom = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jBCancelar = new javax.swing.JButton();
        jBLimpiar = new javax.swing.JButton();
        jBGuardar = new javax.swing.JButton();
        txtMUAccion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtMCcelular = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtMCvalorcuota = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtMCbalance = new javax.swing.JTextField();
        txtMCcorreo = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtMCnom = new javax.swing.JTextField();
        txtMCapellidop = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaMCdirrec = new javax.swing.JTextArea();
        txtMCtele = new javax.swing.JTextField();
        cmbMCstatus = new javax.swing.JComboBox<>();
        fechanacChooser = new com.toedter.calendar.JDateChooser();
        fechaingreChooser = new com.toedter.calendar.JDateChooser();
        rbtnSocio = new javax.swing.JRadioButton();
        rbtnInvitado = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Mantenimiento de Cliente");

        jLabel2.setText("ID Cliente:");

        txtMCid.setColumns(12);
        txtMCid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMCidActionPerformed(evt);
            }
        });

        jLabel3.setText("Nombre:");

        jLabel4.setText("Apellido Paterno:");

        txtMCapellidom.setColumns(12);

        jLabel5.setText("Apellido Materno:");

        jLabel6.setText("Dirección:");

        jLabel7.setText("Fecha de Nacimiento:");

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

        jLabel8.setText("Status:");

        jLabel9.setText("Fecha Ingreso:");

        txtMCcelular.setColumns(12);

        jLabel10.setText("Celular:");

        jLabel11.setText("Teléfono:");

        txtMCvalorcuota.setColumns(12);

        jLabel12.setText("Valor de Cuota:");

        jLabel13.setText("Balance:");

        txtMCbalance.setEditable(false);
        txtMCbalance.setColumns(12);
        txtMCbalance.setEnabled(false);

        txtMCcorreo.setColumns(12);

        jLabel14.setText("Correo:");

        jLabel15.setText("Tipo de Cliente:");

        txtMCnom.setColumns(10);

        txtMCapellidop.setColumns(10);
        txtMCapellidop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMCapellidopActionPerformed(evt);
            }
        });

        jtaMCdirrec.setColumns(12);
        jtaMCdirrec.setLineWrap(true);
        jtaMCdirrec.setRows(3);
        jtaMCdirrec.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jtaMCdirrec);

        txtMCtele.setColumns(12);

        cmbMCstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Pasivo" }));
        cmbMCstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMCstatusActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtnSocio);
        rbtnSocio.setText("Socio Activo");
        rbtnSocio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSocioActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtnInvitado);
        rbtnInvitado.setText("Invitado");
        rbtnInvitado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnInvitadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtMUAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBCancelar)
                .addGap(59, 59, 59)
                .addComponent(jBLimpiar)
                .addGap(60, 60, 60)
                .addComponent(jBGuardar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel2)
                            .addComponent(jLabel11))
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMCapellidop, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtMCapellidom)
                                .addComponent(txtMCid)
                                .addComponent(txtMCnom, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMCtele, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechanacChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel15)
                                        .addComponent(jLabel8))
                                    .addGap(235, 235, 235))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addGap(270, 270, 270)))
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(164, 164, 164)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(rbtnSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtMCvalorcuota, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtMCbalance, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtMCcorreo)
                                        .addComponent(rbtnInvitado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(cmbMCstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMCcelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fechaingreChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(270, 270, 270)))
                .addGap(47, 47, 47))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(txtMUAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel1)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtMCid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMCcelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtMCnom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtMCapellidop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtMCapellidom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel7))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(fechanacChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtMCtele, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(fechaingreChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cmbMCstatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(rbtnSocio))
                        .addGap(18, 18, 18)
                        .addComponent(rbtnInvitado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMCcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMCbalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMCvalorcuota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
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

    private void txtMCidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMCidActionPerformed
    cargarUsuario();
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
        guardarDatos();        
    }//GEN-LAST:event_jBGuardarActionPerformed

    private void txtMCapellidopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMCapellidopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMCapellidopActionPerformed

    private void cmbMCstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMCstatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMCstatusActionPerformed

    private void rbtnSocioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSocioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnSocioActionPerformed

    private void rbtnInvitadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnInvitadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnInvitadoActionPerformed

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
            java.util.logging.Logger.getLogger(MCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MCliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbMCstatus;
    private com.toedter.calendar.JDateChooser fechaingreChooser;
    private com.toedter.calendar.JDateChooser fechanacChooser;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jtaMCdirrec;
    private javax.swing.JRadioButton rbtnInvitado;
    private javax.swing.JRadioButton rbtnSocio;
    private javax.swing.JTextField txtMCapellidom;
    private javax.swing.JTextField txtMCapellidop;
    private javax.swing.JTextField txtMCbalance;
    private javax.swing.JTextField txtMCcelular;
    private javax.swing.JTextField txtMCcorreo;
    private javax.swing.JTextField txtMCid;
    private javax.swing.JTextField txtMCnom;
    private javax.swing.JTextField txtMCtele;
    private javax.swing.JTextField txtMCvalorcuota;
    private javax.swing.JTextField txtMUAccion;
    // End of variables declaration//GEN-END:variables
}
