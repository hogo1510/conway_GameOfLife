import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImportFromSaved {

    public static void importFromJSON(JButton[][] buttons, int gridSize, String fileName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));

            // Parse de JSON-data
            JSONObject gridObject = new JSONObject(content);
            JSONArray gridArray = gridObject.getJSONArray("grid");

            for (int i = 0; i < gridSize; i++) {
                JSONArray rowArray = gridArray.getJSONArray(i);
                for (int j = 0; j < gridSize; j++) {
                    String cellColor = rowArray.getString(j);
                    if (cellColor.equals("black")) {
                        buttons[i][j].setBackground(Color.BLACK);
                    } else {
                        buttons[i][j].setBackground(Color.WHITE);
                    }
                }
            }

            System.out.println("Rooster succesvol geïmporteerd vanuit " + fileName);

        } catch (IOException e) {
            System.err.println("Fout bij het lezen van het bestand: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Fout bij het verwerken van JSON: " + e.getMessage());
        }
    }
}

