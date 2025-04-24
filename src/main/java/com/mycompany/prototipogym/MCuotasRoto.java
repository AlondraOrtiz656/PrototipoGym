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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.ListSelectionModel;

/**
 *
 * @author asist-depti
 */
public class MCuotasRoto extends javax.swing.JFrame {

    public MCuotasRoto() {
        initComponents();
        setTitle("Pantera Fitness");
        setLocationRelativeTo(null);
        txtMUAccion.setText("Creando");

        
    }

    private static final String ENCABEZADO_PATH = "archivos/encabezado_cuota.txt";
    private static final String DETALLE_PATH = "archivos/detalle_cuota.txt";
    private static final String CLIENTE_PATH = "archivos/cliente.txt";
    private static final String COBROS_PATH = "archivos/cobros.txt";
    
 
   
private void verificarOCargarCuota() throws IOException {
    String idCuota   = txtMCid.getText().trim();
    String idCliente = txtMC_IDcliente.getText().trim();
    if (idCuota.isEmpty() || idCliente.isEmpty()) return;

    if (estaCuotaProcesada(idCuota)) {
        JOptionPane.showMessageDialog(null, "Esta cuota ya fue procesada.");
        return;
    }

    if (existeEncabezado(idCuota, idCliente)) {
        txtMUAccion.setText("Modificando");
        cargarEncabezado(idCuota);
        refrescarDetalle(idCuota, idCliente);
    } else {
        txtMUAccion.setText("Creando");
        fechaChooser.setDate(new Date());
        cargarNombreCliente(idCliente);
        generarDetalleEnMemoria(idCuota, idCliente);
    }
}
private void refrescarDetalle(String idCuota, String idCliente) throws IOException {
    DefaultTableModel modelo = (DefaultTableModel) TMCdetalle.getModel();
    modelo.setRowCount(0);
    
    // A) cargo las líneas de detalle que NO estén Procesadas
    try (BufferedReader br = new BufferedReader(new FileReader(DETALLE_PATH))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] d = linea.split(",");
            if (d.length >= 6
                && d[0].equals(idCuota)
                && !d[5].trim().equalsIgnoreCase("Procesado")) {
                boolean status = Boolean.parseBoolean(d[5].trim());
                modelo.addRow(new Object[]{
                    d[0], d[1], d[2], d[3], d[4], status
                });
            }
        }
    }

    // B) añado los conceptos faltantes (siempre con status false)
    List<String> faltantes = calcularConceptosFaltantes(idCliente, idCuota);
    int sec = obtenerUltimoSecCuota(idCuota);
    String valor = txtMCvalorcobro.getText().trim();
    for (String conc : faltantes) {
        sec++;
        modelo.addRow(new Object[]{
            idCuota,
            String.format("%03d", sec),
            conc,
            valor,
            buscarIdCobroCorrespondiente(idCliente, conc),
            Boolean.FALSE
        });
    }
}




    private boolean tieneDetalle(String idCuota) {
    try (Stream<String> lines = Files.lines(Paths.get(DETALLE_PATH))) {
        return lines
            .map(l -> l.split(","))
            .anyMatch(p -> p.length>=6 && p[0].equals(idCuota));
    } catch (IOException e) {
        return false;
    }
}

