import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class ExportToJson {

    public static void exportToJSON(JButton[][] buttons, int gridSize, String fileName) {
        JSONArray gridArray = new JSONArray();

        for (int i = 0; i < gridSize; i++) {
            JSONArray rowArray = new JSONArray();
            for (int j = 0; j < gridSize; j++) {
                if (buttons[i][j].getBackground().equals(Color.BLACK)) {
                    rowArray.put("black");
                } else {
                    rowArray.put("white");
                }
            }
            gridArray.put(rowArray);
        }

        JSONObject gridObject = new JSONObject();
        gridObject.put("grid", gridArray);

        try (FileWriter file = new FileWriter(fileName)) {
            file.write(gridObject.toString(4)); //met 4 spaties voor leesbaarheid
            System.out.println("Rooster succesvol geëxporteerd naar " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
