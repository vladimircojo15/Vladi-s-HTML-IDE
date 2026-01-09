import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class VladisHTMLIDE {
    private JFrame frame;
    private JTextArea textArea;
    private JFileChooser fileChooser;

    public static void main(String[] args) {
        // Setăm aspectul să arate ca în Windows 11
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            try {
                VladisHTMLIDE window = new VladisHTMLIDE();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public VladisHTMLIDE() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Vladi's HTML IDE");
        
        // --- SECȚIUNE ICONIȚĂ ---
        // Asigură-te că ai un fișier numit "icon.png" în folderul proiectului
        try {
            ImageIcon img = new ImageIcon("icon.png");
            frame.setIconImage(img.getImage());
        } catch (Exception e) {
            System.out.println("Nu s-a putut încărca iconița. Verifică dacă icon.png există.");
        }
        // ------------------------

        frame.setBounds(100, 100, 900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Zona de text
        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        textArea.setTabSize(4);
        
        // Adăugăm funcția de TAB (Shortcut-ul !)
        addTabShortcut();

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Bara de unelte (Toolbar)
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        frame.getContentPane().add(panel, BorderLayout.NORTH);

        JButton btnSave = new JButton("Save File (Ctrl+S)");
        btnSave.addActionListener(e -> saveToFile());
        panel.add(btnSave);

        // Shortcut tastatură pentru Salvare (Ctrl + S)
        textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "save");
        textArea.getActionMap().put("save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });

        JLabel labelHint = new JLabel("  Tip: Write ! and press TAB for a standard HTML template");
        labelHint.setForeground(Color.GRAY);
        panel.add(labelHint);

        fileChooser = new JFileChooser();
    }

    private void addTabShortcut() {
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    String text = textArea.getText();
                    int caretPos = textArea.getCaretPosition();
                    
                    if (caretPos > 0 && text.charAt(caretPos - 1) == '!') {
                        e.consume(); 
                        
                        String htmlTemplate = "<!DOCTYPE html>\n" +
                                "<html lang=\"en\">\n" +
                                "<head>\n" +
                                "    <meta charset=\"UTF-8\">\n" +
                                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                                "    <title>Document</title>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "    \n" +
                                "</body>\n" +
                                "</html>";
                        
                        textArea.replaceRange(htmlTemplate, caretPos - 1, caretPos);
                        // Poziționăm cursorul în interiorul body-ului
                        textArea.setCaretPosition(textArea.getText().indexOf("</body>") - 5);
                    }
                }
            }
        });
    }

    private void saveToFile() {
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".html")) {
                file = new File(file.getAbsolutePath() + ".html");
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(frame, "Fișier salvat cu succes!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Eroare la salvare: " + ex.getMessage());
            }
        }
    }
}