private boolean estaCuotaProcesada(String idCuota) {
    try {
        List<String> lineasDetalle = Files.readAllLines(Paths.get("archivos/detalle_cuota.txt"));
        for (String linea : lineasDetalle) {
            String[] partes = linea.split(",");
            if (partes.length >= 6 && partes[0].equals(idCuota)) {
                if (partes[5].equalsIgnoreCase("Procesado")) {
                    return true;
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al verificar cuota: " + e.getMessage());
    }
    return false;
}


    private boolean existeEncabezado(String idCuota, String idCliente) {
        try (BufferedReader br = new BufferedReader(new FileReader(ENCABEZADO_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 4 && datos[0].equals(idCuota) && datos[2].equals(idCliente)) {
                    return true;
                }
            }
        } catch (IOException ignored) {}
        return false;
    }

    private void cargarEncabezado(String idCuota) {
        try (BufferedReader br = new BufferedReader(new FileReader(ENCABEZADO_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[0].equals(idCuota)) {
                    SimpleDateFormat formato = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
                    fechaChooser.setDate(formato.parse(datos[1]));
                    txtMC_IDcliente.setText(datos[2]);
                    txtMCvalorcobro.setText(datos[3]);
                    cargarNombreCliente(datos[2]);
                    break;
                }
            }
        } catch (Exception e) {
            mostrarError("cargar encabezado", e);
        }
    }

    private void cargarDetalleCuota(String idCuota) {
        DefaultTableModel modelo = (DefaultTableModel) TMCdetalle.getModel();
        modelo.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(DETALLE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 6 && datos[0].equals(idCuota) && !datos[5].equalsIgnoreCase("Procesado")) {
                    boolean status = Boolean.parseBoolean(datos[5]);
                    modelo.addRow(new Object[]{
                        datos[0], // idCuota
                        datos[1], // sec
                        datos[2], // concepto
                        datos[3], // valor
                        datos[4], // idCobro
                        status    // status booleano
                    });

                }
            }

        } catch (IOException e) {
            mostrarError("cargar detalle cuota", e);
        }
    }


 private void generarDetalleEnMemoria(String idCuota, String idCliente) throws IOException {
    DefaultTableModel modelo = (DefaultTableModel) TMCdetalle.getModel();
    modelo.setRowCount(0);
    List<String> conceptos = calcularConceptosFaltantes(idCliente, idCuota); // <--- AQUÍ
    int sec = obtenerUltimoSecCuota(idCuota);
    String valor = txtMCvalorcobro.getText().trim();
    for (String conc : conceptos) {
        sec++;
        modelo.addRow(new Object[]{idCuota,
                                  String.format("%03d", sec),
                                  conc,
                                  valor,
                                  buscarIdCobroCorrespondiente(idCliente, conc), // <--- AQUÍ
                                  Boolean.FALSE});
    }
}




private List<String> calcularConceptosFaltantes(String idCliente, String idCuota) throws IOException {
    // 1) recojo todos los conceptos que en DETALLE_PATH están PROCESADOS (cualquier idCuota)
    Set<String> conceptosProcesados = new HashSet<>();
    try (BufferedReader br = new BufferedReader(new FileReader(DETALLE_PATH))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] p = linea.split(",");
            if (p.length >= 6 && p[5].trim().equalsIgnoreCase("Procesado")) {
                conceptosProcesados.add(p[2].trim());
            }
        }
    }

    // 2) recojo los conceptos de COBROS_PATH que estén PENDIENTES (status == false) y sean de este cliente
    List<String> conceptosCobro = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(COBROS_PATH))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] p = linea.split(",");
            if (p.length >= 6
                && p[2].trim().equals(idCliente.trim())
                && p[5].trim().equalsIgnoreCase("false")) {
                conceptosCobro.add(p[4].trim());
            }
        }
    }

    // 3) recojo los conceptos que ya están en ESTA cuota
    Set<String> conceptosEnEstaCuota = new HashSet<>();
    try (BufferedReader br = new BufferedReader(new FileReader(DETALLE_PATH))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] p = linea.split(",");
            if (p.length >= 3
                && p[0].trim().equals(idCuota.trim())) {
                conceptosEnEstaCuota.add(p[2].trim());
            }
        }
    }

    // 4) filtro: solo lo que está en conceptosCobro, y NO está en conceptosEnEstaCuota, y NO en procesados global
    List<String> faltantes = new ArrayList<>();
    for (String c : conceptosCobro) {
        if (!conceptosEnEstaCuota.contains(c)
            && !conceptosProcesados.contains(c)) {
            faltantes.add(c);
        }
    }
    return faltantes;
}



