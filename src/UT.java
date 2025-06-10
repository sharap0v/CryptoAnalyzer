import javafx.concurrent.Task;
import javafx.stage.FileChooser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class UT {
    private static boolean printConsole = false; // переменная для отключения вывода в консоль лучше использовать логер
    private static final FileChooser fileChooser = new FileChooser();

    public static void print(Object msg) {
        //печать в консоль
        if (printConsole) {
            System.out.println(msg);
        }
    }

    public static Path openFileChooser(String description, String extensions) {
        //открывает окно для выбора файла
        fileChooser.getExtensionFilters().clear();
        fileChooser.setInitialDirectory(new File(new File(".").getAbsolutePath()));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extensions));
        File file = fileChooser.showOpenDialog(Main.primaryStage);
        return file != null ? file.toPath() : null;
    }

    public static Path saveFile(Path path, String text) throws IOException {
        UT.print("saveFile");
        Path encryptPath = Paths.get(path.getParent().toAbsolutePath()
                + File.separator +
                path.getFileName().toString().replace(".txt", "_crypt.txt"));
        UT.print(encryptPath.toString());
        Files.write(encryptPath, Collections.singletonList(text), StandardCharsets.UTF_8);
        return encryptPath;
    }

    public static Task<String> createLoadFileTask(Path path){
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                File file = new File(path.toAbsolutePath().toString());
                long fileLength = file.length();
                byte[] buffer = new byte[4096];
                StringBuilder sb = new StringBuilder();
                try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                    int bytesRead;
                    long totalBytesRead = 0;

                    while ((bytesRead = bis.read(buffer)) != -1) {
                        totalBytesRead += bytesRead;
                        updateProgress(totalBytesRead, fileLength);
                        sb.append(new String(buffer));
                    }
                }
                return sb.toString();
            }
        };
    }

}
