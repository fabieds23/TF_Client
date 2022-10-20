package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Label clienteLabel;
    @FXML
    private Label esperaLabelCliente;
    @FXML
    private Button botonClient;
    @FXML
    private TextField textClient;

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try{
            client = new Client(new Socket("localhost", 1234));
            System.out.println("Conectado al servidor");
        }
        catch (IOException e){
            System.out.println("Error");
        }
        client.receiveMessageFromServer(esperaLabelCliente);
        botonClient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String clientMessage = textClient.getText();
                client.sendMessagetoServer(clientMessage);
            }
        });

    }
    public static void labelUpdate(String messageFromServer, Label temp2) {
        temp2.setText(messageFromServer);
    }

}
