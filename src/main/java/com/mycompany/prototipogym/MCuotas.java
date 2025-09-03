/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.prototipogym;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author asist-depti
 */
public class MCuotas extends javax.swing.JFrame {

    private static final String CLIENTE_PATH = "archivos/cliente.txt";
    private static final String COBROS_PATH = "archivos/cobros.txt";
    public static final String EST_PROCESADO  = "Procesado";
    private final List<Object[]> listaDetalle = new ArrayList<>();

    
        public MCuotas() {
        initComponents();
        
        setTitle("Pantera Fitness");
        setLocationRelativeTo(null);
        txtMUAccion.setText("Creando");
        agregarListenersFiltro();
        
    }

        
    private void verificarOCargarCuota() {
        String idCliente = txtIdCliente.getText().trim();
        if (idCliente.isEmpty()) return;

        cargarDatosCliente(idCliente);
        generarDetalleEnMemoria(idCliente);
    }

    private void cargarDatosCliente(String idCliente) {
        try (BufferedReader br = new BufferedReader(new FileReader(COBROS_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(",");
                if (d.length >= 6 && d[2].equals(idCliente)) {
                    txtnombrecliente.setText(buscarNombreEnCliente(d[2]));
                    txtMCvalorcobro.setText(d[3]);
                    fechaChooser.setDate(
                        new SimpleDateFormat("dd MMM yyyy", new Locale("es","ES")).parse(d[1])
                    );
                    break;
                }
            }
        } catch (Exception e) {
            mostrarError("cargar datos cliente", e);
        }
    }

    private String buscarNombreEnCliente(String idCliente) {
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(",");
                if (d[0].equals(idCliente)) return d[1];
            }
        } catch (IOException e) {
            mostrarError("buscar nombre cliente", e);
        }
        return "Cliente no encontrado";
    }

private void generarDetalleEnMemoria(String idCliente) {
    DefaultTableModel modelo = (DefaultTableModel) tableDetalle.getModel();
    modelo.setRowCount(0);
    listaDetalle.clear();  // importante: vaciar lista antes de recargar
    try (BufferedReader br = new BufferedReader(new FileReader(COBROS_PATH))) {
        String linea;
        int contador = 1;
        while ((linea = br.readLine()) != null) {
            String[] d = linea.split(",");
            if (d.length >= 6 && d[2].equals(idCliente)
                    && !d[5].trim().equalsIgnoreCase(EST_PROCESADO)) {
                Object[] fila = new Object[]{
                    idCliente,
                    String.format("%03d", contador++),
                    d[4],
                    d[3],
                    d[0],
                    Boolean.valueOf(d[5])
                };
                modelo.addRow(fila);
                listaDetalle.add(fila);    // <— aquí agregas la fila a la lista
            }
        }
    } catch (IOException e) {
        mostrarError("generar detalle en memoria", e);
    }
}

    private void guardarDetalleEnArchivo() {
        try {
            // Leemos el archivo original
            List<String> lineasActualizadas = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(COBROS_PATH))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] d = linea.split(",");
                    if (d.length >= 6) {
                        // Buscar si este ID_Cobro existe en la tabla
                        boolean actualizado = false;
                        for (int i = 0; i < tableDetalle.getRowCount(); i++) {
                            String idCobroTabla = String.valueOf(tableDetalle.getValueAt(i, 4));
                            if (d[0].equals(idCobroTabla)) {
                                // Actualizar el status (pagado)
                                d[5] = String.valueOf(tableDetalle.getValueAt(i, 5)); // true/false
                                actualizado = true;
                                break;
                            }
                        }
                        lineasActualizadas.add(String.join(",", d));
                    } else {
                        // Línea mal formada, la dejamos igual
                        lineasActualizadas.add(linea);
                    }
                }
            }

            // Escribimos el archivo actualizado
            try (PrintWriter pw = new PrintWriter(new FileWriter(COBROS_PATH))) {
                for (String linea : lineasActualizadas) {
                    pw.println(linea);
                }
            }

            JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.");
        } catch (IOException e) {
            mostrarError("guardar detalle", e);
        }
    }




    private void agregarListenersFiltro() {
        txtFiltro.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarTabla(txtFiltro.getText().trim());
            }
        });
    }


    
    private void filtrarTabla(String texto) {
        int columnaCombo = cmbFiltro.getSelectedIndex();

        // Mapear índice del combo a índice real de columna en listaDetalle
        int[] mapeoColumnas = {1, 2}; // Solo filtrar por ID Cliente, No. Cuota y Concepto
        if (columnaCombo < 0 || columnaCombo >= mapeoColumnas.length) return;

        int columna = mapeoColumnas[columnaCombo];

        DefaultTableModel modelo = (DefaultTableModel) tableDetalle.getModel();
        modelo.setRowCount(0); // Limpiar tabla

        for (Object[] fila : listaDetalle) {
            String valorCelda = String.valueOf(fila[columna]).toLowerCase();
            if (valorCelda.contains(texto.toLowerCase())) {
                modelo.addRow(fila);
            }
        }
    }