private String buscarIdCobroCorrespondiente(String idCliente, String concepto) {
    try (BufferedReader br = new BufferedReader(new FileReader("archivos/cobros.txt"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes.length >= 6
                && partes[2].trim().equals(idCliente.trim())
                && partes[4].trim().equalsIgnoreCase(concepto.trim())
                && partes[5].trim().equalsIgnoreCase("false")) {
                return partes[0].trim();  // Id_Cobro pendiente
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return "";
}



    private int obtenerUltimoSecCuota(String idCuota) {
        int ultimo = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(DETALLE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(",");
                if (d[0].equals(idCuota)) {
                    try {
                        ultimo = Math.max(ultimo, Integer.parseInt(d[1]));
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException ignored) {}
        return ultimo;
    }

    private void guardarCuota() {
        String idCuota = txtMCid.getText().trim();
        String idCliente = txtMC_IDcliente.getText().trim();
        String valor = txtMCvalorcobro.getText().trim();
        String fecha = new SimpleDateFormat("dd MMM yyyy", new Locale("es","ES")).format(fechaChooser.getDate());
        actualizarEncabezadoArchivo(idCuota, fecha, idCliente, valor);
        actualizarDetalleArchivo(idCuota);
        
        JOptionPane.showMessageDialog(null, "Cuota guardada exitosamente.");

    }

    private void actualizarEncabezadoArchivo(String idCuota,
                                            String fecha,
                                            String idCliente,
                                            String valor) {
        String lineaNueva = String.join(",",
                                        idCuota,
                                        fecha,
                                        idCliente,
                                        valor);
        List<String> todos = new ArrayList<>();
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(ENCABEZADO_PATH))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (l.startsWith(idCuota + ",")) {
                    todos.add(lineaNueva);
                    found = true;
                } else {
                    todos.add(l);
                }
            }
        } catch (IOException ignored) {}
        if (!found) todos.add(lineaNueva);
        try (PrintWriter pw = new PrintWriter(new FileWriter(ENCABEZADO_PATH))) {
            for (String s : todos) pw.println(s);
        } catch (IOException e) {
            mostrarError("actualizar encabezado", e);
        }
    }

private void actualizarDetalleArchivo(String idCuota) {
    List<String> otros = new ArrayList<>();

    // 1) Leer detalle_cuota.txt y conservar:
    //    a) Todas las líneas de otras cuotas (p[0] != idCuota)
    //    b) Las líneas de ESTA cuota que ya estén procesadas (p[5] == "true" o "Procesado")
    try (BufferedReader br = new BufferedReader(new FileReader(DETALLE_PATH))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] p = linea.split(",");
            boolean mismaCuota = p.length >= 1 && p[0].trim().equals(idCuota);
            boolean procesada   = p.length >= 6 && 
                                  (p[5].trim().equalsIgnoreCase("true") 
                                || p[5].trim().equalsIgnoreCase("Procesado"));
            if (!mismaCuota || procesada) {
                // conservamos esta línea
                otros.add(linea);
            }
            // si es mismaCuota && !procesada, la descartamos;
            // luego la reemplazaremos con lo que haya en la tabla
        }
    } catch (IOException ignored) {}

    // 2) Añadir todas las filas que están AHORA en la tabla (pendientes y nuevas)
    DefaultTableModel modelo = (DefaultTableModel) TMCdetalle.getModel();
    for (int i = 0; i < modelo.getRowCount(); i++) {
        Object id    = modelo.getValueAt(i, 0);
        Object sec   = modelo.getValueAt(i, 1);
        Object conc  = modelo.getValueAt(i, 2);
        Object val   = modelo.getValueAt(i, 3);
        Object cob   = modelo.getValueAt(i, 4);
        Object st    = modelo.getValueAt(i, 5);
        // reconstruyo la línea
        String nueva = String.join(",",
                    id.toString(),
                    sec.toString(),
                    conc.toString(),
                    val.toString(),
                    cob.toString(),
                    st.toString());
        otros.add(nueva);
    }

    // 3) Volcar todo de nuevo a detalle_cuota.txt
    try (PrintWriter pw = new PrintWriter(new FileWriter(DETALLE_PATH))) {
        for (String s : otros) {
            pw.println(s);
        }
    } catch (IOException e) {
        mostrarError("actualizar detalle", e);
    }
}



    private void cargarNombreCliente(String idCliente) {
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(",");
                if (d[0].equals(idCliente) && d.length > 13) {
                    txtnombrecliente.setText(d[1]);
                    txtMCvalorcobro.setText(d[13]);
                    return;
                }
            }
        } catch (IOException e) {
            mostrarError("cargar cliente", e);
        }
        txtnombrecliente.setText("Cliente no encontrado");
        txtMCvalorcobro.setText("");
    }


