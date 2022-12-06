package com.example.chatclient;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //Inject widgets
    @FXML
    private Label labelBox;
    @FXML
    private ScrollPane pane;
    @FXML
    private Button buttonSend;
    @FXML
    private TextField field;
    @FXML
    private VBox vboxMessages;

    private Client client;


    //Initialize to connect to server localhost with port 4444
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client = new Client(new Socket("localhost", 4444));
            System.out.println("Connected to Server");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error connecting to server");
        }

        vboxMessages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number newValue) {
                pane.setVvalue((Double) newValue);
            }
        });
        client.msgFromServer(vboxMessages);


        //Button Action method
        buttonSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = field.getText();
                if (!messageToSend.isEmpty()) {
                    //Align to the right for sending message
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    // fun colors similar to a certain company :)
                    hBox.setPadding(new Insets(5,5,5,10));
                    Text newText = new Text(("You: ") + (messageToSend));
                    TextFlow newTextFlow = new TextFlow(newText);
                    newText.setFill(Color.color(.934,.945,.996));
                    newTextFlow.setPadding((new Insets(5,10,5,10)));
                    newTextFlow.setStyle("-fx-color: rgb(240,240,255);" +
                            "-fx-background-color: rgb(15,125,240);" +
                            "-fx-background-radius: 20px;");
                    hBox.getChildren().add(newTextFlow);
                    //add to GUI and clear
                    vboxMessages.getChildren().add(hBox);
                    client.sendMessageToServer(("Friend: ")+(messageToSend));
                    System.out.println(messageToSend);
                    field.clear();
                }
            }
        });
    }

    //show on gui method
    public static void addLabel(String messageFromServer, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));
        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(230,230,240);" +
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

        //We have to do this for a new thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}
