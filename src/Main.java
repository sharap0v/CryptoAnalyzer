
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Main extends Application {
    public static TrayIcon trayIcon;
    public static java.awt.Image trayImage;
    public static Stage primaryStage;
    public static SystemTray tray;

    @Override
    public void start(Stage stage) throws Exception {
        trayImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/16-16.png")));
        Main.primaryStage = stage;
        //трей не пашет в линукс таким способом поэтому проверяем
        String osName = System.getProperty("os.name");
        UT.print(osName);
        if (!osName.equals("Linux")) {
            trayIcon = new TrayIcon(trayImage);
            tray = SystemTray.getSystemTray();
            Platform.setImplicitExit(false);
            SwingUtilities.invokeLater(this::addAppToTray);
        }
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Main.fxml")));
        root.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
        primaryStage.setTitle("");
        Scene scene = new Scene(root,Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setResizable(true);
        primaryStage.setFullScreen(false);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void addAppToTray() {
        try {
            Toolkit.getDefaultToolkit();

            if (!SystemTray.isSupported()) {
                UT.print("No system tray support, application exiting.");
                Platform.exit();
            }
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            MenuItem openItem = new MenuItem("Открыть Крипто Анализатор");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            MenuItem hideItem = new MenuItem("Свернуть");
            hideItem.addActionListener(event -> Platform.runLater(this::hideProgram));

            MenuItem exitItem = new MenuItem("Выход");
            exitItem.addActionListener(event -> exitProgram());

            Font defaultFont = Font.decode(null);
            Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(hideItem);
            popup.addSeparator();

            popup.add(exitItem);
            Main.trayIcon.setPopupMenu(popup);
            Main.tray.add(Main.trayIcon);
        } catch (AWTException e) {
            UT.print("Unable to init system tray");
        }
    }

    private void showStage() {
        UT.print("showStage");
        if (primaryStage != null) {
            primaryStage.show();
            primaryStage.toFront();
        }
    }

    public void exitProgram() {
        UT.print("exit");
        Platform.exit();
        Main.tray.remove(Main.trayIcon);
        System.exit(0);
    }

    public void hideProgram() {
        UT.print("hideProgram");
        if (primaryStage != null) {
            primaryStage.hide();
        }
    }


}