public void generarFacturaPDF(String idCuota) {
    String nombreArchivo = "Factura_" + idCuota + ".pdf";
    try {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
        document.open();

        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Factura de Cuota", fontTitulo));
        document.add(Chunk.NEWLINE);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
        document.add(new Paragraph("Fecha: " + sdf.format(fechaChooser.getDate()), fontNormal));
        document.add(new Paragraph("ID Cliente: " + txtMC_IDcliente.getText(), fontNormal));
        document.add(new Paragraph("Nombre: " + txtnombrecliente.getText(), fontNormal));
        document.add(Chunk.NEWLINE);

        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new int[]{1, 4, 2});
        tabla.addCell("N°");
        tabla.addCell("Concepto");
        tabla.addCell("Valor");

        DefaultTableModel modelo = (DefaultTableModel) TMCdetalle.getModel();
        double sumaTotal = 0;
        int contador = 0;

        for (int filaModelo = 0; filaModelo < modelo.getRowCount(); filaModelo++) {
            Object statusObj = modelo.getValueAt(filaModelo, 5);
            boolean pagado = statusObj instanceof Boolean
                             ? (Boolean) statusObj
                             : Boolean.parseBoolean(statusObj.toString());

            if (pagado) {
                contador++;
                String concepto = modelo.getValueAt(filaModelo, 2).toString();
                double valor = Double.parseDouble(modelo.getValueAt(filaModelo, 3).toString());
                sumaTotal += valor;

                tabla.addCell(String.valueOf(contador));
                tabla.addCell(concepto);
                tabla.addCell(String.format("%.2f", valor));
            }
        }

        document.add(tabla);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Valor Total: $" + String.format("%.2f", sumaTotal), fontNormal));

        document.close();

        // Abrir el PDF automáticamente si el entorno lo permite
        try {
            File pdfFile = new File(nombreArchivo);
            if (pdfFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }
        } catch (IOException ex) {
            System.err.println("No se pudo abrir el PDF automáticamente: " + ex.getMessage());
        }

        JOptionPane.showMessageDialog(null,
            "Factura generada: " + nombreArchivo + " (" + contador + " ítems)");
    } catch (Exception e) {
        mostrarError("generar factura PDF", e);
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
        btnfactura = new javax.swing.JButton();

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

        btnfactura.setText("Generar Factura");
        btnfactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfacturaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(224, 224, 224)
                .addComponent(txtMUAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jBCancelar))
                                .addGap(198, 198, 198)
                                .addComponent(jBLimpiar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                                .addComponent(jBGuardar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtnombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(fechaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtMCvalorcobro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(btnfactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addGap(52, 52, 52))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMC_IDcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtMCid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                    .addComponent(txtMCvalorcobro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnfactura))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
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
        try {
            verificarOCargarCuota();
        } catch (IOException ex) {
            Logger.getLogger(MCuotasRoto.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            verificarOCargarCuota();
        } catch (IOException ex) {
            Logger.getLogger(MCuotasRoto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtMC_IDclienteActionPerformed

    private void txtMCvalorcobroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMCvalorcobroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMCvalorcobroActionPerformed

    private void txtnombreclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreclienteActionPerformed

    private void txtMC_IDclienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMC_IDclienteFocusLost
        
    }//GEN-LAST:event_txtMC_IDclienteFocusLost

    private void btnfacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfacturaActionPerformed
     String idCuota = txtMCid.getText();
    if (TMCdetalle.getSelectedRowCount() == 0) {
        JOptionPane.showMessageDialog(null, "Selecciona al menos una fila para generar la factura.");
        return;
    }
    generarFacturaPDF(idCuota);
    }//GEN-LAST:event_btnfacturaActionPerformed

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
            java.util.logging.Logger.getLogger(MCuotasRoto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MCuotasRoto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MCuotasRoto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MCuotasRoto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new MCuotasRoto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TMCdetalle;
    private javax.swing.JButton btnfactura;
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