public void generarFacturaYMarcarProcesadoDesdeChecks() {
    final int COL_NO_CUOTA = 1; // No. Cuota
    final int COL_CONCEPTO = 2; // Concepto
    final int COL_VALOR = 3;    // Valor
    final int COL_IDCOBRO = 4;  // IdCobro
    final int COL_PAGADO = 5;   // Checkbox Booleano

    try {
        // 1) Recolectar filas visibles que tengan el checkbox marcado
        List<Integer> filasATomar = new ArrayList<>();
        for (int viewRow = 0; viewRow < tableDetalle.getRowCount(); viewRow++) {
            Object statusObj = null;
            try { statusObj = tableDetalle.getValueAt(viewRow, COL_PAGADO); }
            catch (Exception ex) { statusObj = null; }

            boolean marcado = false;
            if (statusObj instanceof Boolean) {
                marcado = (Boolean) statusObj;
            } else if (statusObj != null) {
                String s = statusObj.toString().trim().toLowerCase();
                marcado = s.equals("true") || s.equals("sí") || s.equals("si") || s.equals("1");
            }
            if (marcado) filasATomar.add(viewRow);
        }

        if (filasATomar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay filas marcadas con el checkbox.", "Generar factura", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 2) Generar PDF (puedes adaptar formato / columnas)
        String idCliente = txtIdCliente.getText().trim().isEmpty() ? "sin_id" : txtIdCliente.getText().trim();
        String nombreArchivo = String.format("Factura_%s_%d.pdf", idCliente, System.currentTimeMillis());

        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
        document.open();

        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font fontTablaHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

        document.add(new Paragraph("Pantera Fitness", fontTitulo));
        document.add(new Paragraph("Factura de Pagos", fontNormal));
        document.add(Chunk.NEWLINE);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
        String fechaTexto = (fechaChooser.getDate() != null) ? sdf.format(fechaChooser.getDate()) : sdf.format(new Date());
        document.add(new Paragraph("Fecha: " + fechaTexto, fontNormal));
        document.add(new Paragraph("ID Cliente: " + idCliente, fontNormal));
        document.add(new Paragraph("Nombre: " + txtnombrecliente.getText(), fontNormal));
        document.add(Chunk.NEWLINE);

        // Tabla con N°, Concepto, Valor, IdCobro (añadí IdCobro para referencia fiscal)
        PdfPTable tabla = new PdfPTable(new float[]{1f, 6f, 2f, 2f});
        tabla.setWidthPercentage(100);
        tabla.addCell(new Paragraph("N°", fontTablaHeader));
        tabla.addCell(new Paragraph("Concepto", fontTablaHeader));
        tabla.addCell(new Paragraph("Valor", fontTablaHeader));
        tabla.addCell(new Paragraph("IdCobro", fontTablaHeader));

        double sumaTotal = 0.0;
        int contador = 0;
        DefaultTableModel modelo = (DefaultTableModel) tableDetalle.getModel();

        for (int viewRow : filasATomar) {
            Object oConcepto = modelo.getValueAt(viewRow, COL_CONCEPTO);
            Object oValor = modelo.getValueAt(viewRow, COL_VALOR);
            Object oIdCobro = modelo.getValueAt(viewRow, COL_IDCOBRO);

            String concepto = oConcepto == null ? "" : oConcepto.toString();
            String valorRaw = oValor == null ? "0" : oValor.toString();
            String idCobro = oIdCobro == null ? "" : oIdCobro.toString();

            double valor = 0.0;
            try {
                String tmp = valorRaw.replaceAll("[^0-9,\\.\\-]", "");
                if (tmp.indexOf(',') > -1 && tmp.indexOf('.') > -1) tmp = tmp.replace(".", "").replace(",", ".");
                else if (tmp.indexOf(',') > -1 && tmp.indexOf('.') == -1) tmp = tmp.replace(",", ".");
                valor = Double.parseDouble(tmp);
            } catch (Exception ex) { valor = 0.0; }

            contador++;
            sumaTotal += valor;

            tabla.addCell(new Paragraph(String.valueOf(contador), fontNormal));
            tabla.addCell(new Paragraph(concepto, fontNormal));
            tabla.addCell(new Paragraph(String.format("%.2f", valor), fontNormal));
            tabla.addCell(new Paragraph(idCobro, fontNormal));
        }

        document.add(tabla);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Valor Total: $" + String.format("%.2f", sumaTotal), fontNormal));
        document.close();

        // Abrir PDF si es posible
        try {
            File pdfFile = new File(nombreArchivo);
            if (pdfFile.exists() && Desktop.isDesktopSupported()) Desktop.getDesktop().open(pdfFile);
        } catch (IOException ex) { System.err.println("No se pudo abrir el PDF automáticamente: " + ex.getMessage()); }

        // 3) Marcar cobros como procesados en archivo y eliminar filas de la tabla/lista
        marcarFilasComoProcesadasYRemover(filasATomar);

        JOptionPane.showMessageDialog(this, "Factura generada: " + nombreArchivo + " (" + contador + " ítems)");

    } catch (Exception e) {
        mostrarError("generar factura y marcar procesado", e);
    }
}

/**
 * Marca en el archivo COBROS_PATH los IdCobro correspondientes y remueve las filas
 * de la tabla y de listaDetalle para que ya no aparezcan.
 */
private void marcarFilasComoProcesadasYRemover(List<Integer> filasATomar) {
    final int COL_IDCOBRO = 4;
    DefaultTableModel modelo = (DefaultTableModel) tableDetalle.getModel();

    // 1) Recopilar IdCobro de las filas a procesar
    Set<String> idCobros = new HashSet<>();
    for (int r : filasATomar) {
        Object val = modelo.getValueAt(r, COL_IDCOBRO);
        if (val != null) idCobros.add(val.toString());
    }

    if (idCobros.isEmpty()) return;

    // 2) Actualizar archivo (marcar como EST_PROCESADO)
    try {
        actualizarEstadoCobros(idCobros, EST_PROCESADO);
    } catch (IOException ex) {
        mostrarError("actualizar estado cobros", ex);
        // aunque falle el archivo, continuamos con la limpieza de la tabla para evitar duplicados en UI
    }

    // 3) Eliminar filas de la tabla (hacerlo en orden descendente para no romper índices)
    Collections.sort(filasATomar, Collections.reverseOrder());
    for (int viewRow : filasATomar) {
        try {
            // obtener IdCobro antes de remover (por seguridad ya lo tenemos), luego remover
            modelo.removeRow(viewRow);
        } catch (Exception ex) {
            // ignorar fallos puntuales
        }
    }

    // 4) Además remover de listaDetalle (tu lista en memoria)
    // listaDetalle contiene Object[] donde la posición 4 es IdCobro (según tu generación)
    try {
        Iterator<Object[]> it = listaDetalle.iterator();
        while (it.hasNext()) {
            Object[] fila = it.next();
            if (fila.length > 4 && idCobros.contains(String.valueOf(fila[4]))) {
                it.remove();
            }
        }
    } catch (Exception ex) {
        // no crítico
    }
}

/**
 * Lee COBROS_PATH y actualiza la columna de status (posición 5) para los IdCobro pasados.
 */
private void actualizarEstadoCobros(Set<String> idCobros, String nuevoEstado) throws IOException {
    List<String> lineas = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(COBROS_PATH))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] d = linea.split(",", -1); // mantener campos vacíos
            if (d.length >= 6 && idCobros.contains(d[0])) {
                d[5] = nuevoEstado;
            }
            // Si la línea tiene menos de 6 campos la dejamos igual (o puedes normalizarla según prefieras)
            lineas.add(String.join(",", d));
        }
    }

    // Sobrescribir archivo
    try (PrintWriter pw = new PrintWriter(new FileWriter(COBROS_PATH))) {
        for (String l : lineas) pw.println(l);
    }
}
    
   
      


    private void mostrarError(String contexto, Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error en " + contexto + ": " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void limpiarCampos() {
        txtIdCliente.setText("");
        txtMCvalorcobro.setText("");
        txtFiltro.setText("");
        ((DefaultTableModel) tableDetalle.getModel()).setRowCount(0);
        listaDetalle.clear();
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
        cmbfiltro = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdCliente = new javax.swing.JTextField();
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
        tableDetalle = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        cmbFiltro = new javax.swing.JComboBox<>();

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        cmbfiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID Cliente", "Secuencia", "Concepto", "Cuota", "ID Cobro" }));
        cmbfiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbfiltroActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Cuotas");

        txtIdCliente.setColumns(12);
        txtIdCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdClienteFocusLost(evt);
            }
        });
        txtIdCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdClienteActionPerformed(evt);
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

        tableDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Cliente", "No. Cuota", "Concepto", "Valor Cuota", "ID Cobro", "Pago"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableDetalle);

        jLabel9.setText("Filtrar por:");

        txtFiltro.setColumns(12);
        txtFiltro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroFocusLost(evt);
            }
        });
        txtFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroActionPerformed(evt);
            }
        });
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
        });

        cmbFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No. Cuota", "Concepto" }));
        cmbFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFiltroActionPerformed(evt);
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jBCancelar)
                        .addGap(198, 198, 198)
                        .addComponent(jBLimpiar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBGuardar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMCvalorcobro, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(33, 33, 33)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addComponent(txtnombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(fechaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cmbFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(txtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(52, 52, 52))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator2)
                .addContainerGap())
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
                        .addComponent(jLabel7)
                        .addComponent(txtnombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtMCvalorcobro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        guardarDetalleEnArchivo();       
        generarFacturaYMarcarProcesadoDesdeChecks();
    }//GEN-LAST:event_jBGuardarActionPerformed

    private void txtIdClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdClienteActionPerformed
        verificarOCargarCuota();
    }//GEN-LAST:event_txtIdClienteActionPerformed

    private void txtMCvalorcobroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMCvalorcobroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMCvalorcobroActionPerformed

    private void txtnombreclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreclienteActionPerformed

    private void txtIdClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdClienteFocusLost
        
    }//GEN-LAST:event_txtIdClienteFocusLost

    private void txtFiltroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroFocusLost

    private void txtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroActionPerformed

    private void cmbfiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbfiltroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbfiltroActionPerformed

    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        filtrarTabla(txtFiltro.getText());
    }//GEN-LAST:event_txtFiltroKeyReleased

    private void cmbFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFiltroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFiltroActionPerformed

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
    private javax.swing.JComboBox<String> cmbFiltro;
    private javax.swing.JComboBox<String> cmbfiltro;
    private com.toedter.calendar.JDateChooser fechaChooser;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTable tableDetalle;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtMCvalorcobro;
    private javax.swing.JTextField txtMUAccion;
    private javax.swing.JTextField txtnombrecliente;
    // End of variables declaration//GEN-END:variables
}
