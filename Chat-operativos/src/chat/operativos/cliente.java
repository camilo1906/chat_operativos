package chat.operativos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author K-milo
 */
public class cliente {

    //atributos
    JFrame ventana = null;
    JButton enviar = null;
    JTextField txt_msj = null;
    JTextArea area_chat = null;
    JPanel contenedor_areachat = null;
    JPanel contenedor_btntxt = null;
    JScrollPane scroll = null;
    Socket socket = null;
    BufferedReader lector = null;
    PrintWriter escritor = null;

    //contructor
    public cliente() {
        crearInterfaz();
    }

    public void crearInterfaz() {
        ventana = new JFrame(" CLIENTE ");
        enviar = new JButton(" ENVIAR ");
        txt_msj = new JTextField(5);
        area_chat = new JTextArea(10, 12);
        scroll = new JScrollPane(area_chat);
        contenedor_areachat = new JPanel();
        contenedor_areachat.setLayout(new GridLayout(1, 1));
        contenedor_areachat.add(scroll);
        contenedor_btntxt = new JPanel();
        contenedor_btntxt.setLayout(new GridLayout(1, 2));
        contenedor_btntxt.add(txt_msj);
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
                    socket = new Socket("localhost",8080);
                    leer();
                    escribir();
                } catch (Exception ex) {
                    ex.printStackTrace();;
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
                        area_chat.append(" Servidor dice: " + mensaje_recibido + "\n");
                        
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
                            String enviar_mensaje = txt_msj.getText();
                            escritor.println(enviar_mensaje);
                            area_chat.append(" Cliente dice: " + enviar_mensaje + "\n");
                            txt_msj.setText("");
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
        new cliente();

    }//termina main
}//termina clase cliente
