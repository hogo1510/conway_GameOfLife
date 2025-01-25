
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RoosterFrame();
        });
    }
}
class RoosterFrame extends JFrame {
    private static final int GRID_SIZE = 24;
    private JButton[][] buttons;
    public RoosterFrame() {
        setTitle("Game Of Lide");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        add(gridPanel, BorderLayout.CENTER);

        buttons = new JButton[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE); // Standaard wit
                button.setPreferredSize(new Dimension(50, 50));
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (button.getBackground().equals(Color.WHITE)) {
                            button.setBackground(Color.BLACK);
                        } else {
                            button.setBackground(Color.WHITE);
                        }
                    }
                });
                buttons[i][j] = button;
                gridPanel.add(button);
            }
        }

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        add(settingsPanel, BorderLayout.EAST);

        JButton playButton = new JButton("Volgende Generatie");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color[][] nextState = new Color[GRID_SIZE][GRID_SIZE];//temp array

                //bereken de volgende staat
                for (int i = 0; i < GRID_SIZE; i++) {
                    for (int j = 0; j < GRID_SIZE; j++) {
                        if (buttons[i][j].getBackground().equals(Color.WHITE)) {
                            int surroundingBlacks = countSurroundingBlackForWhite(i, j);
                            if (surroundingBlacks == 3) {
                                nextState[i][j] = Color.BLACK;
                            } else {
                                nextState[i][j] = Color.WHITE;
                            }
                        } else {
                            int surroundingBlacks = countSurroundingBlacksForBlack(i, j);
                            if (surroundingBlacks == 2 || surroundingBlacks == 3) {
                                nextState[i][j] = Color.BLACK;
                            } else {
                                nextState[i][j] = Color.WHITE;
                            }
                        }
                    }
                }

                //volgende staat
                for (int i = 0; i < GRID_SIZE; i++) {
                    for (int j = 0; j < GRID_SIZE; j++) {
                        buttons[i][j].setBackground(nextState[i][j]);
                    }
                }
            }

            private int countSurroundingBlackForWhite(int i, int j) {
                int blackNeighborCount = 0;

                int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1};
                int[] dy = {0, 0, -1, 1, -1, 1, -1, 1};

                for (int k = 0; k < 8; k++) {
                    int newX = i + dx[k];
                    int newY = j + dy[k];

                    if (newX >= 0 && newX < GRID_SIZE && newY >= 0 && newY < GRID_SIZE) {
                        if (buttons[newX][newY].getBackground().equals(Color.BLACK)) {
                            blackNeighborCount++;
                        }
                    }
                }
                return blackNeighborCount;
            }

            private int countSurroundingBlacksForBlack(int i, int j) {
                return countSurroundingBlackForWhite(i, j); //zelfde logica
            }
        });
        settingsPanel.add(playButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < GRID_SIZE; i++) {
                    for (int j = 0; j < GRID_SIZE; j++) {
                        buttons[i][j].setBackground(Color.WHITE);
                    }
                }
            }
        });
        settingsPanel.add(clearButton);
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        settingsPanel.add(exitButton);
        JButton importButton = new JButton("Import");
        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecteer een JSON-bestand");

            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON-bestanden", "json"));

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                ImportFromSaved.importFromJSON(buttons, GRID_SIZE, fileName);
            } else {
                System.out.println("Importeren geannuleerd.");
            }
        });
        settingsPanel.add(importButton);

        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(e -> {
            JTextField textField = new JTextField(10);

            Object[] message = {
                    "Voer bestands naam in:", textField
            };

            int option = JOptionPane.showConfirmDialog(
                    null,
                    message,
                    "Invoer",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (option == JOptionPane.OK_OPTION) {
                String input = textField.getText(); // Haal tekst uit het veld
                System.out.println("Ingevoerde tekst: " + input);

                ExportToJson.exportToJSON(buttons, GRID_SIZE, input + ".json");
            } else {
                System.out.println("Actie geannuleerd.");
            }
        });

        settingsPanel.add(exportButton);
        settingsPanel.setPreferredSize(new Dimension(150, getHeight()));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}