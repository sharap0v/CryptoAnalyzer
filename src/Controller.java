import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import java.io.*;
import java.nio.file.Path;
import java.util.Objects;


public class Controller {
    public TextArea textArea;
    public Text filePath;
    public ProgressBar progressBar;
    public TextField keyTextField;
    private String textForDecodeEncode;
    private Path path = null;

    public void openFile(ActionEvent actionEvent) {
        path = UT.openFileChooser("TXT", "*.txt");
        if (path != null) {
            filePath.setText(path.toAbsolutePath().toString());
            loadFile(path);
        }
    }


    private void loadFile(Path path) {
        Task<String> task = UT.createLoadFileTask(path);
        task.setOnSucceeded(event -> {
            textForDecodeEncode = task.getValue();
            updateTextArea(textForDecodeEncode);
        });
        progressBar.progressProperty().bind(task.progressProperty());
        runTask(task);
    }

    private void runTask(Task<String> task) {
        new Thread(task).start();
    }

    private void updateTextArea(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textArea.setText(text);
            }
        });
    }

    public void decrypt(ActionEvent actionEvent) {
        // расшифровка
        int key = calculateKey();
        if (key != 0) {
            textForDecodeEncode = EncodeDecode.encodeDecode(textForDecodeEncode, key);
            updateTextArea(textForDecodeEncode);
        }
    }

    public void encrypt(ActionEvent actionEvent) {
        //зашифровка
        int key = calculateKey();
        if (key != 0) {
            textForDecodeEncode = EncodeDecode.encodeDecode(textForDecodeEncode, key * -1);
            updateTextArea(textForDecodeEncode);
        }

    }

    private int calculateKey() {
        String key = keyTextField.getText();
        int k = 0;
        try {
            k = Integer.parseInt(key);
        } catch (NumberFormatException e) {
            createAlertStage("Ты вводишь какую то дичь!" + key + "\r\n Введи число!!!");
        }
        return k;
    }


    public void saveFile(ActionEvent actionEvent) {
        try {
            createAlertStage("Файл успешно сохранен \r\n" + UT.saveFile(path, textForDecodeEncode));
        } catch (IOException e) {
            createAlertStage("Ошибка записи в файл");
        }

    }

    private static void createAlertStage(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(Controller.class.getResource("/css/dark-theme.css")).toExternalForm());
        alert.getDialogPane().setStyle("-fx-background-radius: 0;");
        UT.print(alert.getModality());
        alert.initModality(Modality.WINDOW_MODAL);
        UT.print(alert.getModality());
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initOwner(Main.primaryStage);
        alert.setX(Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2 - 150);
        alert.setY(Main.primaryStage.getY() + Main.primaryStage.getHeight() / 2);
        alert.setResizable(false);
        alert.showAndWait();
    }

    public void bruteForce(ActionEvent actionEvent) {
        //подбираем с помощью брут
        updateTextArea(EncodeDecode.brute(textForDecodeEncode));
    }
}
