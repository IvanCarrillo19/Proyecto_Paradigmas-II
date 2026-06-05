package vistas;

import conexion.Conexion;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JDialog;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;

public class Equipos extends JFrame {

    // Paleta de Colores Dark Mode Premium
    private final Color FONDO_PRINCIPAL = new Color(24, 24, 27);  // Gris casi negro (Zinc 900)
    private final Color FONDO_PANELES = new Color(39, 39, 42);    // Gris oscuro (Zinc 800)
    private final Color FONDO_INPUTS = new Color(63, 63, 70);     // Gris medio para los campos
    private final Color TEXTO_BLANCO = new Color(250, 250, 250);   // Blanco puro
    private final Color TEXTO_GRIS = new Color(161, 161, 170);     // Gris claro para etiquetas
    private final Color AZUL_ACCENT = new Color(37, 99, 235);     // Azul brillante para botones principales
    private final Color VERDE_EXITO = new Color(22, 163, 74);     // Verde para guardar/jugadores
    private final Color ROJO_ELIMINAR = new Color(220, 38, 38);   // Rojo para eliminar

    JLabel lblTitulo;

    JLabel lblNombre, lblCiudad, lblEntrenador, lblFundacion, lblEstadio, lblPresupuesto, lblCategoria;
    JTextField txtId, txtNombre, txtCiudad, txtEntrenador, txtFundacion, txtEstadio, txtPresupuesto, txtCategoria;

    JButton btnGuardar, btnActualizar, btnEliminar, btnMostrar, btnRegistrarJugador;

    JTable tablaEquipos, tablaAuditoria;
    JScrollPane scroll1, scroll2;
    JPanel panelDatos, panelBotones;

