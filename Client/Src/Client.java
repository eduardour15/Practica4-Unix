package Client.Src;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Client {
    /**
     * @param args
     */
    public static void main(String[] args) {
        final File[] _archivoEnviar = new File[1];
        // Definimos lo que sería el marco sobre el cual se trabajará
        JFrame marco = new JFrame("Practica de Java-Cliente");
        marco.setSize(400, 450);
        marco.setLayout(new BoxLayout(marco.getContentPane(), BoxLayout.Y_AXIS));
        marco.setDefaultCloseOperation(marco.EXIT_ON_CLOSE);
        /**
         * En esta parte se define lo que sería la propiedad Titulo
         * que irá sobre el marco
         */
        JLabel titulo = new JLabel("Enviador de archivos con Java");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        titulo.setBorder(new EmptyBorder(20, 0, 0, 0));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        /** Definimos la propiedad del nombre del Archivo */
        JLabel nombreArchivo = new JLabel("Escoge el archivo a enviar");
        nombreArchivo.setFont(new Font("Arial", Font.BOLD, 25));
        nombreArchivo.setBorder(new EmptyBorder(20, 0, 0, 0));
        nombreArchivo.setAlignmentX(Component.CENTER_ALIGNMENT);
        /* Definimos el contenedor o container para los botones */
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(75, 0, 10, 0));
        /* Creación del botón de enviar archivo */
        JButton btEnviaArchivo = new JButton("Envia archivo");
        btEnviaArchivo.setPreferredSize(new Dimension(150, 75));
        btEnviaArchivo.setFont(new Font("Arial", Font.BOLD, 20));
        /* Creación del botón de escoger archivo */
        JButton btEscogeArchivo = new JButton("Escoger archivo");
        btEscogeArchivo.setPreferredSize(new Dimension(150, 75));
        btEscogeArchivo.setFont(new Font("Arial", Font.BOLD, 20));
        /* Lo añanimos sobre el panel */
        panel.add(btEnviaArchivo);
        panel.add(btEscogeArchivo);
        /* Definición de la acción que tomará el botón btEscogeArchivo */
        btEscogeArchivo.addActionListener(new ActionListener() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             * En esta parte es donde se escogerá el archivo que se enviará luego,
             * lo que se seleccione se almacenará en la variable _archivoEnviar.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Escoge un archivo para enviar");
                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    _archivoEnviar[0] = jFileChooser.getSelectedFile();
                    nombreArchivo.setText("El archivo que desea enviar es: " + _archivoEnviar[0].getName());
                }
            }
        });
        /* Definición de la acción que tomará el archivo btEnviaArchivo */
        btEnviaArchivo.addActionListener(new ActionListener() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             * En esta parte se definirá lo que es el socket y así mismo verificará que
             * el archivo no esté vacío
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Verificamos que el archivo no esté vacío de lo contrario se
                 * mostrará el mensaje en pantalla
                 */
                if (_archivoEnviar[0] == null) {
                    nombreArchivo.setText("Por favor escoja un archivo primero");
                } else {
                    try {
                        /*Realizamos la lectura del archivo seleccionado */
                        FileInputStream archivoRecibido = new FileInputStream(_archivoEnviar[0].getAbsolutePath());
                        /*Inicializamos el socket desde el puerto 5003  */
                        Socket socket = new Socket("localhost", 5003);
                        /*Permitiremos escribir los datos en un archivo de salida */
                        DataOutputStream archivoSalida = new DataOutputStream(socket.getOutputStream());
                        /*Obtenemos el nombre del archivo a enviar y su tamaño en bytes */
                        String auxNombreArchivo = _archivoEnviar[0].getName();
                        byte[] auxNombreArchivoByte = auxNombreArchivo.getBytes();

                        byte[] contenidoArchivo = new byte[(int) _archivoEnviar[0].length()];
                        archivoRecibido.read(contenidoArchivo);

                        archivoSalida.writeInt(auxNombreArchivoByte.length);
                        archivoSalida.write(auxNombreArchivoByte);

                        archivoSalida.writeInt(contenidoArchivo.length);
                        archivoSalida.write(contenidoArchivo);

                    } catch (IOException err) {
                        err.printStackTrace();
                    }

                }

            }
        });
        marco.add(titulo);
        marco.add(nombreArchivo);
        marco.add(panel);
        marco.setVisible(true);
    }

}
