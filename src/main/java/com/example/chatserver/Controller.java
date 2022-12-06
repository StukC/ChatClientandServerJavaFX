package com.example.chatserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public AnchorPane ap_main;
    @FXML
    private ScrollPane pane;

    @FXML
    private Button buttonSend;

    @FXML
    private TextField field;

    @FXML
    private VBox vboxMessages;

    private Server server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //creating socket to communicate to client
            server = new Server(new ServerSocket(4444));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server Error");
        }
        //Change vertical height for scroll box to make it work
        vboxMessages.heightProperty().addListener((observableValue, oldValue, newValue) -> pane.setVvalue((Double) newValue));
        server.receiveMessageFromClient(vboxMessages);

        buttonSend.setOnAction((event -> {
            String messageToSend = field.getText();
            if (!messageToSend.isEmpty()) {
                //Alignment to know who sent it
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 5, 10));

                //pretty colors
                Text newText = new Text(("You: ") + (messageToSend));
                TextFlow textFlow = new TextFlow(newText);
                textFlow.setStyle("-fx-color: rgb(240,240,255);" +
                        "-fx-background-color: rgb(15,125,240);" +
                        "-fx-background-radius: 20px;");
                textFlow.setPadding((new Insets(5, 10, 5, 10)));
                newText.setFill(Color.color(.935, .945, .995));
                hBox.getChildren().add(textFlow);

                //send and clear
                vboxMessages.getChildren().add(hBox);
                server.sendMessageToClient(("Friend: ")+(messageToSend));
                System.out.println(messageToSend);
                field.clear();

            }
        }));

    }

    //Show on gui method
    public static void addLabel (String messageFromClient, VBox vbox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));
        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(230,230,240);" +
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> vbox.getChildren().add(hBox));
    }

}
