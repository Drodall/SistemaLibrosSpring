package co.com.zonatelematica.tienda_libros.vista;

import co.com.zonatelematica.tienda_libros.modelo.Libro;
import co.com.zonatelematica.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@Component
public class LibroForm extends JFrame {
    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField idTexto;
    private JTextField libroTexto;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField existenciasTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tablaModeloLibros;

    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarFormulario();
        agregarButton.addActionListener(e -> agegarLibro());
        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
        modificarButton.addActionListener(e ->modificarLibro());

        eliminarButton.addActionListener(e ->eliminarLibro());




    }

    private void iniciarFormulario(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900 , 700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension tamanioPantalla = toolkit.getScreenSize();
        int x = (tamanioPantalla.width - getWidth() / 2);
        int y = (tamanioPantalla.height - getHeight() / 2);
        setLocation(x , y);

    }

    private void agegarLibro(){
        // Leer los valores del formulario
        if(libroTexto.getText().equals("")){
            mostrarMensaje("Ingrese el nombre del libro");
            libroTexto.requestFocusInWindow();
            return;
        }
        var nombreLibro = libroTexto.getText();
        var autor = autorTexto.getText();
        var precio = precioTexto.getText();
        var existencias = Integer.parseInt(existenciasTexto.getText());
        // Crear el libro
        var libro = new Libro();
        libro.setNombreLibro(nombreLibro);
        libro.setAutor(autor);
        libro.setPrecio(precio);
        libro.setExistencias(existencias);
        this.libroServicio.guardarLibro(libro);
        mostrarMensaje("El libro se agrega exitosamente...");
        limpiarFormulario();
        listarLibros();

    }

    private void cargarLibroSeleccionado(){
        // Desde 0 comienzan los indices de la tabla
        var renglon = tablaLibros.getSelectedRow();
        if(renglon != -1){ //Regresa -1 si no se selecciona ningun registro
            String idLibro =
                    tablaLibros.getModel().getValueAt(renglon , 0).toString();
            idTexto.setText(idLibro);
            String nombreLibro =
                    tablaLibros.getModel().getValueAt(renglon , 1).toString();
           libroTexto.setText(nombreLibro);
           String autor =
                   tablaLibros.getModel().getValueAt(renglon , 2).toString();
           autorTexto.setText(autor);
           String precio =
                   tablaLibros.getModel().getValueAt(renglon , 3).toString();
           precioTexto.setText(precio);
           String existencias =
                   tablaLibros.getModel().getValueAt(renglon , 4).toString();
           existenciasTexto.setText(existencias);
        }
    }

    private void modificarLibro(){
        if(this.idTexto.getText().equals("")){
            mostrarMensaje("Debe seleccionar un registro..");

        }
        else {
                // Verificamos que el nombre del libro no sea nulo
            if(libroTexto.getText().equals("")){
                mostrarMensaje("Ingrese el nombre del libro...");
                libroTexto.requestFocusInWindow();
                return;
            }
            // Se actualiza el objeto libro
            int idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = libroTexto.getText();
            var autor = autorTexto.getText();
            var precio = precioTexto.getText();
            var existencias = Integer.parseInt(existenciasTexto.getText());
            var libro =
                    new Libro(idLibro , nombreLibro , autor , precio , existencias);
            libroServicio.guardarLibro(libro);
            mostrarMensaje("El libro se modifica exitosamente...");
            limpiarFormulario();
            listarLibros();
        }


    }

    private void eliminarLibro(){
        var renglon = tablaLibros.getSelectedRow();
        if(renglon != -1){
            String idLibro =
                    tablaLibros.getModel().getValueAt(renglon , 0).toString();
           var libro = new Libro();
           libro.setIdLibro(Integer.parseInt(idLibro));
           libroServicio.eliminarLibro(libro);
           mostrarMensaje("Libro " + idLibro + " eliminado correctamente...");
           limpiarFormulario();
           listarLibros();
        }
        else{
            mostrarMensaje("No se ha seleccionado el libro a eliminar...");
        }
    }

    private void limpiarFormulario(){
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        existenciasTexto.setText("");
    }

    private  void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this , mensaje);
    }



    private void createUIComponents() {
        // TODO: place custom component creation code here
        // Se crea el elemento idTexto oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);

        this.tablaModeloLibros = new DefaultTableModel(0 , 5){
            @Override
            public  boolean isCellEditable(int row , int column){ return false;}

        };
        String[] cabeceros = {"Id" , "Libro" , "Autor" , "Precio" , "Existencias"};
        tablaModeloLibros.setColumnIdentifiers(cabeceros);
        // Instanciar el objeto JTable
        this.tablaLibros = new JTable(tablaModeloLibros);
        // Para no seleccionar varios registros de la tabla
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listarLibros();


    }

    private void listarLibros(){
        // Limpiar la tabla
        tablaModeloLibros.setRowCount(0);
        //Obtener los libros
        var libros = libroServicio.listarLibros();
        libros.forEach((libro)->{
            Object[] renglonLibro = {
                    libro.getIdLibro(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getExistencias()
            };
            this.tablaModeloLibros.addRow(renglonLibro);
        });

    }
}
