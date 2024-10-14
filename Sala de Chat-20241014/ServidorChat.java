import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

/**
 *
 * @author RPVZ
 */
public class ServidorChat {
    private DefaultListModel mensajes = new DefaultListModel();

    public static void main(String[] args) {
        new ServidorChat();
    }

    public ServidorChat() {
        try {
            ServerSocket socketServidor = new ServerSocket(5000);
            while (true) {
                Socket cliente = socketServidor.accept();
                DataInputStream input = new DataInputStream(cliente.getInputStream());
                String grupo = input.readUTF(); // Leer el grupo del cliente
                Runnable nuevoCliente = new HiloDeCliente(mensajes, cliente, grupo); // Pasar el grupo al cliente
                Thread hilo = new Thread(nuevoCliente);
                hilo.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}