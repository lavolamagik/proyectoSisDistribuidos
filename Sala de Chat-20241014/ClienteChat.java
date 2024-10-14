import javax.swing.*;
import java.net.Socket;

public class ClienteChat {
    private Socket socket;
    private PanelCliente panel;
    public int numClients;

    public static void main(String[] args) {
        new ClienteChat();
    }

    public ClienteChat() {
        try {
            // Preguntar cuántos clientes crear
            String input = JOptionPane.showInputDialog("¿Cuántos clientes deseas crear?");
            numClients = Integer.parseInt(input); // Convertir la entrada a un número entero

            // Crear los clientes
            for (int i = 0; i < numClients; i++) {
                crearCliente(); // Método para crear cada cliente
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crearCliente() {
        try {
            creaYVisualizaVentana();
            socket = new Socket("localhost", 5000);
            new ControlCliente(socket, panel, numClients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void creaYVisualizaVentana() {
        JFrame v = new JFrame();
        panel = new PanelCliente(v.getContentPane());
        v.pack();
        v.setVisible(true);
        v.setSize(600, 300);
        v.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
