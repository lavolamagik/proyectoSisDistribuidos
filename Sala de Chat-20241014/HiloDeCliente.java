import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author RPVZ
 */
public class HiloDeCliente implements Runnable, ListDataListener {
    private static int contador = 0;
    private int id;
    private DefaultListModel mensajes;
    private Socket socket;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;
    private static ArrayList<HiloDeCliente> clientes = new ArrayList<>();
    private String grupo;

    public HiloDeCliente(DefaultListModel mensajes, Socket socket, String grupo) {
        this.mensajes = mensajes;
        this.socket = socket;
        this.grupo = grupo;
        id = contador++;
        clientes.add(this);
        try {
            dataInput = new DataInputStream(socket.getInputStream());
            dataOutput = new DataOutputStream(socket.getOutputStream());
            mensajes.addListDataListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String texto = dataInput.readUTF();
                System.out.println(texto);
                if (texto.startsWith("@")) {
                    // Lógica para mensajes privados
                    String[] parts = texto.split(":", 2);
                    String recipientId = parts[0].substring(1); // Obtener el ID del destinatario
                    String messageContent = parts[1]; // Obtener el contenido del mensaje
                    // Enviar el mensaje al cliente especificado
                    for (HiloDeCliente cliente : clientes) {
                        if (String.valueOf(cliente.id).equals(recipientId)) {
                            cliente.dataOutput.writeUTF("Privado de Cliente " + id + ": " + messageContent);
                            break;
                        }
                        if (String.valueOf(cliente.id).equals(String.valueOf(id))) {
                            cliente.dataOutput.writeUTF("Privado a Cliente " + recipientId + "-de cliente " + id + ": "
                                    + messageContent);
                        }
                    }
                } else if (texto.startsWith("#")) {
                    String mensajeGrupo = "Grupo " + grupo + "- Cliente " + id + ": " + texto.substring(1);
                    for (HiloDeCliente cliente : clientes) {
                        if (cliente.grupo.equals(this.grupo)) {
                            try {
                                cliente.dataOutput.writeUTF(mensajeGrupo);
                            } catch (IOException e) {
                                System.out.println("Error enviando mensaje de grupo: " + e.getMessage());
                            }
                        }
                    }

                } else {
                    // Lógica para mensajes generales
                    String mensajeId = "Cliente " + id + ": " + texto;
                    synchronized (mensajes) {
                        mensajes.addElement(mensajeId);
                        System.out.println(mensajeId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        String texto = (String) mensajes.getElementAt(e.getIndex0());
        try {
            dataOutput.writeUTF(texto);
        } catch (Exception excepcion) {
            excepcion.printStackTrace();
        }
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated
        // from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated
        // from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
