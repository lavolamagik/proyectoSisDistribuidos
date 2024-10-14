import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author RPVZ
 */
public class ControlCliente implements ActionListener, Runnable {
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;
    private PanelCliente panel;

    public ControlCliente(Socket socket, PanelCliente panel, int numClients) {
        this.panel = panel;
        try {
            dataInput = new DataInputStream(socket.getInputStream());
            dataOutput = new DataOutputStream(socket.getOutputStream());
            panel.addActionListener(this);
            Thread hilo = new Thread(this);
            hilo.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        try {
            String message = panel.getTexto();
            String formattedMessage = message;
            dataOutput.writeUTF(formattedMessage);
        } catch (Exception excepcion) {
            excepcion.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String texto = dataInput.readUTF();
                panel.addTexto(texto);
                panel.addTexto("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
