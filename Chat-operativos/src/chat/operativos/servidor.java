package chat.operativos;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;

/**
 *
 * @author K-milo
 */
public class servidor {

    //atributos
    JFrame ventana = null;
    JButton enviar = null;
    JTextField txt_mensaje = null;
    JTextArea area_chat = null;
    JPanel contenedor_areachat = null;
    JPanel contenedor_btntxt = null;
    JScrollPane scroll = null;
    ServerSocket servidor = null;
    Socket socket = null;
    BufferedReader lector = null;
    PrintWriter escritor = null;

    //constructor
    public servidor() {
        crearInterfaz();
    }

    public void crearInterfaz() {
        ventana = new JFrame(" SERVIDOR ");
        enviar = new JButton(" ENVIAR ");
        txt_mensaje = new JTextField(5);
        area_chat = new JTextArea(10, 12);
        scroll = new JScrollPane(area_chat);
        contenedor_areachat = new JPanel();
        contenedor_areachat.setLayout(new GridLayout(1, 1));
        contenedor_areachat.add(scroll);
        contenedor_btntxt = new JPanel();
        contenedor_btntxt.setLayout(new GridLayout(1, 2));
        contenedor_btntxt.add(txt_mensaje);
        contenedor_btntxt.add(enviar);
        ventana.setLayout(new BorderLayout());
        ventana.add(contenedor_areachat, BorderLayout.NORTH);
        ventana.add(contenedor_btntxt, BorderLayout.SOUTH);
        ventana.setSize(300, 220);
        ventana.setVisible(true);
        ventana.setResizable(false);
        area_chat.enable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Thread principal = new Thread(new Runnable() {
            public void run() {
                try {
                    servidor = new ServerSocket(8080);
                    while (true) {
                        socket = servidor.accept();
                        leer();
                        escribir();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        principal.start();
    }

    public void leer() {
        Thread leer_hilo = new Thread(new Runnable() {
            public void run() {
                try {
                    lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        String mensaje_recibido = lector.readLine();
                        area_chat.append(" Cliente dice: " + mensaje_recibido + "\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        leer_hilo.start();
    }

    public void escribir() {
        Thread escribir_hilo = new Thread(new Runnable() {
            public void run() {
                try {
                    escritor = new PrintWriter(socket.getOutputStream(),true);
                    enviar.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String enviar_mensaje = txt_mensaje.getText();
                            escritor.println(enviar_mensaje);
                            area_chat.append(" Servidor dice: " + enviar_mensaje + "\n");
                            txt_mensaje.setText("");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        escribir_hilo.start();
    }

    public static void main(String[] args) {
        new servidor();
    }//termina main
}//termina clase servidor

