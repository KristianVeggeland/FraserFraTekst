package com.example.oblig2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SvadaApplikasjon extends Application {
    private String lagringsBuffer = "";
    private final TextProcessor processor = new TextProcessor();
    private final TextArea textArea = new TextArea();
    private final TextField inputText = new TextField();
    private final TextField inputAmount = new TextField();

    @Override
    public void start(Stage primaryStage) {
        Button loadButton = new Button("Last Inn Tekst");
        Button generateButton = new Button("Generer");
        Button saveButton = new Button("Lagre");

        loadButton.setOnAction(e -> loadText());
        generateButton.setOnAction(e -> generateText());
        saveButton.setOnAction(e -> saveText());

        Label inputTextLabel = new Label("Startkombinasjon (2 ord adskilt med '+')");
        Label inputAmountLabel = new Label("Antall ord som skal genereres");

        VBox inputTextVbox = new VBox(inputTextLabel, inputText);
        VBox inputAmountVbox = new VBox(inputAmountLabel, inputAmount);

        HBox inputHbox = new HBox(5, inputTextVbox, inputAmountVbox);
        HBox bottomButtons = new HBox(5, generateButton, saveButton);

        textArea.setWrapText(true);

        VBox layout = new VBox(10, loadButton, inputHbox, textArea, bottomButtons);
        layout.setPadding(new Insets(10,30,30,30));
        Scene scene = new Scene(layout, 500, 500);

        primaryStage.setTitle("SvadaApplikasjon");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadText() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                processor.processText(content);
                textArea.setText("Tekst lastet inn vellykket");
            } catch (IOException ex) {
                textArea.setText("Error ved lesing av fil");
            }
        }
    }

    private void generateText() {
        String generatedText = processor.generateText(inputText.getText(), Integer.parseInt(inputAmount.getText()));
        //System.out.println("AAAAAAAA " + Integer.parseInt(inputAmount.getText()));
        lagringsBuffer += generatedText + "\n";
        textArea.setText(generatedText);
    }

    private void saveText() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                Files.write(file.toPath(), lagringsBuffer.getBytes());
                textArea.setText("Tekst lagring vellykket");
            } catch (IOException ex) {
                textArea.setText("Error ved lagring av fil");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}