    public Equipos() {
        setTitle("Fútbol Pro - Gestión de Equipos");
        setSize(1280, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        iniciarComponentes();

        mostrarTabla();
        mostrarAuditoria();
    }

    public void iniciarComponentes() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(FONDO_PRINCIPAL);

        // --- ENCABEZADO ---
        lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE EQUIPOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(TEXTO_BLANCO);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // --- ETIQUETAS Y CAMPOS ---
        lblNombre = crearEtiqueta("Nombre del Equipo");
        lblCiudad = crearEtiqueta("Ciudad Sede");
        lblEntrenador = crearEtiqueta("Director Técnico");
        lblFundacion = crearEtiqueta("Año de Fundación");
        lblEstadio = crearEtiqueta("Estadio");
        lblPresupuesto = crearEtiqueta("Presupuesto ($)");
        lblCategoria = crearEtiqueta("Categoría");

        txtId = new JTextField(); // Oculto o usado en segundo plano

        txtNombre = crearCampoTexto();
        txtCiudad = crearCampoTexto();
        txtEntrenador = crearCampoTexto();
        txtFundacion = crearCampoTexto();
        txtEstadio = crearCampoTexto();
        txtPresupuesto = crearCampoTexto();
        txtCategoria = crearCampoTexto();

        // --- BOTONES ESTILIZADOS ---
        btnGuardar = crearBotón("Guardar Equipo", VERDE_EXITO);
        btnActualizar = crearBotón("Actualizar", AZUL_ACCENT);
        btnEliminar = crearBotón("Eliminar", ROJO_ELIMINAR);
        btnMostrar = crearBotón("Refrescar", FONDO_INPUTS);
        btnRegistrarJugador = crearBotón("➕ Registrar Jugador", new Color(124, 58, 237)); // Morado eléctrico

        // --- CONTENEDORES ---
        panelDatos = new JPanel(new GridLayout(7, 2, 10, 15));
        panelDatos.setBackground(FONDO_PANELES);
        panelDatos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelDatos.add(lblNombre); panelDatos.add(txtNombre);
        panelDatos.add(lblCiudad); panelDatos.add(txtCiudad);
        panelDatos.add(lblEntrenador); panelDatos.add(txtEntrenador);
        panelDatos.add(lblFundacion); panelDatos.add(txtFundacion);
        panelDatos.add(lblEstadio); panelDatos.add(txtEstadio);
        panelDatos.add(lblPresupuesto); panelDatos.add(txtPresupuesto);
        panelDatos.add(lblCategoria); panelDatos.add(txtCategoria);

        panelBotones = new JPanel(new GridLayout(5, 1, 0, 10)); // Botones en lista vertical para que luzca mejor
        panelBotones.setBackground(FONDO_PANELES);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnRegistrarJugador); // Resaltado abajo de guardar
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnMostrar);

        JPanel izquierda = new JPanel(new BorderLayout(10, 10));
        izquierda.setBackground(FONDO_PANELES);
        izquierda.setPreferredSize(new Dimension(350, 700));
        izquierda.setBorder(BorderFactory.createLineBorder(FONDO_INPUTS, 1));

        izquierda.add(panelDatos, BorderLayout.CENTER);
        izquierda.add(panelBotones, BorderLayout.SOUTH);

        // --- TABLAS ESTILIZADAS ---
        tablaEquipos = crearTablaPersonalizada();
        tablaAuditoria = crearTablaPersonalizada();

        scroll1 = configurarScroll(tablaEquipos, "EQUIPOS REGISTRADOS");
        scroll2 = configurarScroll(tablaAuditoria, "HISTORIAL DE AUDITORÍA");

        JPanel derecha = new JPanel(new GridLayout(2, 1, 0, 15));
        derecha.setBackground(FONDO_PRINCIPAL);
        derecha.add(scroll1);
        derecha.add(scroll2);

        // Agregar al Frame Principal con márgenes limpios
        JPanel contenedorMargen = new JPanel(new BorderLayout(20, 20));
        contenedorMargen.setBackground(FONDO_PRINCIPAL);
        contenedorMargen.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        contenedorMargen.add(izquierda, BorderLayout.WEST);
        contenedorMargen.add(derecha, BorderLayout.CENTER);

        add(lblTitulo, BorderLayout.NORTH);
        add(contenedorMargen, BorderLayout.CENTER);

        // --- EVENTOS DE LOS BOTONES (Misma lógica exacta que tenías) ---
        btnGuardar.addActionListener(e -> {
            try {
                Conexion con = new Conexion();
                Connection cn = Conexion.conectar();
                String sql = "INSERT INTO equipos(nombre, ciudad, entrenador, fundacion, estadio, presupuesto, categoria) VALUES(?,?,?,?,?,?,?)";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, txtNombre.getText());
                ps.setString(2, txtCiudad.getText());
                ps.setString(3, txtEntrenador.getText());
                ps.setString(4, txtFundacion.getText());
                ps.setString(5, txtEstadio.getText());
                ps.setLong(6, Long.parseLong(txtPresupuesto.getText()));
                ps.setString(7, txtCategoria.getText());

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "¡Equipo guardado con éxito!");
                limpiarCampos();
                mostrarTabla();
                mostrarAuditoria();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage());
            }
        });

        btnMostrar.addActionListener(e -> {
            mostrarTabla();
            mostrarAuditoria();
        });

        btnEliminar.addActionListener(e -> {
            try {
                Conexion con = new Conexion();
                Connection cn = Conexion.conectar();
                int fila = tablaEquipos.getSelectedRow();
                if (fila == -1) { JOptionPane.showMessageDialog(null, "Selecciona un equipo para eliminar."); return; }
                int id = Integer.parseInt(tablaEquipos.getValueAt(fila, 0).toString());

                String sql = "DELETE FROM equipos WHERE id_equipo=?";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Equipo eliminado correctamente.");
                mostrarTabla();
                mostrarAuditoria();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al eliminar: " + ex.getMessage());
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                Conexion con = new Conexion();
                Connection cn = Conexion.conectar();
                int fila = tablaEquipos.getSelectedRow();
                if (fila == -1) { JOptionPane.showMessageDialog(null, "Selecciona un equipo para actualizar."); return; }
                int id = Integer.parseInt(tablaEquipos.getValueAt(fila, 0).toString());

                String sql = "UPDATE equipos SET nombre=?, ciudad=?, entrenador=? WHERE id_equipo=?";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, txtNombre.getText());
                ps.setString(2, txtCiudad.getText());
                ps.setString(3, txtEntrenador.getText());
                ps.setInt(4, id);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Equipo actualizado correctamente.");
                mostrarTabla();
                mostrarAuditoria();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar: " + ex.getMessage());
            }
        });

        btnRegistrarJugador.addActionListener(e -> {
            int fila = tablaEquipos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Por favor, selecciona un equipo de la tabla primero.");
                return;
            }
            int idEquipoSeleccionado = Integer.parseInt(tablaEquipos.getValueAt(fila, 0).toString());
            String nombreEquipo = tablaEquipos.getValueAt(fila, 1).toString();
            abrirVentanaJugador(idEquipoSeleccionado, nombreEquipo);
        });

        tablaEquipos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tablaEquipos.getSelectedRow();
                txtId.setText(tablaEquipos.getValueAt(fila, 0).toString());
                txtNombre.setText(tablaEquipos.getValueAt(fila, 1).toString());
                txtCiudad.setText(tablaEquipos.getValueAt(fila, 2).toString());
                txtEntrenador.setText(tablaEquipos.getValueAt(fila, 3).toString());
            }
        });
    }

    // --- INTERFAZ EMERGENTE REGISTRAR JUGADOR (MUY VISTOSA) ---
    private void abrirVentanaJugador(int idEquipo, String nombreEquipo) {
        JDialog ventanaAlta = new JDialog(this, "Fútbol Pro - Fichar para " + nombreEquipo, true);
        ventanaAlta.setSize(440, 580);
        ventanaAlta.setLocationRelativeTo(this);
        ventanaAlta.setLayout(new BorderLayout());
        ventanaAlta.getContentPane().setBackground(FONDO_PRINCIPAL);

        JLabel lblSub = new JLabel("NUEVO FICHAJE", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSub.setForeground(TEXTO_BLANCO);
        lblSub.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JPanel pnlCampos = new JPanel(new GridLayout(9, 2, 10, 15));
        pnlCampos.setBackground(FONDO_PANELES);
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField jTxtNombre = crearCampoTexto();
        JTextField jTxtApellido = crearCampoTexto();
        JTextField jTxtEdad = crearCampoTexto();
        JTextField jTxtPosicion = crearCampoTexto();
        JTextField jTxtNacionalidad = crearCampoTexto();
        JTextField jTxtSalario = crearCampoTexto();
        JTextField jTxtFechaNac = crearCampoTexto();
        JTextField jTxtDorsal = crearCampoTexto();
        
        JLabel jLblIdEquipo = new JLabel(idEquipo + " (" + nombreEquipo + ")");
        jLblIdEquipo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLblIdEquipo.setForeground(new Color(124, 58, 237));

        pnlCampos.add(crearEtiqueta("Nombre:")); pnlCampos.add(jTxtNombre);
        pnlCampos.add(crearEtiqueta("Apellido:")); pnlCampos.add(jTxtApellido);
        pnlCampos.add(crearEtiqueta("Edad:")); pnlCampos.add(jTxtEdad);
        pnlCampos.add(crearEtiqueta("Posición (ej: Delantero):")); pnlCampos.add(jTxtPosicion);
        pnlCampos.add(crearEtiqueta("Nacionalidad:")); pnlCampos.add(jTxtNacionalidad);
        pnlCampos.add(crearEtiqueta("Salario Anual ($):")); pnlCampos.add(jTxtSalario);
        pnlCampos.add(crearEtiqueta("Fecha Nac (AAAA-MM-DD):")); pnlCampos.add(jTxtFechaNac);
        pnlCampos.add(crearEtiqueta("Dorsal (Número):")); pnlCampos.add(jTxtDorsal);
        pnlCampos.add(crearEtiqueta("Equipo Destino:")); pnlCampos.add(jLblIdEquipo);

        JButton jBtnGuardar = crearBotón("Confirmar Contratación", VERDE_EXITO);
        jBtnGuardar.setPreferredSize(new Dimension(440, 50));

        jBtnGuardar.addActionListener(ev -> {
            try {
                Conexion con = new Conexion();
                Connection cn = Conexion.conectar();
                String sql = "INSERT INTO jugadores (nombre, apellido, edad, posicion, nacionalidad, salario, fecha_nacimiento, dorsal, id_equipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = cn.prepareStatement(sql);

                ps.setString(1, jTxtNombre.getText());
                ps.setString(2, jTxtApellido.getText());
                ps.setInt(3, Integer.parseInt(jTxtEdad.getText()));
                ps.setString(4, jTxtPosicion.getText());
                ps.setString(5, jTxtNacionalidad.getText());
                ps.setDouble(6, Double.parseDouble(jTxtSalario.getText()));
                ps.setString(7, jTxtFechaNac.getText());
                ps.setString(8, jTxtDorsal.getText());
                ps.setInt(9, idEquipo);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(ventanaAlta, "¡Jugador registrado correctamente en el club!");
                ventanaAlta.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ventanaAlta, "Error en los datos: " + ex.getMessage());
            }
        });

        ventanaAlta.add(lblSub, BorderLayout.NORTH);
        ventanaAlta.add(pnlCampos, BorderLayout.CENTER);
        ventanaAlta.add(jBtnGuardar, BorderLayout.SOUTH);
        ventanaAlta.setVisible(true);
    }

    // --- MÉTODOS AUXILIARES DE DISEÑO UI/UX ---
    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXTO_GRIS);
        return label;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setBackground(FONDO_INPUTS);
        campo.setForeground(TEXTO_BLANCO);
        campo.setCaretColor(TEXTO_BLANCO);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(82, 82, 91), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return campo;
    }

    private JButton crearBotón(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(colorFondo);
        boton.setForeground(TEXTO_BLANCO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return boton;
    }

    private JTable crearTablaPersonalizada() {
        JTable tabla = new JTable();
        tabla.setBackground(FONDO_PANELES);
        tabla.setForeground(TEXTO_BLANCO);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(25);
        tabla.setGridColor(FONDO_INPUTS);
        tabla.setSelectionBackground(AZUL_ACCENT);
        tabla.setSelectionForeground(TEXTO_BLANCO);
        
        // Estilo del encabezado de la tabla
        tabla.getTableHeader().setBackground(FONDO_INPUTS);
        tabla.getTableHeader().setForeground(TEXTO_BLANCO);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        return tabla;
    }

    private JScrollPane configurarScroll(JTable tabla, String tituloSeccion) {
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBackground(FONDO_PRINCIPAL);
        scroll.getViewport().setBackground(FONDO_PRINCIPAL);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(FONDO_INPUTS, 1), 
                tituloSeccion, 
                0, 0, 
                new Font("Segoe UI", Font.BOLD, 14), 
                TEXTO_BLANCO
        ));
        return scroll;
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCiudad.setText("");
        txtEntrenador.setText("");
        txtFundacion.setText("");
        txtEstadio.setText("");
        txtPresupuesto.setText("");
        txtCategoria.setText("");
    }

    // --- CONSULTAS ORIGINALES BASE DE DATOS ---
    public void mostrarTabla() {
        try {
            Conexion con = new Conexion();
            Connection cn = Conexion.conectar();
            DefaultTableModel modelo = new DefaultTableModel() {
                @Override public boolean isCellEditable(int r, int c) { return false; } // Celdas no editables directo
            };

            modelo.addColumn("ID Equipo");
            modelo.addColumn("Nombre Club");
            modelo.addColumn("Ciudad");
            modelo.addColumn("Entrenador (DT)");

            String sql = "SELECT * FROM equipos";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()){
                Object fila[] = new Object[4];
                fila[0] = rs.getInt("id_equipo");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("ciudad");
                fila[3] = rs.getString("entrenador");
                modelo.addRow(fila);
            }
            tablaEquipos.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar tabla: " + e);
        }
    }

    public void mostrarAuditoria() {
        try {
            Conexion con = new Conexion();
            Connection cn = con.conectar();
            DefaultTableModel modelo = new DefaultTableModel() {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            modelo.addColumn("Acción");
            modelo.addColumn("Usuario");
            modelo.addColumn("Fecha Registro");
            modelo.addColumn("Modificación");

            String sql = "SELECT * FROM audi_equipos";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()){
                Object fila[] = new Object[4];
                fila[0] = rs.getString("accion");
                fila[1] = rs.getString("usuario");
                fila[2] = rs.getString("modificado");
                fila[3] = rs.getString("nomnue");
                modelo.addRow(fila);
            }
            tablaAuditoria.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error auditoría: " + e);
        }
    }

    public static void main(String[] args) {
        new Equipos().setVisible(true);
    }
}