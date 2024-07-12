//IMPORTS NEEDED FOR PROGRAM, DO NOT DELETE
import java.io.*;
import java.util.ArrayList;
import java.net.Socket;

public class ClientHandler implements Runnable {
    // Makes an array of all the clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
//  Default classes needed for this handler.
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;


    public ClientHandler(Socket socket) {
        try {
//           If someone connects, create more ClientHandlers (as each ClientHandler is for each individual user.)
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
//          Then, broadcast publicly who joined.
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat.");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messagefromClient;

        while (socket.isConnected()) {
            try {
//              Reads message from client
                messagefromClient = bufferedReader.readLine();
                broadcastMessage(messagefromClient);
            } catch (IOException e) {
//              If an error occurs, force close everything in this order.
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
//              If someone presses enter on a message, it sends it.
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
//              If an error/exception occurs, close everything in this exact order. (Server, reader, writer)
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    public void removeClientHandler() {
//      If someone leaves, broadcast who left as a SERVER message.
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat.");
    }
    // Defines what "closeEverything" does in code.
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
//          If an error occurs, it prints the error trace/error log to the console.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
