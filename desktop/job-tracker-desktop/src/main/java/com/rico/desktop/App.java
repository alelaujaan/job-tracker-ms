package com.rico.desktop;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;

public class App extends Application {

  private static final String IMPORT_URL = "http://localhost:8082/import";
  private static final String LIST_URL = "http://localhost:8081/applications";

  private static final String ANGULAR_URL = "http://localhost:4200";

private static final Path ANGULAR_DIR =
        Path.of("..", "..", "frontend", "job-tracker-ui").normalize();


  private Process angularProcess;

  private final HttpClient http = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(5))
      .build();

  private final ObjectMapper mapper = new ObjectMapper();

  private final TextField urlField = new TextField();
  private final TextArea output = new TextArea();

  @Override
  public void start(Stage stage) {
    urlField.setPromptText("Pega URL de InfoJobs/LinkedIn...");

    Button importBtn = new Button("Importar");
    importBtn.setOnAction(e -> importUrl());

    Button listBtn = new Button("Listar candidaturas");
    listBtn.setOnAction(e -> listApplications());

    Button webBtn = new Button("Lanzar Web Angular");
    webBtn.setOnAction(e -> startAndOpenAngular());

    HBox actions = new HBox(10, importBtn, listBtn, webBtn);

    output.setEditable(false);
    output.setPrefRowCount(20);

    VBox root = new VBox(10,
        new Label("Job Tracker Desktop"),
        urlField,
        actions,
        output
    );
    root.setPadding(new Insets(12));

    stage.setScene(new Scene(root, 900, 520));
    stage.setTitle("Job Tracker Desktop");
    stage.show();
  }

  private void startAndOpenAngular() {
    try {
      // Si ya está arrancado, solo abrir navegador
      if (angularProcess != null && angularProcess.isAlive()) {
        Desktop.getDesktop().browse(URI.create(ANGULAR_URL));
        return;
      }

      File dir = ANGULAR_DIR.toFile();
      if (!dir.exists()) {
        output.setText("❌ No encuentro la carpeta Angular:\n" + dir.getAbsolutePath()
            + "\n\nAjusta ANGULAR_DIR en App.java");
        return;
      }

      // Ejecutar "npm start"
      ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "npm", "start");
      pb.directory(dir);
      pb.redirectErrorStream(true);

      angularProcess = pb.start();
      output.setText("🚀 Angular arrancando...\n" + dir.getAbsolutePath()
          + "\n\nSe abrirá en unos segundos: " + ANGULAR_URL);

      // Abrir navegador tras unos segundos (simple)
      new Thread(() -> {
        try {
          Thread.sleep(6000);
          Desktop.getDesktop().browse(URI.create(ANGULAR_URL));
        } catch (Exception ex) {
          output.setText("❌ Error abriendo navegador: " + ex.getMessage());
        }
      }).start();

    } catch (IOException ex) {
      output.setText("❌ Error lanzando Angular: " + ex.getMessage()
          + "\n\n¿Tienes Node/NPM instalado? ¿Funciona 'npm -v' en una terminal?");
    }
  }

  private void importUrl() {
    try {
      String url = urlField.getText().trim();
      if (url.isEmpty()) {
        output.setText("❌ Escribe una URL primero.");
        return;
      }

      String body = mapper.writeValueAsString(new ScrapeRequest(url));

      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(IMPORT_URL))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      output.setText("IMPORT Status: " + res.statusCode() + "\n\n" + res.body());

    } catch (Exception ex) {
      output.setText("❌ Error importando: " + ex.getMessage());
    }
  }

  private void listApplications() {
    try {
      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(LIST_URL))
          .GET()
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      output.setText("LIST Status: " + res.statusCode() + "\n\n" + res.body());

    } catch (Exception ex) {
      output.setText("❌ Error listando: " + ex.getMessage());
    }
  }

  public record ScrapeRequest(String url) {}

  public static void main(String[] args) {
    launch(args);
  }
}
