package Server.src;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicSliderUI.ComponentHandler;

import Server.src.Myfile;

public class Server {
    static ArrayList<Myfile> misArchivos = new ArrayList<Myfile>();

    public static void main(String[] args) throws IOException {
        int idArchivo = 0;
        JFrame marco = new JFrame("Practica de Java-Server");
        marco.setSize(400, 400);
        marco.setLayout(new BoxLayout(marco.getContentPane(), BoxLayout.Y_AXIS));
        marco.setDefaultCloseOperation(marco.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel titulo = new JLabel("Recibidor de archivos");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        titulo.setBorder(new EmptyBorder(20, 0, 10, 0));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        marco.add(titulo);
        marco.add(scroll);
        marco.setVisible(true);
        /*Espera por la conexion en el puerto 5003 */
        ServerSocket socketServidor = new ServerSocket(5003);

        while (true) {
            try {
                /*Se espera que el cliente se una al socket */
                Socket socket = socketServidor.accept();
                /*Se obtiene la informacion proveniente del cliente */
                DataInputStream archivoRecibido = new DataInputStream(socket.getInputStream());

                int tamanioArchivo = archivoRecibido.readInt();
                /*Si los dados han sido enviado hara la siguiente accion */
                if (tamanioArchivo > 0) {
                    byte[] inforByte = new byte[tamanioArchivo];
                    archivoRecibido.readFully(inforByte, 0, inforByte.length);
                    String nombreArchivo = new String(inforByte);

                    int longitudFile = archivoRecibido.readInt();

                    if (longitudFile > 0) {
                        byte[] tamanioFile = new byte[longitudFile];
                        archivoRecibido.readFully(tamanioFile, 0, longitudFile);

                        JPanel columnaArchivo = new JPanel();
                        columnaArchivo.setLayout(new BoxLayout(columnaArchivo, BoxLayout.Y_AXIS));

                        JLabel tituloArchivo = new JLabel(nombreArchivo);
                        tituloArchivo.setFont(new Font("Arial", Font.BOLD, 20));
                        tituloArchivo.setBorder(new EmptyBorder(10, 0, 10, 0));

                        if (getFilextension(nombreArchivo).equalsIgnoreCase("txt")) {
                            columnaArchivo.setName(String.valueOf(idArchivo));
                            columnaArchivo.addMouseListener(getMyMouseListener());

                            columnaArchivo.add(tituloArchivo);
                            panel.add(columnaArchivo);
                            marco.validate();
                        } else {
                            columnaArchivo.setName(String.valueOf(idArchivo));
                            columnaArchivo.addMouseListener(getMyMouseListener());

                            columnaArchivo.add(tituloArchivo);
                            panel.add(columnaArchivo);

                            marco.validate();
                        }
                        misArchivos.add(new Myfile(idArchivo, nombreArchivo, tamanioFile, getFilextension(nombreArchivo)));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static MouseInputListener getMyMouseListener() {
        return new MouseInputListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = (JPanel) e.getSource();
                int idArchivo = Integer.parseInt(panel.getName());
                for (Myfile valor : misArchivos) {
                    if (valor.getId() == idArchivo) {
                        JFrame mostrarMarco = crearMarco(valor.getName(), valor.getData(), valor.getFilextension());
                        mostrarMarco.setVisible(true);
                    }
                }

            }

            private JFrame crearMarco(String nombreArchivo, byte[] fileData, String fileExtension) {
                JFrame marco = new JFrame("Practica-Java Descargas");
                marco.setSize(400, 400);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                JLabel titulo = new JLabel("¿Desea descargar este archivo?");
                titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
                titulo.setFont(new Font("Arial", Font.BOLD, 25));
                titulo.setBorder(new EmptyBorder(20, 0, 10, 0));

                JLabel prompt = new JLabel();
                prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
                prompt.setFont(new Font("Arial", Font.BOLD, 25));
                prompt.setBorder(new EmptyBorder(20, 0, 10, 0));

                JButton btSi = new JButton("SI");
                btSi.setPreferredSize(new Dimension(150, 75));

                JButton btNo = new JButton("NO");
                btNo.setPreferredSize(new Dimension(150, 75));

                JLabel labelArchivo = new JLabel();
                labelArchivo.setAlignmentX(Component.CENTER_ALIGNMENT);

                JPanel panelBotones = new JPanel();
                panelBotones.setBorder(new EmptyBorder(20, 0, 10, 0));
                panelBotones.add(btSi);
                panelBotones.add(btNo);

                if (fileExtension.equalsIgnoreCase("Ext")) {
                    labelArchivo.setText("<html>" + new String(fileData) + "</html>");

                } else {
                    labelArchivo.setIcon(new ImageIcon(fileData));
                }

                btSi.addActionListener((ActionListener) new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File fileToDowload = new File(nombreArchivo);
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(fileToDowload);
                            fileOutputStream.write(fileData);
                            fileOutputStream.close();

                            marco.dispose();
                        } catch (Exception err) {
                            err.printStackTrace();
                        }

                    }
                });

                btNo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        marco.dispose();
                    }
                });

                panel.add(titulo);
                panel.add(prompt);
                panel.add(labelArchivo);
                panel.add(panelBotones);
                marco.add(panel);

                return marco;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub

            }

        };
    }

    public static String getFilextension(String nombreArchivo) {
        int i = nombreArchivo.lastIndexOf('.');
        if (i > 0) {
            return nombreArchivo.substring(i + 1);
        } else {
            return "NO se encontro esa extension";
        }
    }

}
