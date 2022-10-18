package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.text.TabableView;
import java.io.File;
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
    private Button botonAddFiles;
    @FXML
    private Button botonAddCarpetoide;
    @FXML
    private TextField textClient;
    @FXML
    private TableView<Names> tableArchivos;
    @FXML
    private TableColumn <Names, String> columArchivos;
    private Client client;
    private String path;
    private File archivo;
    private File directorio;
    private File[] archivos;

    FileChooser seleccionador = new FileChooser();
    private ObservableList<Names> nombresDisp;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        nombresDisp = FXCollections.observableArrayList();
        this.columArchivos.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        try{
            client = new Client(new Socket("localhost", 1234));
            System.out.println("Conectado al servidor");
        }
        catch (IOException e){
            System.out.println("Error conectando al servidor");
        }
        client.receiveMessageFromServer(esperaLabelCliente);
        botonClient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String clientMessage = textClient.getText();
                client.sendMessagetoServer(clientMessage);
            }
        });
        botonAddFiles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                archivo = seleccionador.showOpenDialog(new Stage());
                if (archivo.getPath().substring(archivo.getPath().length() - 4).equals(".pdf") ||
                        archivo.getPath().substring(archivo.getPath().length() - 4).equals(".txt") ||
                        archivo.getPath().substring(archivo.getPath().length() - 4).equals("docx")){
                    nombresDisp.add(new Names(archivo.getName()));
                    client.sendMessagetoServer(archivo.getPath() + "," + archivo.getName() + "," + archivo.getPath().substring(archivo.getPath().length()-4));
                }
                else{
                    System.out.println("Tipo de documento incorrecto");
                }
            }
        });
        botonAddCarpetoide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                archivo = seleccionador.showOpenDialog(new Stage());
                directorio = new File(archivo.getAbsoluteFile().getParent());
                archivos = directorio.listFiles();
                if (archivos != null){
                    for(File file : archivos){
                        if (file.getPath().substring(file.getPath().length() - 4).equals(".pdf") ||
                                file.getPath().substring(file.getPath().length() - 4).equals(".txt") ||
                                file.getPath().substring(file.getPath().length() - 4).equals("docx")){
                            nombresDisp.add(new Names(file.getName()));
                            client.sendMessagetoServer(file.getPath() + "," + file.getName() + "," + file.getPath().substring(file.getPath().length()-4));
                        }
                        else{
                            System.out.println("Tipo de documento incorrecto");
                        }
                    }
                }
            }
        });
        this.tableArchivos.setItems(nombresDisp);
    }
}