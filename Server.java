// IMPORTS NEEDED FOR PROGRAM, DO NOT DELETE
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {

    try {
        while (!serverSocket.isClosed()) {
            // Accepts the user into your server if your server isn't closed.
            Socket socket = serverSocket.accept();
            // If a new user connects to your server, it says it (only the server owner can see this.)
            System.out.println("New connection from " + socket.getRemoteSocketAddress());
            ClientHandler clientHandler = new ClientHandler(socket);

            Thread thread = new Thread(clientHandler);
            thread.start();
        }
    } catch (IOException e) {

    }

    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // Opens your server on your IP Address on port 1234 (you can change this,
        // just tell your friends to edit Client.java
        // to change their port setting to the port you changed it to.
        ServerSocket serverSocket = new ServerSocket(1234);
        // Initialises server
        Server server = new Server(serverSocket);
        // Starts server
        server.startServer();
    }
}
